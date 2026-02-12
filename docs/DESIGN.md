# Design Document - Finance Management Application

## 1. Architecture Overview

### 1.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌─────────┐ │
│  │ Activity │  │ Fragment │  │ViewModel │  │ Adapter │ │
│  └──────────┘  └──────────┘  └──────────┘  └─────────┘ │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────┴────────────────────────────────┐
│                     DOMAIN LAYER                        │
│  ┌──────────┐  ┌──────────────┐  ┌──────────────────┐ │
│  │  Models  │  │  Use Cases   │  │  Repository      │ │
│  │          │  │              │  │  Interfaces      │ │
│  └──────────┘  └──────────────┘  └──────────────────┘ │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────┴────────────────────────────────┐
│                      DATA LAYER                         │
│  ┌──────────┐  ┌──────────┐  ┌────────────────────┐   │
│  │ Entities │  │   DAOs   │  │   Repositories     │   │
│  │          │  │          │  │   (Implementations)│   │
│  └──────────┘  └──────────┘  └────────────────────┘   │
│  ┌─────────────────────────────────────────────────┐   │
│  │           Room Database (SQLite)                │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### 1.2 Design Patterns

- **MVVM:** Separation of UI from business logic
- **Repository Pattern:** Abstract data sources
- **Use Case Pattern:** Single responsibility business logic
- **Observer Pattern:** LiveData/Flow for reactive updates
- **Singleton Pattern:** Database, repository instances
- **Factory Pattern:** ViewModelFactory via Hilt
- **Adapter Pattern:** RecyclerView adapters

---

## 2. Database Design

### 2.1 Entity Relationship Diagram

```
┌──────────────┐         ┌──────────────────┐
│    User      │         │   BankAccount    │
├──────────────┤         ├──────────────────┤
│ userId (PK)  │◄────────│ userId (FK)      │
│ name         │         │ accountId (PK)   │
│ email        │         │ bankName         │
│ passwordHash │         │ accountName      │
│ upiIdEnc     │         │ balance          │
│ createdAt    │         │ isMainAccount    │
└──────────────┘         │ createdAt        │
                         └──────────────────┘
                                 │
                                 │
                         ┌───────▼──────────┐
                         │   Transaction    │
                         ├──────────────────┤
                         │ transactionId(PK)│
                         │ userId (FK)      │
                         │ accountId (FK)   │
                         │ amount           │
                         │ type             │
                         │ category         │
                         │ description      │
                         │ receiptPath      │
                         │ timestamp        │
                         └──────────────────┘
                                 
┌──────────────┐         ┌──────────────────┐
│    Budget    │         │ RecurringTrans   │
├──────────────┤         ├──────────────────┤
│ budgetId (PK)│         │ recurringId (PK) │
│ userId (FK)  │         │ userId (FK)      │
│ targetAmount │         │ accountId (FK)   │
│ spentAmount  │         │ amount           │
│ period       │         │ frequency        │
│ startDate    │         │ nextDueDate      │
│ endDate      │         │ isActive         │
└──────────────┘         └──────────────────┘

┌──────────────┐         ┌──────────────────┐
│ SavingsGoal  │         │  SplitTransaction│
├──────────────┤         ├──────────────────┤
│ goalId (PK)  │         │ splitId (PK)     │
│ userId (FK)  │         │ transactionId(FK)│
│ goalName     │         │ participantName  │
│ targetAmount │         │ amountOwed       │
│ currentAmount│         │ isPaid           │
│ deadline     │         └──────────────────┘
└──────────────┘
```

### 2.2 Database Schema

**Users Table**
```sql
CREATE TABLE users (
    userId TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    passwordHash TEXT NOT NULL,
    upiIdEncrypted TEXT,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER
);
CREATE INDEX idx_user_email ON users(email);
```

**BankAccounts Table**
```sql
CREATE TABLE bank_accounts (
    accountId INTEGER PRIMARY KEY AUTOINCREMENT,
    userId TEXT NOT NULL,
    bankName TEXT NOT NULL,
    accountName TEXT NOT NULL,
    balance REAL NOT NULL DEFAULT 0,
    isMainAccount INTEGER NOT NULL DEFAULT 0,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);
CREATE INDEX idx_account_user ON bank_accounts(userId);
```

