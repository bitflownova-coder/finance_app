# Android Development Instructions - Finance Management App

## Development Environment Setup

### Required Tools
- **Android Studio:** Latest stable version (Hedgehog or later)
- **JDK:** Version 17 or higher
- **Android SDK:** API 24 (minimum) to API 34 (target)
- **Gradle:** Version 8.0+
- **Kotlin:** Version 1.9+

### Initial Project Setup

1. **Create New Project**
   - Choose "Empty Activity" or "Empty Compose Activity"
   - Package name: `com.yourcompany.financeapp`
   - Language: Kotlin
   - Minimum SDK: API 24 (Android 7.0)
   - Build configuration: Kotlin DSL (build.gradle.kts)

2. **Configure build.gradle.kts (Project level)**
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
}
```

3. **Configure build.gradle.kts (App level)**
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.yourcompany.financeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yourcompany.financeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true // If using Jetpack Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Hilt Dependency Injection
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Charts
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // ML Kit
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.google.mlkit:text-recognition:16.0.0")

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")

    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("org.mindrot:jbcrypt:0.4")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")

    // Lottie Animations
    implementation("com.airbnb.android:lottie:6.2.0")

    // Paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")

    // Export Libraries
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("com.itextpdf:itextpdf:5.5.13.3")

    // Calendar View
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")
}
```

## Project Structure

```
app/src/main/
├── java/com/yourcompany/financeapp/
│   ├── data/
│   │   ├── local/
│   │   │   ├── dao/
│   │   │   │   ├── UserDao.kt
│   │   │   │   ├── BankAccountDao.kt
│   │   │   │   ├── TransactionDao.kt
│   │   │   │   └── BudgetDao.kt
│   │   │   ├── database/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   └── Converters.kt
│   │   │   └── entities/
│   │   │       ├── UserEntity.kt
│   │   │       ├── BankAccountEntity.kt
│   │   │       ├── TransactionEntity.kt
│   │   │       └── BudgetEntity.kt
│   │   ├── repository/
│   │   │   ├── UserRepositoryImpl.kt
│   │   │   ├── BankAccountRepositoryImpl.kt
│   │   │   ├── TransactionRepositoryImpl.kt
│   │   │   └── BudgetRepositoryImpl.kt
│   │   └── mapper/
│   │       ├── UserMapper.kt
│   │       └── TransactionMapper.kt
│   ├── domain/
│   │   ├── model/
│   │   │   ├── User.kt
│   │   │   ├── BankAccount.kt
│   │   │   ├── Transaction.kt
│   │   │   └── Budget.kt
│   │   ├── repository/
│   │   │   ├── UserRepository.kt
│   │   │   └── TransactionRepository.kt
│   │   └── usecase/
│   │       ├── auth/
│   │       │   ├── LoginUseCase.kt
│   │       │   └── RegisterUseCase.kt
│   │       ├── transaction/
│   │       │   ├── AddTransactionUseCase.kt
│   │       │   └── GetTransactionsUseCase.kt
│   │       └── budget/
│   │           └── CalculateBudgetUseCase.kt
│   ├── presentation/
│   │   ├── auth/
│   │   │   ├── LoginFragment.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   └── RegisterFragment.kt
│   │   ├── dashboard/
│   │   │   ├── DashboardFragment.kt
│   │   │   └── DashboardViewModel.kt
│   │   ├── transaction/
│   │   │   ├── TransactionListFragment.kt
│   │   │   ├── AddTransactionDialog.kt
│   │   │   └── TransactionViewModel.kt
│   │   ├── profile/
│   │   │   ├── ProfileFragment.kt
│   │   │   └── ProfileViewModel.kt
│   │   ├── budget/
│   │   │   ├── BudgetFragment.kt
│   │   │   └── BudgetViewModel.kt
│   │   └── common/
│   │       ├── BaseFragment.kt
│   │       └── adapters/
│   │           └── TransactionAdapter.kt
│   ├── di/
│   │   ├── AppModule.kt
│   │   ├── DatabaseModule.kt
│   │   └── RepositoryModule.kt
│   └── utils/
│       ├── Constants.kt
│       ├── Extensions.kt
│       ├── SecurityUtils.kt
│       └── NotificationHelper.kt
└── res/
    ├── layout/
    ├── values/
    ├── navigation/
    └── drawable/
```

## Key Implementation Guidelines

### 1. Database Setup

**AppDatabase.kt**
```kotlin
@Database(
    entities = [
        UserEntity::class,
        BankAccountEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
}
```

