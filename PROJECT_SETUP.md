# Finance Manager Android App

## Project Setup Complete! ✅

### Phase 0: Foundation - COMPLETED

The following components have been created:

#### 1. **Gradle Configuration**
- ✅ Root `build.gradle.kts` with plugin versions
- ✅ App `build.gradle.kts` with all dependencies (Room, Hilt, SQLCipher, BCrypt, Charts, etc.)
- ✅ `settings.gradle.kts` with repository configuration

#### 2. **Database Layer**
- ✅ 4 Entity classes: `UserEntity`, `BankAccountEntity`, `TransactionEntity`, `BudgetEntity`
- ✅ 4 DAO interfaces: `UserDao`, `BankAccountDao`, `TransactionDao`, `BudgetDao`
- ✅ `AppDatabase` with Room configuration
- ✅ `TypeConverters` for date/time conversion
- ✅ SQLCipher encryption enabled

#### 3. **Domain Layer**
- ✅ Domain models: `User`, `BankAccount`, `Transaction`, `Budget`
- ✅ Enums: `AccountType`, `TransactionType`, `TransactionCategory`, `BudgetPeriodType`
- ✅ Data mappers between entities and domain models

#### 4. **Dependency Injection (Hilt)**
- ✅ `FinanceManagerApp` with Hilt setup
- ✅ `DatabaseModule` providing database and DAOs
- ✅ `AppModule` with EncryptedSharedPreferences and Coroutine Dispatchers
- ✅ Notification channels setup

#### 5. **UI Foundation**
- ✅ `MainActivity` with Navigation Component
- ✅ Material Design 3 theme (light + dark mode)
- ✅ Navigation graph structure
- ✅ Bottom navigation menu
- ✅ Resources: strings, colors, dimens, themes

#### 6. **Security**
- ✅ `SecurityUtils` with BCrypt password hashing (12 rounds)
- ✅ Password validation (8+ chars, uppercase, lowercase, digit, special char)
- ✅ Email validation
- ✅ EncryptedSharedPreferences for session storage
- ✅ SQLCipher for database encryption

### Architecture: MVVM + Clean Architecture

```
app/
├── data/
│   ├── local/
│   │   ├── dao/         # Database access objects
│   │   ├── database/    # Room database
│   │   └── entities/    # Database entities
│   └── mapper/          # Entity to domain mappers
├── domain/
│   └── model/           # Domain models & business logic
├── di/                  # Hilt dependency injection modules
├── presentation/        # UI layer (ViewModels, Fragments)
└── util/                # Utility classes
```

### Next Steps: Phase 1 - Authentication

To continue building the app, you need to implement:

1. **UserRepository** (interface + implementation)
2. **Use Cases**: LoginUseCase, RegisterUseCase, LogoutUseCase
3. **ViewModels**: LoginViewModel, RegisterViewModel
4. **Fragments**: SplashFragment, LoginFragment, RegisterFragment
5. **Layouts**: Login and Register screens
6. **Session Management**: Store logged-in user

### How to Run

1. Open project in **Android Studio Hedgehog (2023.1.1)** or newer
2. Sync Gradle files
3. Build and run on an emulator or device (API 24+)

### Dependencies Included

- **Room**: 2.6.1 (with SQLCipher encryption)
- **Hilt**: 2.48
- **Navigation Component**: 2.7.6
- **BCrypt**: 0.10.2
- **MPAndroidChart**: 3.1.0
- **Lottie**: 6.2.0
- **WorkManager**: 2.9.0
- **Material Design 3**

### Database Encryption

The app uses **SQLCipher** to encrypt the entire database with AES-256 encryption. The passphrase is hardcoded for development; for production, use Android Keystore to securely generate and store the key.

---

**Status**: Phase 0 Complete ✅  
**Next**: Phase 1 - Authentication System 🔐