**Transactions Table**
```sql
CREATE TABLE transactions (
    transactionId INTEGER PRIMARY KEY AUTOINCREMENT,
    userId TEXT NOT NULL,
    accountId INTEGER NOT NULL,
    amount REAL NOT NULL,
    type TEXT NOT NULL CHECK(type IN ('DEBIT', 'CREDIT')),
    category TEXT NOT NULL,
    description TEXT NOT NULL,
    receiptPhotoPath TEXT,
    timestamp INTEGER NOT NULL,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY (accountId) REFERENCES bank_accounts(accountId) ON DELETE CASCADE
);
CREATE INDEX idx_transaction_user ON transactions(userId);
CREATE INDEX idx_transaction_timestamp ON transactions(timestamp);
CREATE INDEX idx_transaction_category ON transactions(category);
CREATE INDEX idx_transaction_account ON transactions(accountId);
```

**Budgets Table**
```sql
CREATE TABLE budgets (
    budgetId INTEGER PRIMARY KEY AUTOINCREMENT,
    userId TEXT NOT NULL,
    targetAmount REAL NOT NULL,
    spentAmount REAL NOT NULL DEFAULT 0,
    period TEXT NOT NULL CHECK(period IN ('MONTHLY', 'YEARLY')),
    startDate INTEGER NOT NULL,
    endDate INTEGER NOT NULL,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);
CREATE INDEX idx_budget_user ON budgets(userId);
CREATE INDEX idx_budget_dates ON budgets(startDate, endDate);
```

---

## 3. UI/UX Design

### 3.1 Screen Flow

```
Splash Screen
      │
      ▼
Login/Register ──► Dashboard ──┬──► Profile
                      │         │
                      │         ├──► Budget Management
                      │         │
                      │         ├──► Transaction List
                      │         │         │
                      │         │         ├──► Add Transaction
                      │         │         │
                      │         │         └──► Transaction Detail
                      │         │
                      │         ├──► Analytics/Reports
                      │         │
                      │         └──► Settings
                      │
                      └──► (Quick Add Transaction - FAB)
```

### 3.2 Color Scheme

**Light Theme:**
- Primary: #6200EE (Purple)
- Primary Variant: #3700B3
- Secondary: #03DAC6 (Teal)
- Background: #FFFFFF
- Surface: #F5F5F5
- Error: #B00020
- Success: #4CAF50

**Dark Theme:**
- Primary: #BB86FC
- Primary Variant: #3700B3
- Secondary: #03DAC6
- Background: #121212
- Surface: #1E1E1E
- Error: #CF6679
- Success: #81C784

### 3.3 Typography

- **Headings:** Roboto Bold, 24sp
- **Subheadings:** Roboto Medium, 18sp
- **Body:** Roboto Regular, 16sp
- **Captions:** Roboto Regular, 14sp
- **Amounts:** Roboto Mono, 20sp (for precision)

### 3.4 Key Screens Layout

**Dashboard Layout:**
```
┌─────────────────────────────────┐
│  ☰  Dashboard          👤       │
├─────────────────────────────────┤
│                                 │
│  ┌─────────────────────────┐   │
│  │   Total Balance         │   │
│  │   ₹ 45,230.50          │   │
│  └─────────────────────────┘   │
│                                 │
│  ┌───────────┐ ┌───────────┐  │
│  │  Income   │ │ Expenses  │  │
│  │  ₹15,000  │ │ ₹8,500    │  │
│  └───────────┘ └───────────┘  │
│                                 │
│  Budget Progress                │
│  ████████░░░░ 65%              │
│                                 │
│  Recent Transactions            │
│  ┌───────────────────────────┐ │
│  │ 🍔 Food - ₹350            │ │
│  │ 🚗 Transport - ₹200       │ │
│  │ 💳 Shopping - ₹1,200      │ │
│  └───────────────────────────┘ │
│                                 │
│                         [+]     │
└─────────────────────────────────┘
```

