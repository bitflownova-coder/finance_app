# Security Documentation - Finance Management Application

## 1. Security Overview

### 1.1 Security Principles

This financial application adheres to the following security principles:

1. **Defense in Depth:** Multiple layers of security controls
2. **Least Privilege:** Minimal permissions requested
3. **Fail Securely:** Graceful handling of security failures
4. **Privacy by Design:** User data protected by default
5. **Encryption Everywhere:** All sensitive data encrypted

### 1.2 Threat Model

**Assets to Protect:**
- User credentials (email, password)
- Financial data (transactions, balances)
- Personal identifiers (UPI IDs, account numbers)
- Receipt photos

**Potential Threats:**
- Unauthorized access to app
- Data theft from device
- Man-in-the-middle attacks (if network added)
- Malware on rooted devices
- Physical device theft
- Backup data exposure

---

## 2. Authentication & Authorization

### 2.1 Password Security

**Implementation:**
```kotlin
object PasswordSecurity {
    private const val BCRYPT_ROUNDS = 12
    
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS))
    }
    
    fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }
}
```

**Requirements:**
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one number
- At least one special character (@, #, $, %, etc.)

**Storage:**
- Passwords hashed using BCrypt with 12 rounds
- Salt automatically generated per password
- Never stored in plain text anywhere
- Hash stored in encrypted database

### 2.2 Session Management

**Implementation:**
```kotlin
object SessionManager {
    private const val SESSION_TIMEOUT_MS = 15 * 60 * 1000L // 15 minutes
    private const val KEY_USER_ID = "encrypted_user_id"
    private const val KEY_SESSION_START = "session_start_time"
    
    fun createSession(context: Context, userId: String) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit()
            .putString(KEY_USER_ID, userId)
            .putLong(KEY_SESSION_START, System.currentTimeMillis())
            .apply()
    }
    
    fun isSessionValid(context: Context): Boolean {
        val prefs = getEncryptedPrefs(context)
        val startTime = prefs.getLong(KEY_SESSION_START, 0)
        val elapsed = System.currentTimeMillis() - startTime
        return elapsed < SESSION_TIMEOUT_MS
    }
    
    fun endSession(context: Context) {
        getEncryptedPrefs(context).edit().clear().apply()
    }
}
```

**Security Measures:**
- Sessions expire after 15 minutes of inactivity
- Session tokens stored in EncryptedSharedPreferences
- Automatic logout on app background (optional)
- Biometric re-authentication for sensitive operations

### 2.3 Biometric Authentication

**Implementation:**
```kotlin
class BiometricAuth(private val activity: FragmentActivity) {
    
    fun authenticate(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate to Access Finance App")
            .setNegativeButtonText("Use Password")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
        
        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    }
}
```

**Security Measures:**
- Uses Android Keystore for secure key storage
- BIOMETRIC_STRONG requirement (fingerprint, face, iris)
- Fallback to password authentication
- Rate limiting on failed attempts

---

## 3. Data Encryption

### 3.1 Database Encryption

**Implementation: SQLCipher**

```kotlin
@Provides
@Singleton
fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
    val passphrase = getDatabasePassphrase(context)
    val factory = SupportFactory(passphrase)
    
    return Room.databaseBuilder(context, AppDatabase::class.java, "finance.db")
        .openHelperFactory(factory)
        .build()
}

private fun getDatabasePassphrase(context: Context): ByteArray {
    val prefs = SessionManager.getEncryptedPrefs(context)
    var passphrase = prefs.getString("db_key", null)
    
    if (passphrase == null) {
        // Generate 256-bit encryption key
        passphrase = ByteArray(32).apply {
            SecureRandom().nextBytes(this)
        }.encodeBase64()
        prefs.edit().putString("db_key", passphrase).apply()
    }
    
    return passphrase.decodeBase64()
}
```

**Encryption Details:**
- Algorithm: AES-256-CBC (via SQLCipher)
- Key: 256-bit randomly generated
- Key storage: EncryptedSharedPreferences (backed by Android Keystore)
- Every database page encrypted

### 3.2 Field-Level Encryption

**For Extra Sensitive Fields (UPI ID, Account Numbers):**

```kotlin
object FieldEncryption {
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEY_ALIAS = "finance_field_key"
    
    fun encrypt(plaintext: String): String {
        val key = getOrCreateKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        
        // Prepend IV to ciphertext
        return (iv + encrypted).encodeBase64()
    }
    
    fun decrypt(ciphertext: String): String {
        val key = getOrCreateKey()
        val data = ciphertext.decodeBase64()
        
        val iv = data.take(12).toByteArray()
        val encrypted = data.drop(12).toByteArray()
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        
        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }
    
    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(false)
                .build()
            )
            keyGenerator.generateKey()
        }
        
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }
}
```

**Encryption Details:**
- Algorithm: AES-256-GCM
- Mode: Galois/Counter Mode (authenticated encryption)
- Key storage: Android Keystore (hardware-backed if available)
- Unique IV per encryption operation

### 3.3 Secure Storage

**EncryptedSharedPreferences:**

```kotlin
fun getEncryptedPrefs(context: Context): SharedPreferences {
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
```

**What to Store:**
- Session tokens
- Database encryption key
- User preferences (sensitive)
- Biometric enrollment status

**What NOT to Store:**
- Plain text passwords
- Unencrypted financial data
- API keys (use BuildConfig instead)

---

## 4. Input Validation & Sanitization

### 4.1 Transaction Input Validation

```kotlin
object InputValidator {
    
    fun validateAmount(input: String): Result<Double> {
        if (input.isBlank()) {
            return Result.Failure("Amount cannot be empty")
        }
        
        val amount = input.toDoubleOrNull()
            ?: return Result.Failure("Invalid amount format")
        
        return when {
            amount <= 0 -> Result.Failure("Amount must be positive")
            amount > 10_00_000 -> Result.Failure("Amount exceeds limit")
            else -> Result.Success(amount)
        }
    }
    
    fun sanitizeDescription(input: String): String {
        return input
            .replace(Regex("<[^>]*>"), "") // Remove HTML
            .replace(Regex("[';\"\\\\]"), "") // Remove SQL chars
            .trim()
            .take(200) // Limit length
    }
    
    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
```

### 4.2 SQL Injection Prevention

**✅ Always use parameterized queries:**

```kotlin
@Query("SELECT * FROM transactions WHERE userId = :userId AND category = :category")
fun getTransactions(userId: String, category: String): Flow<List<TransactionEntity>>
```

**❌ Never build queries with string concatenation:**

```kotlin
// DANGEROUS - SQL injection vulnerable
val query = "SELECT * FROM users WHERE email = '$userInput'"
```

---

## 5. Network Security (Future)

### 5.1 SSL/TLS

**If backend is added:**

```kotlin
val certificatePinner = CertificatePinner.Builder()
    .add("api.financeapp.com", "sha256/AAAAAAA...")
    .build()

val okHttpClient = OkHttpClient.Builder()
    .certificatePinner(certificatePinner)
    .build()
```

### 5.2 API Key Protection

**Never hardcode API keys:**

```kotlin
// In build.gradle.kts
android {
    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"${project.findProperty("apiKey")}\"")
        }
    }
}

// In local.properties (add to .gitignore)
apiKey=your_api_key_here

// In code
val apiKey = BuildConfig.API_KEY
```

---

## 6. App Security Features

### 6.1 Screenshot Protection

```kotlin
class SensitiveFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
    
    override fun onPause() {
        super.onPause()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}
```

### 6.2 Root Detection

```kotlin
object SecurityCheck {
    
    fun isDeviceRooted(): Boolean {
        return checkBuildTags() || checkSuperUser() || checkSuBinary() || checkDangerousProps()
    }
    
    private fun checkBuildTags(): Boolean {
        return Build.TAGS?.contains("test-keys") == true
    }
    
    private fun checkSuperUser(): Boolean {
        return File("/system/app/Superuser.apk").exists()
    }
    
    private fun checkSuBinary(): Boolean {
        val paths = arrayOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su",
            "/data/local/xbin/su", "/data/local/bin/su"
        )
        return paths.any { File(it).exists() }
    }
    
    fun showSecurityWarning(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Security Risk Detected")
            .setMessage("Your device appears to be rooted. This may compromise data security.")
            .setPositiveButton("I Understand") { d, _ -> d.dismiss() }
            .setCancelable(false)
            .show()
    }
}
```

### 6.3 Backup Security

**AndroidManifest.xml:**

```xml
<application
    android:allowBackup="false"
    android:fullBackupContent="@xml/backup_rules"
    ...>
```

**res/xml/backup_rules.xml:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<full-backup-content>
    <exclude domain="sharedpref" path="secure_prefs.xml"/>
    <exclude domain="database" path="finance.db"/>
</full-backup-content>
```

---

## 7. Secure Coding Practices

### 7.1 Logging Security

**✅ DO:**
```kotlin
if (BuildConfig.DEBUG) {
    Log.d(TAG, "Transaction count: ${transactions.size}")
}
```

**❌ DON'T:**
```kotlin
Log.d(TAG, "User password: $password") // NEVER!
Log.d(TAG, "Balance: $balance") // Avoid in production
```

### 7.2 Memory Security

**Clear sensitive data from memory:**

```kotlin
fun secureLogin(password: CharArray) {
    try {
        val hash = hashPassword(String(password))
        // Use hash
    } finally {
        password.fill('0') // Clear password from memory
    }
}
```

### 7.3 ProGuard/R8 Rules

**proguard-rules.pro:**

```proguard
# Keep database models
-keep class com.yourapp.data.local.entities.** { *; }

# Keep BCrypt
-keep class org.mindrot.jbcrypt.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
```

---

## 8. Security Testing

### 8.1 Security Checklist

- [ ] All passwords hashed with BCrypt (12+ rounds)
- [ ] Database encrypted with SQLCipher
- [ ] Sensitive fields encrypted (UPI, account numbers)
- [ ] EncryptedSharedPreferences for session data
- [ ] No hardcoded secrets or API keys
- [ ] Input validation on all user inputs
- [ ] SQL injection prevention verified
- [ ] ProGuard enabled for release builds
- [ ] Root detection implemented
- [ ] Screenshot protection on sensitive screens
- [ ] Backup exclusions configured
- [ ] Session timeout implemented
- [ ] Biometric authentication (optional)
- [ ] Secure logging (no sensitive data in logs)
- [ ] Memory cleared after using sensitive data

### 8.2 Security Audit Steps

1. **Static Analysis:**
   - Run Android Lint security checks
   - Use OWASP dependency checker
   - Review ProGuard output

2. **Dynamic Analysis:**
   - Test on rooted device
   - Attempt SQL injection
   - Try various input attacks
   - Test session hijacking

3. **Penetration Testing:**
   - Decompile APK and review
   - Inspect local storage
   - Analyze network traffic (if applicable)
   - Test authentication bypass

---

## 9. Incident Response Plan

### 9.1 Security Breach Protocol

**Immediate Actions (0-24 hours):**
1. Identify and contain the breach
2. Revoke all active sessions
3. Document the incident
4. Assess scope of data exposure

**Investigation (24-72 hours):**
1. Analyze attack vector
2. Identify affected users
3. Determine data compromised
4. Preserve evidence

**User Notification (within 72 hours):**
1. Inform affected users
2. Provide clear guidance
3. Offer remediation steps
4. Report to authorities if required

**Remediation:**
1. Patch vulnerability
2. Force password resets
3. Update security measures
4. Release security update

**Post-Incident:**
1. Document lessons learned
2. Update security procedures
3. Conduct team training
4. Schedule security audit

---

## 10. Compliance & Privacy

### 10.1 Data Protection

**GDPR Compliance (if EU users):**
- Right to access data (export functionality)
- Right to erasure (account deletion)
- Right to portability (CSV/PDF export)
- Privacy by design (encryption by default)

### 10.2 Privacy Policy Requirements

**Must Include:**
- What data is collected
- How data is used
- How data is stored and protected
- User rights regarding their data
- Contact information for privacy concerns

---

## 11. Security Resources

**Guidelines & Standards:**
- OWASP Mobile Top 10: https://owasp.org/www-project-mobile-top-10/
- Android Security: https://developer.android.com/topic/security
- NIST Cryptography: https://www.nist.gov/cryptography

**Tools:**
- MobSF (Mobile Security Framework)
- AndroBugs Framework
- QARK (Quick Android Review Kit)

**Libraries:**
- Tink (Google's crypto library)
- Conscrypt (Modern crypto provider)

---

**Security Contact:** security@yourapp.com  
**Last Security Audit:** [Date]  
**Next Scheduled Audit:** [Date]  
**Document Version:** 1.0
