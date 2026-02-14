package com.financemanager.app.presentation.auth;

import com.financemanager.app.domain.usecase.auth.VerifyPinUseCase;
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
public final class PinVerifyViewModel_Factory implements Factory<PinVerifyViewModel> {
  private final Provider<VerifyPinUseCase> verifyPinUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public PinVerifyViewModel_Factory(Provider<VerifyPinUseCase> verifyPinUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.verifyPinUseCaseProvider = verifyPinUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public PinVerifyViewModel get() {
    return newInstance(verifyPinUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static PinVerifyViewModel_Factory create(
      Provider<VerifyPinUseCase> verifyPinUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new PinVerifyViewModel_Factory(verifyPinUseCaseProvider, sessionManagerProvider);
  }

  public static PinVerifyViewModel newInstance(VerifyPinUseCase verifyPinUseCase,
      SessionManager sessionManager) {
    return new PinVerifyViewModel(verifyPinUseCase, sessionManager);
  }
}
