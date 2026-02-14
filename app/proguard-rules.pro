-keep class com.financemanager.app.** { *; }
-keep class at.favre.lib.crypto.bcrypt.** { *; }
-keep class net.sqlcipher.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
