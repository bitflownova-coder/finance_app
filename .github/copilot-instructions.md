# GitHub Copilot Instructions - Finance Management App

## Project Context

You are working on a **Finance Management Android Application** built with:
- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture (Data, Domain, Presentation layers)
- **Database:** Room (SQLite with type converters)
- **DI:** Hilt (Dagger)
- **Async:** Kotlin Coroutines + Flow
- **UI:** Jetpack Compose or XML with ViewBinding
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)

## Code Style & Conventions

### Kotlin Style
- Use Kotlin idioms (data classes, sealed classes, extension functions)
- Prefer `val` over `var`
- Use descriptive names: `calculateMonthlyExpenses()` not `calc()`
- Null safety: use safe calls `?.` and Elvis operator `?:`
- Collection operations: `filter`, `map`, `groupBy` instead of loops

### Package Structure
```
com.yourcompany.financeapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entities/
│   ├── repository/
│   └── mapper/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── presentation/
    ├── auth/
    ├── dashboard/
    ├── transaction/
    ├── profile/
    ├── budget/
    └── common/
```

### Naming Conventions
- **Entities:** `TransactionEntity`, `BankAccountEntity`
- **Domain Models:** `Transaction`, `BankAccount`
- **DAOs:** `TransactionDao`, `BankAccountDao`
- **Repositories:** `TransactionRepository`, `BankAccountRepository`
- **Use Cases:** `AddTransactionUseCase`, `GetMonthlyReportUseCase`
- **ViewModels:** `DashboardViewModel`, `TransactionViewModel`
- **Fragments/Activities:** `DashboardFragment`, `LoginActivity`
- **Layout files:** `fragment_dashboard.xml`, `item_transaction.xml`

## Architecture Guidelines

### Layer Responsibilities

**Data Layer:**
- Room entities with proper annotations
- DAOs return `Flow<T>` or `suspend fun`
- Repositories implement domain interfaces
- Map entities to domain models

**Domain Layer:**
- Pure Kotlin (no Android dependencies)
- Business logic in use cases
- Repository interfaces
- Domain models (not entities)

**Presentation Layer:**
- ViewModels use `viewModelScope`
- Observe data with `LiveData` or `StateFlow`
- UI state classes: `data class DashboardUiState(...)`
- No business logic in UI

### Dependency Injection (Hilt)

Always inject dependencies:
```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel()
```

Module examples:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(...)
    }
}
```

## Database Guidelines

### Entity Definition
```kotlin
@Entity(
    tableName = "transactions",
    indices = [Index("userId"), Index("timestamp"), Index("category")],
    foreignKeys = [
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Long = 0,
    val userId: String,
    val accountId: Long,
    val amount: Double, // Or Long for paise
    val type: String, // "DEBIT" or "CREDIT"
    val category: String,
    val description: String,
    @ColumnInfo(name = "created_at") val timestamp: Long
)
```

### DAO Best Practices
```kotlin
@Dao
interface TransactionDao {
    // Return Flow for reactive updates
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactions(userId: String): Flow<List<TransactionEntity>>
    
    // Suspend for one-time operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long
    
    // Direct update for atomic operations
    @Query("UPDATE accounts SET balance = balance - :amount WHERE accountId = :accountId")
    suspend fun decreaseBalance(accountId: Long, amount: Double)
    
    // Database transaction for consistency
    @Transaction
    @Query("SELECT * FROM transactions WHERE transactionId = :id")
    suspend fun getTransactionWithAccount(id: Long): TransactionWithAccount
}
```

### Type Converters
```kotlin
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}
```

## Coroutines & Flow

### ViewModel Usage
```kotlin
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadTransactions()
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getTransactions()
                .catch { e -> 
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
                .collect { transactions ->
                    _uiState.value = UiState.Success(transactions)
                }
        }
    }
    
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                repository.addTransaction(transaction)
                // Success handled by Flow updates
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to add")
            }
        }
    }
}
```

### Repository Pattern
```kotlin
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao,
    private val mapper: TransactionMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {
    
    override fun getTransactions(userId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactions(userId)
            .map { entities -> entities.map { mapper.toDomain(it) } }
            .flowOn(ioDispatcher)
    }
    
    override suspend fun addTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        database.withTransaction {
            // Insert transaction
            val entity = mapper.toEntity(transaction)
            transactionDao.insert(entity)
            
            // Update account balance
            if (transaction.type == TransactionType.DEBIT) {
                accountDao.decreaseBalance(transaction.accountId, transaction.amount)
                budgetDao.increaseSpent(transaction.userId, transaction.amount)
            } else {
                accountDao.increaseBalance(transaction.accountId, transaction.amount)
            }
        }
    }
}
```

## Security Best Practices

### Password Hashing
```kotlin
object SecurityUtils {
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }
    
    fun verifyPassword(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}
