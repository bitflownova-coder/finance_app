package com.financemanager.app.presentation.profile;

import com.financemanager.app.domain.usecase.account.AddAccountUseCase;
import com.financemanager.app.domain.usecase.account.CalculateTotalBalanceUseCase;
import com.financemanager.app.domain.usecase.account.DeleteAccountUseCase;
import com.financemanager.app.domain.usecase.account.GetAccountsUseCase;
import com.financemanager.app.domain.usecase.account.UpdateAccountUseCase;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<GetAccountsUseCase> getAccountsUseCaseProvider;

  private final Provider<AddAccountUseCase> addAccountUseCaseProvider;

  private final Provider<UpdateAccountUseCase> updateAccountUseCaseProvider;

  private final Provider<DeleteAccountUseCase> deleteAccountUseCaseProvider;

  private final Provider<CalculateTotalBalanceUseCase> calculateTotalBalanceUseCaseProvider;

  public ProfileViewModel_Factory(Provider<SessionManager> sessionManagerProvider,
      Provider<GetAccountsUseCase> getAccountsUseCaseProvider,
      Provider<AddAccountUseCase> addAccountUseCaseProvider,
      Provider<UpdateAccountUseCase> updateAccountUseCaseProvider,
      Provider<DeleteAccountUseCase> deleteAccountUseCaseProvider,
      Provider<CalculateTotalBalanceUseCase> calculateTotalBalanceUseCaseProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.getAccountsUseCaseProvider = getAccountsUseCaseProvider;
    this.addAccountUseCaseProvider = addAccountUseCaseProvider;
    this.updateAccountUseCaseProvider = updateAccountUseCaseProvider;
    this.deleteAccountUseCaseProvider = deleteAccountUseCaseProvider;
    this.calculateTotalBalanceUseCaseProvider = calculateTotalBalanceUseCaseProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(sessionManagerProvider.get(), getAccountsUseCaseProvider.get(), addAccountUseCaseProvider.get(), updateAccountUseCaseProvider.get(), deleteAccountUseCaseProvider.get(), calculateTotalBalanceUseCaseProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<SessionManager> sessionManagerProvider,
      Provider<GetAccountsUseCase> getAccountsUseCaseProvider,
      Provider<AddAccountUseCase> addAccountUseCaseProvider,
      Provider<UpdateAccountUseCase> updateAccountUseCaseProvider,
      Provider<DeleteAccountUseCase> deleteAccountUseCaseProvider,
      Provider<CalculateTotalBalanceUseCase> calculateTotalBalanceUseCaseProvider) {
    return new ProfileViewModel_Factory(sessionManagerProvider, getAccountsUseCaseProvider, addAccountUseCaseProvider, updateAccountUseCaseProvider, deleteAccountUseCaseProvider, calculateTotalBalanceUseCaseProvider);
  }

  public static ProfileViewModel newInstance(SessionManager sessionManager,
      GetAccountsUseCase getAccountsUseCase, AddAccountUseCase addAccountUseCase,
      UpdateAccountUseCase updateAccountUseCase, DeleteAccountUseCase deleteAccountUseCase,
      CalculateTotalBalanceUseCase calculateTotalBalanceUseCase) {
    return new ProfileViewModel(sessionManager, getAccountsUseCase, addAccountUseCase, updateAccountUseCase, deleteAccountUseCase, calculateTotalBalanceUseCase);
  }
}
