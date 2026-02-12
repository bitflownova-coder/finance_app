package com.financemanager.app.presentation.auth;

import com.financemanager.app.domain.usecase.account.AddAccountUseCase;
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
public final class BankSetupViewModel_Factory implements Factory<BankSetupViewModel> {
  private final Provider<AddAccountUseCase> addAccountUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public BankSetupViewModel_Factory(Provider<AddAccountUseCase> addAccountUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.addAccountUseCaseProvider = addAccountUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public BankSetupViewModel get() {
    return newInstance(addAccountUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static BankSetupViewModel_Factory create(
      Provider<AddAccountUseCase> addAccountUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new BankSetupViewModel_Factory(addAccountUseCaseProvider, sessionManagerProvider);
  }

  public static BankSetupViewModel newInstance(AddAccountUseCase addAccountUseCase,
      SessionManager sessionManager) {
    return new BankSetupViewModel(addAccountUseCase, sessionManager);
  }
}
