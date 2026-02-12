# Finance Management Application

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/about/versions/nougat)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34-brightgreen.svg)](https://developer.android.com/about/versions/14)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive Android application for personal finance management that helps users track income, expenses, budgets, and savings goals with smart insights and analytics.

![App Banner](docs/images/banner.png) <!-- Add your app banner -->

---

## ✨ Features

### Core Features
- 🔐 **Secure Authentication** - Login/Register with password hashing and biometric support
- 💰 **Multi-Account Management** - Track multiple bank accounts, wallets, and credit cards
- 📊 **Transaction Tracking** - Add, edit, delete transactions with categories
- 🎯 **Budget Management** - Set monthly/yearly budgets and track spending
- 📈 **Analytics & Charts** - Visual insights into spending patterns
- 🔔 **Smart Notifications** - Balance updates and budget alerts
- 🔍 **Search & Filter** - Powerful search with advanced filters

### Advanced Features
- 🔁 **Recurring Transactions** - Auto-add subscriptions and regular expenses
- 🧠 **Smart Insights** - AI-powered spending analysis and predictions
- 💾 **Export Reports** - Generate CSV, Excel, and PDF reports
- 🎯 **Savings Goals** - Track progress toward financial goals
- 📸 **Receipt Management** - Attach photos with OCR support
- 👥 **Bill Splitting** - Split expenses with friends
- 📅 **Calendar View** - Visualize transactions on calendar

---

## 📱 Screenshots

| Dashboard | Transactions | Analytics | Budget |
|-----------|--------------|-----------|--------|
| ![Dashboard](docs/images/screenshot1.png) | ![Transactions](docs/images/screenshot2.png) | ![Analytics](docs/images/screenshot3.png) | ![Budget](docs/images/screenshot4.png) |

---

## 🏗️ Architecture

### Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture
- **Database:** Room (SQLite) with SQLCipher encryption
- **DI:** Hilt (Dagger)
- **Async:** Kotlin Coroutines + Flow
- **UI:** XML Layouts with ViewBinding
- **Charts:** MPAndroidChart
- **ML:** TensorFlow Lite + ML Kit

### Project Structure
```
app/
├── data/                  # Data layer
│   ├── local/            # Database (Room)
│   │   ├── dao/          # Data Access Objects
│   │   ├── entities/     # Database entities
│   │   └── database/     # Database setup
│   ├── repository/       # Repository implementations
│   └── mapper/           # Data mappers
├── domain/               # Domain layer
│   ├── model/            # Business models
│   ├── repository/       # Repository interfaces
│   └── usecase/          # Business logic
├── presentation/         # Presentation layer
│   ├── auth/             # Authentication screens
│   ├── dashboard/        # Dashboard screen
│   ├── transaction/      # Transaction screens
│   ├── profile/          # Profile screen
│   ├── budget/           # Budget screen
│   └── common/           # Shared components
├── di/                   # Dependency injection
└── utils/                # Utility classes
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or higher
- Android SDK API 24 (Android 7.0) minimum
- Gradle 8.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/finance-management-app.git
   cd finance-management-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (Shift + F10)

---

## 🔧 Configuration

### Build Variants
- **Debug:** Development build with logging enabled
- **Release:** Production build with ProGuard enabled

### API Keys (if applicable)
Create `local.properties` file in root directory:
```properties
apiKey=your_api_key_here
```

### Database
The app uses encrypted SQLite database (SQLCipher). No additional configuration needed.

---

## 📖 Documentation

- **[Product Requirements Document (PRD)](docs/PRD.md)** - Complete feature specifications
- **[Design Document](docs/DESIGN.md)** - Architecture and design decisions
- **[Security Documentation](docs/SECURITY.md)** - Security implementation details
- **[TODO](docs/TODO.md)** - Development roadmap and tasks
- **[Deployment Checklist](docs/DEPLOYMENT_CHECKLIST.md)** - Pre-release checklist

### Developer Instructions
- **[Android Development Guide](.github/instructions/android.instructions.md)**
- **[Security Guidelines](.github/instructions/security.instructions.md)**
- **[Common Mistakes](.github/COMMON_MISTAKES.md)**
- **[Code Review Checklist](.github/AI_CODE_REVIEW_CHECKLIST.md)**

---

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
```bash
./gradlew jacocoTestReport
```

---

## 🔒 Security

This app implements multiple layers of security:

- **Password Hashing:** BCrypt with 12 rounds
- **Database Encryption:** SQLCipher (AES-256)
- **Field Encryption:** Android Keystore for sensitive fields
- **Secure Storage:** EncryptedSharedPreferences
- **Session Management:** Auto-logout after 15 min inactivity
- **Biometric Auth:** Fingerprint/Face unlock support

For detailed security information, see [SECURITY.md](docs/SECURITY.md).

---

## 📊 Performance

- **App Launch:** < 2 seconds
- **Dashboard Load:** < 2 seconds
- **Transaction Add:** < 500ms
- **Search Results:** < 1 second
- **App Size:** < 50MB

---

## 🗺️ Roadmap

### v1.0 (MVP) - Current
- ✅ Authentication system
- ✅ Account management
- ✅ Transaction tracking
- ✅ Budget management
- ✅ Basic analytics

### v1.1 - Planned
- [ ] Recurring transactions
- [ ] Smart insights
- [ ] Export functionality
- [ ] Advanced filters

### v1.2 - Future
- [ ] Goals/Savings feature
- [ ] Receipt management
- [ ] Bill splitting
- [ ] Calendar view

### v2.0 - Vision
- [ ] Cloud sync (encrypted)
- [ ] Multi-user support
- [ ] Investment tracking
- [ ] Wear OS app

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and development process.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/yourusername)

See also the list of [contributors](https://github.com/yourusername/finance-management-app/contributors) who participated in this project.

---

## 🙏 Acknowledgments

- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - Beautiful charts library
- [Room Database](https://developer.android.com/training/data-storage/room) - Robust database layer
- [Hilt](https://dagger.dev/hilt/) - Dependency injection
- [Material Design](https://material.io/) - UI components and guidelines
- [TensorFlow Lite](https://www.tensorflow.org/lite) - On-device ML
- [ML Kit](https://developers.google.com/ml-kit) - OCR and text recognition

---

## 📞 Support

For support, email support@yourapp.com or join our [Discord server](https://discord.gg/yourserver).

---

## 📱 Download

Coming soon to Google Play Store!

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store)

---

## ⭐ Star History

[![Star History Chart](https://api.star-history.com/svg?repos=yourusername/finance-management-app&type=Date)](https://star-history.com/#yourusername/finance-management-app&Date)

---

**Made with ❤️ in India** 🇮🇳

---

## 📝 Changelog

### [1.0.0] - 2026-XX-XX
#### Added
- Initial release
- User authentication
- Account management
- Transaction tracking
- Budget management
- Dashboard with analytics
- Notifications system

See [CHANGELOG.md](CHANGELOG.md) for full version history.
