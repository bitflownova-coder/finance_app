# Security Instructions - Finance Management App

## Critical Security Requirements

> **⚠️ IMPORTANT:** This is a financial application handling sensitive user data. Security is not optional—it's mandatory.

## Authentication Security

### Password Management

**✅ DO:**
```kotlin
// Hash passwords with BCrypt (12 rounds minimum)
fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt(12))
}

// Verify passwords securely
fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(plainPassword, hashedPassword)
}
```

**❌ DON'T:**
```kotlin
// NEVER store plain text passwords
@Entity
data class User(
    val password: String // NO!
)

// NEVER use weak hashing
val hash = password.md5() // NO!
val hash = password.sha256() // Still NO!
```

### Password Requirements

Enforce strong passwords:
```kotlin
object PasswordValidator {
    private const val MIN_LENGTH = 8
    private val PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()
    
    fun isValid(password: String): Boolean {
        return password.length >= MIN_LENGTH && PASSWORD_PATTERN.matches(password)
    }
    
    fun getRequirements(): String {
        return """
            Password must:
            - Be at least 8 characters
            - Contain uppercase letter
            - Contain lowercase letter
            - Contain a number
            - Contain a special character
        """.trimIndent()
    }
}
```

### Session Management

**Secure Session Storage:**
```kotlin
object SessionManager {
    private const val PREF_NAME = "secure_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_SESSION_TOKEN = "session_token"
    private const val KEY_LOGIN_TIME = "login_time"
    private const val SESSION_TIMEOUT = 15 * 60 * 1000L // 15 minutes
    
    fun getEncryptedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    fun isSessionValid(context: Context): Boolean {
        val prefs = getEncryptedPrefs(context)
        val loginTime = prefs.getLong(KEY_LOGIN_TIME, 0)
        val currentTime = System.currentTimeMillis()
        
        return (currentTime - loginTime) < SESSION_TIMEOUT
    }
    
    fun logout(context: Context) {
        getEncryptedPrefs(context).edit().clear().apply()
    }
}
```

### Biometric Authentication

**Implementation:**
```kotlin
class BiometricHelper(private val activity: FragmentActivity) {
    
    fun authenticate(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    onSuccess()
                }
                
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    onError(errString.toString())
                }
                
                override fun onAuthenticationFailed() {
                    onError("Authentication failed")
                }
            }
        )
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Finance App")
            .setSubtitle("Use your fingerprint or face")
            .setNegativeButtonText("Use Password")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(activity)
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
}
```

## Data Encryption

### Database Encryption

**Using SQLCipher:**
```kotlin
// Add dependency
// implementation("net.zetetic:android-database-sqlcipher:4.5.4")

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        val passphrase = getOrCreateDatabasePassphrase(context)
        val factory = SupportFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finance_database"
        )
        .openHelperFactory(factory)
        .build()
    }
    
    private fun getOrCreateDatabasePassphrase(context: Context): ByteArray {
        val prefs = SessionManager.getEncryptedPrefs(context)
        var passphrase = prefs.getString("db_passphrase", null)
        
        if (passphrase == null) {
            passphrase = generateSecureRandomString(64)
            prefs.edit().putString("db_passphrase", passphrase).apply()
        }
        
        return passphrase.toByteArray()
    }
}
```

### Field-Level Encryption

**Encrypt Sensitive Fields:**
```kotlin
object FieldEncryption {
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    
    fun encrypt(plaintext: String, context: Context): String {
        val cipher = getCipher()
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(context))
        
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plaintext.toByteArray())
        
        // Combine IV and encrypted data
        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }
    
    fun decrypt(ciphertext: String, context: Context): String {
        val combined = Base64.decode(ciphertext, Base64.NO_WRAP)
        val iv = combined.copyOfRange(0, 12) // GCM IV is 12 bytes
        val encryptedBytes = combined.copyOfRange(12, combined.size)
        
        val cipher = getCipher()
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context), GCMParameterSpec(128, iv))
        
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
    
    private fun getSecretKey(context: Context): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        
        if (!keyStore.containsAlias("finance_key")) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                "finance_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
        
        return keyStore.getKey("finance_key", null) as SecretKey
    }
    
    private fun getCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION)
    }
}
```

