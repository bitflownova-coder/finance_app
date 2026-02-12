# AI Code Review Checklist - Finance Management App

## General Code Quality

### Kotlin Best Practices
- [ ] Code follows Kotlin coding conventions
- [ ] Proper use of nullable types and safe calls
- [ ] Extension functions used appropriately
- [ ] Data classes used for models
- [ ] Sealed classes for state management
- [ ] Coroutines properly structured with appropriate dispatchers

### Architecture (MVVM + Clean Architecture)
- [ ] Separation of concerns maintained (Data, Domain, Presentation layers)
- [ ] ViewModels don't hold Context references
- [ ] Repository pattern properly implemented
- [ ] Use cases contain single responsibility business logic
- [ ] UI observes data through LiveData/StateFlow

### Room Database
- [ ] Entities properly annotated with @Entity
- [ ] Primary keys correctly defined
- [ ] Foreign keys and relationships properly set up
- [ ] DAOs use suspend functions with Coroutines
- [ ] Database migrations handled correctly
- [ ] Indices added for frequently queried columns
- [ ] Database transactions used for multi-step operations

### Dependency Injection (Hilt)
- [ ] All dependencies properly injected
- [ ] Modules correctly annotated (@Module, @InstallIn)
- [ ] Scope annotations used appropriately
- [ ] No manual instantiation of ViewModels

## Security

### Authentication & Data Protection
- [ ] Passwords hashed with BCrypt/Argon2 (never plain text)
- [ ] Sensitive data encrypted (UPI ID, account details)
- [ ] SharedPreferences encrypted using EncryptedSharedPreferences
- [ ] Session tokens stored securely
- [ ] Auto-logout implemented for inactivity
- [ ] SQL injection prevented (using parameterized queries)

### Permissions
- [ ] Runtime permissions requested properly (Android 6+)
- [ ] Camera permission for receipt scanning
- [ ] Notification permission for Android 13+
- [ ] Storage permissions handled correctly
- [ ] Permission rationale shown to users

## Performance

### Memory Management
- [ ] No memory leaks (coroutines cancelled in onCleared)
- [ ] Images loaded efficiently with Glide/Coil
- [ ] Large lists use RecyclerView with ViewHolder pattern
- [ ] Database queries run on background threads
- [ ] Proper lifecycle awareness (ViewModel, LiveData)

### Optimization
- [ ] Pagination implemented for large transaction lists (Paging 3)
- [ ] DiffUtil used for RecyclerView updates
- [ ] Database queries optimized with indices
- [ ] Heavy calculations offloaded to WorkManager
- [ ] Caching strategy implemented for dashboard data

## UI/UX

### Material Design Compliance
- [ ] Material Design 3 components used
- [ ] Consistent spacing and typography
- [ ] Touch targets minimum 48dp
- [ ] Proper elevation and shadows
- [ ] Ripple effects on clickable items

### User Experience
- [ ] Loading states shown during operations
- [ ] Empty states displayed when no data
- [ ] Error messages are clear and actionable
- [ ] Success feedback provided for actions
- [ ] Animations smooth (not janky)
- [ ] Back navigation works correctly
- [ ] Deep linking supported where needed

### Accessibility
- [ ] Content descriptions on images/icons
- [ ] Proper contrast ratios (light and dark mode)
- [ ] Touch targets properly sized
- [ ] Screen reader compatible
- [ ] Text scaling supported

## Functionality

### Transaction Management
- [ ] Transactions correctly deduct from account balance
- [ ] Budget automatically updated on transaction
- [ ] Transaction categories properly validated
- [ ] Edit/Delete operations work correctly
- [ ] Timestamps stored in UTC

### Account Management
- [ ] Multiple accounts supported
- [ ] Balance calculations accurate
- [ ] Account deletion handles existing transactions
- [ ] Main account designation works

### Budget System
- [ ] Budget periods calculated correctly (monthly/yearly)
- [ ] Budget alerts trigger at correct thresholds (80%, 100%)
- [ ] Budget reset works for new periods
- [ ] Multiple budget periods not overlapping

### Notifications
- [ ] Notifications show correct information
- [ ] Notification channels properly created
- [ ] Notifications dismissible
- [ ] Notification actions work (mark as read, etc.)
- [ ] Notifications respect user preferences

## Testing

### Unit Tests
- [ ] ViewModels tested with mock repositories
- [ ] Repository logic tested with in-memory database
- [ ] Use case business logic tested
- [ ] Edge cases covered (zero balance, negative amounts)
- [ ] Test coverage > 70%

### UI Tests
- [ ] Critical flows tested (login, add transaction)
- [ ] Navigation tested
- [ ] Error states tested
- [ ] Empty states tested

### Edge Cases
- [ ] Handle zero/negative balances
- [ ] Large numbers (lakhs, crores)
- [ ] Date boundary conditions (month end, year end)
- [ ] Concurrent transaction additions
- [ ] Network unavailable scenarios

## Code Maintainability

### Documentation
- [ ] Complex logic has comments
- [ ] Public APIs documented
- [ ] README up to date
- [ ] Database schema documented
- [ ] Architecture decisions recorded

### Code Organization
- [ ] Consistent package structure
- [ ] Clear naming conventions
- [ ] No god classes (>500 lines)
- [ ] No deeply nested code
- [ ] Constants extracted to companion objects

### Error Handling
- [ ] Try-catch blocks where needed
- [ ] Errors logged appropriately
- [ ] User-friendly error messages
- [ ] Crash reporting integrated (Firebase Crashlytics)

## Build & Deployment

### Configuration
- [ ] ProGuard/R8 rules configured
- [ ] API keys not hardcoded
- [ ] Different configurations for dev/prod
- [ ] Version code and name incremented
- [ ] Signing configuration secured

### Dependencies
- [ ] No unused dependencies
- [ ] Dependencies up to date
- [ ] No version conflicts
- [ ] Build variants configured correctly

## Specific Feature Checks

### Recurring Transactions
- [ ] WorkManager properly configured
- [ ] Recurring logic accurate (daily, weekly, monthly, yearly)
- [ ] Recurring transactions can be paused/resumed
- [ ] Notifications sent when auto-added

### ML Auto-Categorization
- [ ] TensorFlow Lite model integrated
- [ ] Inference runs on background thread
- [ ] Model loading error handled
- [ ] Fallback categorization works

### Receipt OCR
- [ ] ML Kit Text Recognition integrated
- [ ] Image compression before storage
- [ ] OCR parsing logic robust
- [ ] Camera permission handled

### Bill Splitting
- [ ] Split calculations accurate
- [ ] Participant management works
- [ ] Payment status tracking correct
- [ ] Share functionality works

### Export Features
- [ ] CSV export format correct
- [ ] PDF generation works
- [ ] File permissions handled
- [ ] Share intent works properly

### Calendar View
- [ ] Dates displayed correctly
- [ ] Transactions grouped by date
- [ ] Month navigation smooth
- [ ] Filter by category works

## Final Checks

- [ ] App works on different screen sizes (phones, tablets)
- [ ] App works on different Android versions (API 24-34)
- [ ] Dark mode fully functional
- [ ] No hardcoded strings (use strings.xml)
- [ ] No TODO/FIXME comments in production code
- [ ] App icon and splash screen polished
- [ ] Privacy policy included
- [ ] App passes Play Store requirements

---

## Review Sign-off

**Reviewer:** _______________  
**Date:** _______________  
**Phase:** _______________  
**Status:** ☐ Approved ☐ Needs Changes ☐ Rejected  

**Notes:**
