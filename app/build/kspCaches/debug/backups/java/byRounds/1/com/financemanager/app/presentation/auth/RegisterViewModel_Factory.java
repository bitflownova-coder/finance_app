package com.financemanager.app.presentation.auth;

import com.financemanager.app.domain.usecase.auth.RegisterUseCase;
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
public final class RegisterViewModel_Factory implements Factory<RegisterViewModel> {
  private final Provider<RegisterUseCase> registerUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public RegisterViewModel_Factory(Provider<RegisterUseCase> registerUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.registerUseCaseProvider = registerUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public RegisterViewModel get() {
    return newInstance(registerUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static RegisterViewModel_Factory create(Provider<RegisterUseCase> registerUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new RegisterViewModel_Factory(registerUseCaseProvider, sessionManagerProvider);
  }

  public static RegisterViewModel newInstance(RegisterUseCase registerUseCase,
      SessionManager sessionManager) {
    return new RegisterViewModel(registerUseCase, sessionManager);
  }
}