**Usage in Entity:**
```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val passwordHash: String,
    val upiIdEncrypted: String? // Store encrypted
) {
    // Helper methods
    fun setUpiId(upiId: String, context: Context) {
        upiIdEncrypted = FieldEncryption.encrypt(upiId, context)
    }
    
    fun getUpiId(context: Context): String? {
        return upiIdEncrypted?.let { FieldEncryption.decrypt(it, context) }
    }
}
```

## Network Security (If Backend Added)

### SSL Pinning

**Implementation:**
```kotlin
object NetworkModule {
    
    fun provideOkHttpClient(): OkHttpClient {
        val certificatePinner = CertificatePinner.Builder()
            .add("yourdomain.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()
        
        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    }
}
```

### Secure API Communication

**Never store API keys in code:**
```kotlin
// ❌ WRONG
const val API_KEY = "sk_live_1234567890abcdef"

// ✅ CORRECT - Use BuildConfig
// In build.gradle.kts:
android {
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${project.findProperty("apiKey")}\"")
    }
}

// In local.properties (add to .gitignore):
apiKey=sk_live_1234567890abcdef

// In code:
val apiKey = BuildConfig.API_KEY
```

## Input Validation & Sanitization

### Transaction Input Validation

```kotlin
object TransactionValidator {
    
    fun validateAmount(amount: String): ValidationResult {
        if (amount.isBlank()) {
            return ValidationResult.Error("Amount cannot be empty")
        }
        
        val numAmount = amount.toDoubleOrNull()
            ?: return ValidationResult.Error("Invalid amount format")
        
        if (numAmount <= 0) {
            return ValidationResult.Error("Amount must be greater than zero")
        }
        
        if (numAmount > 10_00_000) { // 10 lakhs limit
            return ValidationResult.Error("Amount exceeds maximum limit")
        }
        
        return ValidationResult.Success(numAmount)
    }
    
    fun sanitizeDescription(description: String): String {
        // Remove HTML/SQL injection attempts
        return description
            .replace(Regex("<[^>]*>"), "") // Remove HTML tags
            .replace(Regex("[';\"\\\\]"), "") // Remove SQL special chars
            .trim()
            .take(200) // Limit length
    }
    
    fun validateCategory(category: String, validCategories: List<String>): Boolean {
        return validCategories.contains(category)
    }
}

sealed class ValidationResult {
    data class Success(val value: Double) : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
```

### SQL Injection Prevention

**✅ Always use parameterized queries:**
```kotlin
@Query("SELECT * FROM transactions WHERE userId = :userId AND category = :category")
fun getTransactionsByCategory(userId: String, category: String): Flow<List<TransactionEntity>>
```

**❌ Never concatenate SQL:**
```kotlin
// DANGEROUS!
fun getRawQuery(userId: String): List<Transaction> {
    return database.rawQuery("SELECT * FROM transactions WHERE userId = '$userId'")
}
```

## Notification Security

### Sensitive Information in Notifications

```kotlin
object NotificationHelper {
    
    fun showTransactionNotification(
        context: Context,
        amount: Double,
        remainingBalance: Double
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Transaction Added")
            .setContentText("Balance updated") // Don't show amount on lock screen
            .setSmallIcon(R.drawable.ic_notification)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE) // Hide on lock screen
            .setPublicVersion(
                // This shows on lock screen
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Finance App")
                    .setContentText("New transaction recorded")
                    .setSmallIcon(R.drawable.ic_notification)
                    .build()
            )
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}
```

## Screenshot Protection

**Protect sensitive screens:**
```kotlin
class TransactionDetailFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Prevent screenshots on this screen
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // Remove flag when leaving screen
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}
```

## Root Detection

