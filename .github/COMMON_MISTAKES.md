# Common Mistakes to Avoid - Finance Management App

## Database & Data Management

### ❌ Mistake 1: Performing Database Operations on Main Thread
```kotlin
// WRONG
val transactions = transactionDao.getAllTransactions() // Blocks UI
```
**Impact:** App freezes, ANR (Application Not Responding) errors

**Solution:** Always use coroutines with suspend functions
```kotlin
// CORRECT
viewModelScope.launch {
    val transactions = transactionDao.getAllTransactions()
}
```

### ❌ Mistake 2: Not Using Database Transactions for Multi-Step Operations
```kotlin
// WRONG
accountDao.decreaseBalance(accountId, amount)
transactionDao.insert(transaction)
budgetDao.increaseSpent(amount)
// If middle operation fails, data becomes inconsistent
```
**Impact:** Data inconsistency, incorrect balances

**Solution:** Wrap in database transaction
```kotlin
// CORRECT
database.withTransaction {
    accountDao.decreaseBalance(accountId, amount)
    transactionDao.insert(transaction)
    budgetDao.increaseSpent(amount)
}
```

### ❌ Mistake 3: Storing Decimal Money Values as Float/Double
```kotlin
// WRONG
@Entity
data class Transaction(
    val amount: Double // Precision errors!
)
```
**Impact:** Rounding errors (0.1 + 0.2 ≠ 0.3), incorrect calculations

**Solution:** Store as Long (paise/cents) or use BigDecimal
```kotlin
// CORRECT - Store in smallest unit (paise)
@Entity
data class Transaction(
    val amountInPaise: Long // ₹100.50 = 10050 paise
)

// Or use Double but format for display only
fun Double.toRupees() = "₹%.2f".format(this)
```

### ❌ Mistake 4: Not Adding Database Indices
```kotlin
// WRONG - Slow queries on large datasets
@Entity(tableName = "transactions")
data class Transaction(...)

// Query with WHERE userId = ? becomes slow
```
**Impact:** Slow queries, poor app performance

**Solution:** Add indices on frequently queried columns
```kotlin
// CORRECT
@Entity(
    tableName = "transactions",
    indices = [Index("userId"), Index("timestamp"), Index("category")]
)
data class Transaction(...)
```

### ❌ Mistake 5: No Database Migration Strategy
```kotlin
// WRONG - Just bump version, app crashes
@Database(entities = [...], version = 2)
```
**Impact:** App crashes on update, users lose all data

**Solution:** Implement proper migrations
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE transactions ADD COLUMN receiptPath TEXT")
    }
}
```

## Architecture & Memory Management

### ❌ Mistake 6: ViewModels Holding Context References
```kotlin
// WRONG
class TransactionViewModel(private val context: Context) : ViewModel() {
    // Memory leak!
}
```
**Impact:** Memory leaks, app crashes

**Solution:** Use Application context or pass Context as function parameter
```kotlin
// CORRECT
class TransactionViewModel(
    private val application: Application // Application context is safe
) : AndroidViewModel(application)
```

### ❌ Mistake 7: Not Cancelling Coroutines
```kotlin
// WRONG
class MyFragment : Fragment() {
    fun loadData() {
        GlobalScope.launch {
            // Coroutine continues even after fragment destroyed
        }
    }
}
```
**Impact:** Memory leaks, crashes when updating destroyed views

**Solution:** Use lifecycle-aware scopes
```kotlin
// CORRECT
class MyFragment : Fragment() {
    fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Automatically cancelled when fragment destroyed
        }
    }
}
```

### ❌ Mistake 8: Observing LiveData Without Lifecycle Owner
```kotlin
// WRONG
viewModel.transactions.observeForever { data ->
    // Never cleaned up, memory leak!
}
```
**Impact:** Memory leaks, observers never removed

**Solution:** Use lifecycle-aware observation
```kotlin
// CORRECT
viewModel.transactions.observe(viewLifecycleOwner) { data ->
    // Automatically cleaned up
}
```

## Security

### ❌ Mistake 9: Storing Passwords in Plain Text
```kotlin
// WRONG
@Entity
data class User(
    val password: String // Never do this!
)
```
**Impact:** Security breach, user data compromised

**Solution:** Always hash passwords
```kotlin
// CORRECT
@Entity
data class User(
    val passwordHash: String // BCrypt hash
)