### 2. Hilt Setup

**Application Class**
```kotlin
@HiltAndroidApp
class FinanceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize components
        createNotificationChannels()
    }
}
```

**Don't forget to add in AndroidManifest.xml:**
```xml
<application
    android:name=".FinanceApp"
    ...>
```

### 3. Navigation Setup

Create `nav_graph.xml` in `res/navigation/`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/splashFragment">

    <fragment
        android:id="@+id/splashFragment"
        android:name="com.yourcompany.financeapp.presentation.SplashFragment" />
    
    <fragment
        android:id="@+id/loginFragment"
        android:name="com.yourcompany.financeapp.presentation.auth.LoginFragment" />
    
    <fragment
        android:id="@+id/dashboardFragment"
        android:name="com.yourcompany.financeapp.presentation.dashboard.DashboardFragment" />
</navigation>
```

### 4. Permissions (AndroidManifest.xml)

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />

<uses-feature android:name="android.hardware.camera" android:required="false" />
```

### 5. ProGuard Rules (proguard-rules.pro)

```proguard
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule
-keepnames class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }

# Your data classes
-keep class com.yourcompany.financeapp.data.local.entities.** { *; }
-keep class com.yourcompany.financeapp.domain.model.** { *; }
```

## Development Workflow

### Phase-by-Phase Implementation

**Phase 1: Foundation**
1. Set up project with all dependencies
2. Create package structure
3. Set up Hilt modules
4. Create database entities and DAOs
5. Implement repositories

**Phase 2: Authentication**
1. Create User entity and DAO
2. Build login/register UI
3. Implement password hashing
4. Session management

**Phase 3: Core Features**
1. Implement account management
2. Build transaction system
3. Create dashboard
4. Add budget tracking

**Phase 4: Advanced Features**
1. Add notifications
2. Implement search/filter
3. Add charts and analytics
4. Polish UI

### Testing Strategy

**Unit Tests (test/)**
```kotlin
@RunWith(MockitoJUnitRunner::class)
class TransactionViewModelTest {
    @Mock
    lateinit var repository: TransactionRepository
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @Test
    fun `addTransaction should update balance`() = runTest {
        // Test implementation
    }
}
```

**Instrumentation Tests (androidTest/)**
```kotlin
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: AppDatabase
    private lateinit var transactionDao: TransactionDao
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        transactionDao = database.transactionDao()
    }
    
    @Test
    fun insertAndRetrieveTransaction() = runTest {
        // Test implementation
    }
}
```

## Common Tasks

### Running the App
- **Debug:** Click "Run" (Shift+F10) or use emulator
- **Release:** Build → Generate Signed Bundle/APK

### Debugging
- Use Logcat for logs: `Log.d("TAG", "Message")`
- Set breakpoints in code
- Use Android Profiler for performance

### Database Inspection
- Use Database Inspector in Android Studio
- View tables, run queries
- Export database for analysis

### Emulator Setup
- Create AVD with API 24-34
- Enable hardware acceleration
- Test different screen sizes

## Best Practices

1. **Always use ViewBinding** - No more findViewById()
2. **Never block main thread** - Use coroutines for I/O
3. **Test on real devices** - Emulator doesn't catch all issues
4. **Use vector drawables** - Better for different screen densities
5. **Follow Material Design** - Consistent with Android ecosystem
6. **Handle configuration changes** - Test with screen rotation
7. **Optimize for battery** - Use WorkManager, not AlarmManager
8. **Secure sensitive data** - Encrypt everything financial
9. **Accessibility matters** - Add content descriptions
10. **Version control** - Commit often with clear messages

## Troubleshooting

### Common Issues

**Build Failures:**
- Clean project: Build → Clean Project
- Invalidate caches: File → Invalidate Caches / Restart
- Update Gradle: Check for updates

**Database Issues:**
- Uninstall app to reset database
- Check migrations are correct
- Use fallbackToDestructiveMigration() in dev

**Hilt Issues:**
- Ensure all classes are properly annotated
- Check module installation
- Rebuild project

**Performance Issues:**
- Use Android Profiler
- Check for memory leaks with LeakCanary
- Optimize database queries with EXPLAIN QUERY PLAN

## Resources

- **Official Docs:** developer.android.com
- **Kotlin Docs:** kotlinlang.org
- **Material Design:** material.io
- **Stack Overflow:** Questions tagged [android][kotlin]
- **GitHub:** Search for similar open-source projects

---

**Remember:** Write clean, maintainable code. Your future self will thank you!