**Warn users on rooted devices:**
```kotlin
object RootDetection {
    
    fun isDeviceRooted(): Boolean {
        return checkBuildTags() || checkSuperuserApk() || checkSuBinary()
    }
    
    private fun checkBuildTags(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }
    
    private fun checkSuperuserApk(): Boolean {
        return try {
            File("/system/app/Superuser.apk").exists()
        } catch (e: Exception) {
            false
        }
    }
    
    private fun checkSuBinary(): Boolean {
        val paths = arrayOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su"
        )
        return paths.any { File(it).exists() }
    }
    
    fun showRootWarning(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Security Warning")
            .setMessage("Your device appears to be rooted. This may compromise the security of your financial data.")
            .setPositiveButton("I Understand") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}
```

## Data Backup Security

**AndroidManifest.xml:**
```xml
<application
    android:allowBackup="false"
    android:fullBackupContent="false"
    android:dataExtractionRules="@xml/data_extraction_rules"
    ...>
```

**res/xml/data_extraction_rules.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<data-extraction-rules>
    <cloud-backup>
        <exclude domain="sharedpref" path="secure_session" />
        <exclude domain="database" path="finance_database" />
    </cloud-backup>
</data-extraction-rules>
```

## Logging Security

**✅ Safe Logging:**
```kotlin
// Use different log levels for dev/prod
object SecureLog {
    private const val TAG = "FinanceApp"
    
    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
    
    fun e(message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message, throwable)
        } else {
            // Send to crash reporting (without sensitive data)
            FirebaseCrashlytics.getInstance().recordException(
                throwable ?: Exception(message)
            )
        }
    }
}
```

**❌ Never Log Sensitive Data:**
```kotlin
// WRONG!
Log.d("Auth", "Password: $password")
Log.d("Transaction", "UPI: $upiId")
Log.d("Account", "Balance: $balance")
```

## Security Checklist

### Before Each Release

- [ ] All passwords hashed with BCrypt (12+ rounds)
- [ ] Database encrypted with SQLCipher
- [ ] Sensitive fields encrypted (UPI ID, etc.)
- [ ] EncryptedSharedPreferences used for session data
- [ ] No API keys hardcoded in source
- [ ] ProGuard/R8 enabled and tested
- [ ] SSL pinning implemented (if using network)
- [ ] Input validation on all user inputs
- [ ] SQL injection prevention verified
- [ ] Notification visibility set to PRIVATE
- [ ] Screenshot protection on sensitive screens
- [ ] Root detection implemented and tested
- [ ] Backup disabled or properly secured
- [ ] No sensitive data in logs (production builds)
- [ ] Session timeout implemented (< 15 minutes)
- [ ] Biometric authentication optional
- [ ] Auto-logout on app backgrounding
- [ ] Security audit completed
- [ ] Penetration testing performed

### Code Review Security Focus

- Check for hardcoded secrets
- Verify encryption implementation
- Review database queries for injection
- Validate input sanitization
- Check permission usage
- Review network communication
- Verify data storage locations
- Check for memory leaks with sensitive data

## Incident Response Plan

### If Security Breach Detected

1. **Immediate Actions:**
   - Revoke all active sessions
   - Force password resets
   - Disable compromised features
   - Document the incident

2. **Investigation:**
   - Identify scope of breach
   - Determine affected users
   - Analyze attack vector
   - Preserve evidence

3. **Notification:**
   - Inform affected users within 24 hours
   - Provide clear guidance on actions to take
   - Report to authorities if required
   - Update privacy policy if needed

4. **Remediation:**
   - Patch vulnerability
   - Review related code
   - Update security measures
   - Schedule security audit

5. **Post-Incident:**
   - Document lessons learned
   - Update security procedures
   - Conduct team training
   - Monitor for similar attempts

## Security Resources

- **OWASP Mobile Top 10:** https://owasp.org/www-project-mobile-top-10/
- **Android Security Guidelines:** https://developer.android.com/topic/security
- **Kotlin Crypto:** https://developer.android.com/guide/topics/security/cryptography
- **NIST Guidelines:** https://www.nist.gov/cybersecurity

---

**Remember:** Security is not a feature—it's a requirement. Never compromise on security for convenience!