// Usage
val hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt())
```

### ❌ Mistake 10: Using Regular SharedPreferences for Sensitive Data
```kotlin
// WRONG
sharedPrefs.edit().putString("upi_id", upiId).apply()
```
**Impact:** Data readable by rooted devices, backups

**Solution:** Use EncryptedSharedPreferences
```kotlin
// CORRECT
val encryptedPrefs = EncryptedSharedPreferences.create(
    "secret_prefs",
    masterKey,
    context,
    AES256_SIV,
    AES256_GCM
)
encryptedPrefs.edit().putString("upi_id", upiId).apply()
```

### ❌ Mistake 11: SQL Injection Vulnerability
```kotlin
// WRONG
@Query("SELECT * FROM transactions WHERE userId = $userId")
// Vulnerable to SQL injection!
```
**Impact:** Data breach, database corruption

**Solution:** Use parameterized queries
```kotlin
// CORRECT
@Query("SELECT * FROM transactions WHERE userId = :userId")
fun getTransactions(userId: String): Flow<List<Transaction>>
```

## UI & User Experience

### ❌ Mistake 12: Updating UI from Background Thread
```kotlin
// WRONG
Thread {
    val balance = calculateBalance()
    textView.text = balance.toString() // Crash!
}.start()
```
**Impact:** App crashes with CalledFromWrongThreadException

**Solution:** Update UI on main thread
```kotlin
// CORRECT
viewModelScope.launch {
    val balance = withContext(Dispatchers.IO) {
        calculateBalance()
    }
    _balanceText.value = balance.toString() // LiveData updates on main thread
}
```

### ❌ Mistake 13: Not Handling Configuration Changes
```kotlin
// WRONG
class MainActivity : AppCompatActivity() {
    var userBalance = 0.0 // Lost on rotation!
}
```
**Impact:** Data lost on screen rotation, poor UX

**Solution:** Use ViewModel to survive configuration changes
```kotlin
// CORRECT
class BalanceViewModel : ViewModel() {
    val userBalance = MutableLiveData<Double>()
}
```

### ❌ Mistake 14: Hardcoding Strings in Code
```kotlin
// WRONG
textView.text = "Transaction added successfully"
```
**Impact:** Can't translate app, hard to maintain

**Solution:** Use string resources
```kotlin
// CORRECT
textView.text = getString(R.string.transaction_added_success)

// strings.xml
<string name="transaction_added_success">Transaction added successfully</string>
```

### ❌ Mistake 15: Loading Full RecyclerView at Once
```kotlin
// WRONG
val allTransactions = database.getAllTransactions() // Could be 10,000+
adapter.submitList(allTransactions) // App freezes
```
**Impact:** Memory issues, slow loading, crashes

**Solution:** Implement pagination
```kotlin
// CORRECT
val transactionsPager = Pager(
    PagingConfig(pageSize = 50)
) {
    transactionDao.getTransactionsPaged()
}.flow
```

## Calculations & Logic

### ❌ Mistake 16: Not Handling Time Zones Properly
```kotlin
// WRONG
val timestamp = System.currentTimeMillis() // Local time
// Calendar months calculated incorrectly across timezones
```
**Impact:** Budget periods wrong, reports inaccurate

**Solution:** Always work in UTC, convert for display
```kotlin
// CORRECT
val timestamp = Instant.now().toEpochMilli() // UTC
val displayDate = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(timestamp),
    ZoneId.systemDefault()
)
```

### ❌ Mistake 17: Incorrect Budget Period Calculation
```kotlin
// WRONG
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
// Doesn't account for year boundary (Dec → Jan)
```
**Impact:** Budget resets incorrectly, data corruption

**Solution:** Use proper date comparison
```kotlin
// CORRECT
fun isNewBudgetPeriod(lastPeriodStart: Long, period: BudgetPeriod): Boolean {
    val lastStart = LocalDateTime.ofEpochSecond(lastPeriodStart, 0, ZoneOffset.UTC)
    val now = LocalDateTime.now(ZoneOffset.UTC)
    
    return when(period) {
        MONTHLY -> lastStart.month != now.month || lastStart.year != now.year
        YEARLY -> lastStart.year != now.year
    }
}
```

### ❌ Mistake 18: Race Conditions in Balance Updates
```kotlin
// WRONG
val currentBalance = accountDao.getBalance(accountId)
val newBalance = currentBalance - transactionAmount
accountDao.updateBalance(accountId, newBalance)
// Two transactions at same time = wrong balance!
```
**Impact:** Incorrect balance calculations

**Solution:** Use atomic operations or database transactions
```kotlin
// CORRECT
@Query("UPDATE accounts SET balance = balance - :amount WHERE accountId = :accountId")
suspend fun decreaseBalance(accountId: Long, amount: Double)
```

## Notifications

### ❌ Mistake 19: Not Creating Notification Channels (Android 8+)
```kotlin
// WRONG - Notifications won't show on Android 8+
notificationManager.notify(1, notification)
```
**Impact:** Notifications silently fail on modern Android

**Solution:** Create notification channels
```kotlin
// CORRECT
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        CHANNEL_ID,
        "Transactions",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    notificationManager.createNotificationChannel(channel)
}
```

### ❌ Mistake 20: Showing Sensitive Data in Notifications
```kotlin
// WRONG
val notification = NotificationCompat.Builder(context, CHANNEL_ID)
    .setContentText("Your UPI ID: user@bank") // Visible on lock screen!