**Add Transaction Dialog:**
```
┌─────────────────────────────────┐
│  Add Transaction                │
├─────────────────────────────────┤
│                                 │
│  Amount:                        │
│  ┌──────────────────────────┐  │
│  │ ₹ [________]             │  │
│  └──────────────────────────┘  │
│                                 │
│  Description:                   │
│  ┌──────────────────────────┐  │
│  │ [_________________]      │  │
│  └──────────────────────────┘  │
│                                 │
│  Category:                      │
│  ┌──────────────────────────┐  │
│  │ 🍔 Food             ▼    │  │
│  └──────────────────────────┘  │
│                                 │
│  Type: [Debit] [Credit]        │
│                                 │
│  Account: Main Bank Account    │
│                                 │
│  [Cancel]          [Add]        │
└─────────────────────────────────┘
```

---

## 4. Component Design

### 4.1 ViewModel Architecture

```kotlin
// State management pattern
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// ViewModel structure
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
    private val getBudgetStatusUseCase: GetBudgetStatusUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    fun loadDashboard() {
        viewModelScope.launch {
            // Combine multiple flows
            combine(
                getBalanceUseCase(),
                getRecentTransactionsUseCase(),
                getBudgetStatusUseCase()
            ) { balance, transactions, budget ->
                DashboardUiState.Success(
                    balance = balance,
                    recentTransactions = transactions,
                    budgetStatus = budget
                )
            }.catch { e ->
                _uiState.value = DashboardUiState.Error(e.message ?: "Unknown error")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
```

### 4.2 Repository Pattern

```kotlin
interface TransactionRepository {
    fun getTransactions(userId: String): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction): Result<Long>
    suspend fun updateTransaction(transaction: Transaction): Result<Unit>
    suspend fun deleteTransaction(transactionId: Long): Result<Unit>
    fun searchTransactions(query: String): Flow<List<Transaction>>
}

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao,
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {
    
    override suspend fun addTransaction(transaction: Transaction): Result<Long> {
        return withContext(ioDispatcher) {
            try {
                database.withTransaction {
                    // Multi-step atomic operation
                    val id = transactionDao.insert(transaction.toEntity())
                    updateAccountBalance(transaction)
                    updateBudget(transaction)
                    Result.success(id)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
```

---

## 5. Security Architecture

### 5.1 Encryption Flow

```
User Password
      │
      ▼
   BCrypt Hash (12 rounds)
      │
      ▼
   Store in Database

Sensitive Data (UPI ID)
      │
      ▼
   Android Keystore
      │
      ▼
   AES-256-GCM Encryption
      │
      ▼
   Store Encrypted in Database
```

### 5.2 Authentication Flow

```
User Login
      │
      ▼
   Verify Email Exists
      │
      ▼
   BCrypt.checkpw(input, storedHash)
      │
      ├──► Valid ──► Generate Session Token
      │                     │
      │                     ▼
      │              Store in EncryptedSharedPrefs
      │                     │
      │                     ▼
      │              Navigate to Dashboard
      │
      └──► Invalid ──► Show Error
                       Increment Failed Attempts
```

---

## 6. Performance Optimization

### 6.1 Database Query Optimization

- **Indices:** userId, timestamp, category, accountId
- **Pagination:** Load 50 transactions at a time
- **Caching:** Dashboard data cached for 5 minutes
- **Database Views:** Pre-calculated aggregations

### 6.2 UI Optimization

- **RecyclerView:** ViewHolder pattern with DiffUtil
- **Image Loading:** Glide with disk cache
- **Lazy Loading:** Load on scroll
- **Background Processing:** WorkManager for heavy tasks

---

## 7. Testing Strategy

```
┌──────────────────────────────────────┐
│         TESTING PYRAMID              │
│                                      │
│           ┌─────┐                   │
│           │  E2E │ (5%)             │
│           └─────┘                   │
│         ┌─────────┐                 │
│         │   UI    │ (15%)           │
│         └─────────┘                 │
│     ┌───────────────┐               │
│     │ Integration   │ (30%)         │
│     └───────────────┘               │
│ ┌───────────────────────┐           │
│ │       Unit Tests      │ (50%)     │
│ └───────────────────────┘           │
└──────────────────────────────────────┘
```

---

**Document Status:** Living Document  
**Last Updated:** February 5, 2026  
**Next Review:** Upon Phase 1 Completion
