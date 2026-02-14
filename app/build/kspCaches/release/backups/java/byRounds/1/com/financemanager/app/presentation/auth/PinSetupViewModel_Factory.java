package com.financemanager.app.presentation.auth;

import com.financemanager.app.domain.usecase.auth.SetupPinUseCase;
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
public final class PinSetupViewModel_Factory implements Factory<PinSetupViewModel> {
  private final Provider<SetupPinUseCase> setupPinUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public PinSetupViewModel_Factory(Provider<SetupPinUseCase> setupPinUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.setupPinUseCaseProvider = setupPinUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public PinSetupViewModel get() {
    return newInstance(setupPinUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static PinSetupViewModel_Factory create(Provider<SetupPinUseCase> setupPinUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new PinSetupViewModel_Factory(setupPinUseCaseProvider, sessionManagerProvider);
  }

  public static PinSetupViewModel newInstance(SetupPinUseCase setupPinUseCase,
      SessionManager sessionManager) {
    return new PinSetupViewModel(setupPinUseCase, sessionManager);
  }
}