```
**Impact:** Privacy breach, sensitive data exposed

**Solution:** Use notification visibility settings
```kotlin
// CORRECT
val notification = NotificationCompat.Builder(context, CHANNEL_ID)
    .setContentText("Payment information updated")
    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
```

## Testing

### ❌ Mistake 21: Not Testing Edge Cases
```kotlin
// WRONG - Only test happy path
transactionAmount = 100.0
addTransaction() // Works fine

// But never test:
// transactionAmount = 0
// transactionAmount = -100
// transactionAmount = 999999999999
```
**Impact:** App crashes in real-world usage

**Solution:** Test edge cases explicitly
```kotlin
@Test
fun `test zero amount transaction`() {
    val result = addTransaction(amount = 0.0)
    assertFalse(result.isSuccess)
}

@Test
fun `test negative amount transaction`() {
    val result = addTransaction(amount = -100.0)
    assertFalse(result.isSuccess)
}
```

## Build & Deployment

### ❌ Mistake 22: Hardcoding API Keys
```kotlin
// WRONG
const val API_KEY = "sk_live_abc123xyz789"
```
**Impact:** Keys exposed in version control, security breach

**Solution:** Use BuildConfig or local.properties
```kotlin
// CORRECT - In build.gradle.kts
buildConfigField("String", "API_KEY", "\"${project.findProperty("apiKey")}\"")

// In local.properties (add to .gitignore)
apiKey=sk_live_abc123xyz789
```

### ❌ Mistake 23: Not Enabling ProGuard/R8
```kotlin
// WRONG - build.gradle
buildTypes {
    release {
        minifyEnabled false // Expose all code!
    }
}
```
**Impact:** Larger APK, reverse engineering easy

**Solution:** Enable code shrinking and obfuscation
```kotlin
// CORRECT
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

## Performance

### ❌ Mistake 24: Loading All Images at Full Resolution
```kotlin
// WRONG
Bitmap.decodeFile(receiptPath) // Load 4000x3000 image for 100x100 thumbnail
```
**Impact:** OutOfMemoryError, app crashes

**Solution:** Use image loading libraries
```kotlin
// CORRECT
Glide.with(context)
    .load(receiptPath)
    .override(100, 100)
    .into(imageView)
```

### ❌ Mistake 25: Not Using RecyclerView ViewHolder Correctly
```kotlin
// WRONG
override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.itemView.findViewById<TextView>(R.id.textView) // Finding every time!
}
```
**Impact:** Laggy scrolling, poor performance

**Solution:** Cache views in ViewHolder
```kotlin
// CORRECT
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.textView) // Find once
}
```

---

## Quick Reference Checklist

Before committing code, verify:
- [ ] No database operations on main thread
- [ ] All financial calculations use proper decimal handling
- [ ] Passwords hashed, sensitive data encrypted
- [ ] LiveData observed with lifecycle owner
- [ ] Coroutines use appropriate scopes
- [ ] String resources used (no hardcoded text)
- [ ] Configuration changes handled
- [ ] Edge cases tested (zero, negative, large values)
- [ ] Notification channels created
- [ ] Images loaded efficiently
- [ ] No Context references in ViewModel
- [ ] Database transactions for multi-step operations
- [ ] ProGuard rules configured
- [ ] No API keys in code

---

**Remember:** It's easier to build it right the first time than to fix it later!
