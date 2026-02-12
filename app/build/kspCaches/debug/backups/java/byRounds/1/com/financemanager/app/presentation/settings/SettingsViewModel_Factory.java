package com.financemanager.app.presentation.settings;

import android.app.Application;
import com.financemanager.app.util.SessionManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<Application> applicationProvider;

  public SettingsViewModel_Factory(Provider<SessionManager> sessionManagerProvider,
      Provider<Application> applicationProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(sessionManagerProvider.get(), applicationProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<SessionManager> sessionManagerProvider,
      Provider<Application> applicationProvider) {
    return new SettingsViewModel_Factory(sessionManagerProvider, applicationProvider);
  }

  public static SettingsViewModel newInstance(SessionManager sessionManager,
      Application application) {
    return new SettingsViewModel(sessionManager, application);
  }
}