```

### Encrypted Storage
```kotlin
object SecurePreferences {
    fun getEncryptedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
            
        return EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
```

## Testing Guidelines

### Unit Test Example
```kotlin
@Test
fun `addTransaction should decrease account balance for debit transaction`() = runTest {
    // Given
    val transaction = Transaction(
        amount = 100.0,
        type = TransactionType.DEBIT,
        accountId = 1L
    )
    val initialBalance = 1000.0
    
    // When
    repository.addTransaction(transaction)
    
    // Then
    val newBalance = accountDao.getBalance(1L)
    assertEquals(900.0, newBalance, 0.01)
}
```

### ViewModel Test
```kotlin
@Test
fun `loadTransactions should emit success state with transactions`() = runTest {
    // Given
    val transactions = listOf(mockTransaction1, mockTransaction2)
    coEvery { repository.getTransactions() } returns flowOf(transactions)
    
    // When
    viewModel.loadTransactions()
    
    // Then
    val state = viewModel.uiState.value
    assertTrue(state is UiState.Success)
    assertEquals(transactions, (state as UiState.Success).data)
}
```

## UI Guidelines

### Fragment with ViewBinding
```kotlin
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: DashboardViewModel by viewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> showLoading()
                        is UiState.Success -> showData(state.data)
                        is UiState.Error -> showError(state.message)
                    }
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

### RecyclerView Adapter
```kotlin
class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(TransactionDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(transaction: Transaction) {
            binding.apply {
                textDescription.text = transaction.description
                textAmount.text = formatAmount(transaction.amount)
                textCategory.text = transaction.category
                root.setOnClickListener { onItemClick(transaction) }
            }
        }
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem.id == newItem.id
    
    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem == newItem
}
```

## Common Patterns

### Result Wrapper
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

### Use Case Pattern
```kotlin
class GetMonthlyExpensesUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(month: Int, year: Int): Result<Double> {
        return try {
            val total = repository.getMonthlyExpenses(month, year)
            Result.Success(total)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

## Performance Considerations

- Use `Flow` instead of `LiveData` for better performance
- Implement pagination with Paging 3 library for large lists
- Use `DiffUtil` for RecyclerView updates
- Add database indices on frequently queried columns
- Cache expensive calculations in ViewModel
- Use WorkManager for background tasks, not Service

## Things to Avoid

- ❌ Don't perform database operations on main thread
- ❌ Don't hold Context references in ViewModel
- ❌ Don't store passwords in plain text
- ❌ Don't use GlobalScope for coroutines
- ❌ Don't hardcode strings (use resources)
- ❌ Don't forget to cancel coroutines
- ❌ Don't use Float/Double directly for money (precision issues)
- ❌ Don't skip error handling

## When Generating Code

1. **Always** use proper error handling
2. **Always** use appropriate coroutine scopes
3. **Always** follow the established architecture
4. **Always** add necessary imports
5. **Consider** edge cases (null, empty, negative values)
6. **Consider** thread safety for concurrent operations
7. **Include** proper documentation for complex logic
8. **Follow** Material Design guidelines for UI

---

**Remember:** This is a financial application - accuracy, security, and reliability are paramount! Double-check calculations, always encrypt sensitive data, and handle errors gracefully.
