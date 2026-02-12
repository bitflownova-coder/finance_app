package com.financemanager.app.util;

import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SessionManager_Factory implements Factory<SessionManager> {
  private final Provider<SharedPreferences> sharedPreferencesProvider;

  public SessionManager_Factory(Provider<SharedPreferences> sharedPreferencesProvider) {
    this.sharedPreferencesProvider = sharedPreferencesProvider;
  }

  @Override
  public SessionManager get() {
    return newInstance(sharedPreferencesProvider.get());
  }

  public static SessionManager_Factory create(
      Provider<SharedPreferences> sharedPreferencesProvider) {
    return new SessionManager_Factory(sharedPreferencesProvider);
  }

  public static SessionManager newInstance(SharedPreferences sharedPreferences) {
    return new SessionManager(sharedPreferences);
  }
}
