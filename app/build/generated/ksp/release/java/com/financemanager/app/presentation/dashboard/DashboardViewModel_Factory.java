package com.financemanager.app.presentation.dashboard;

import com.financemanager.app.domain.usecase.account.CalculateTotalBalanceUseCase;
import com.financemanager.app.domain.usecase.account.GetAccountsUseCase;
import com.financemanager.app.domain.usecase.transaction.GetMonthlyExpensesUseCase;
import com.financemanager.app.domain.usecase.transaction.GetMonthlyIncomeUseCase;
import com.financemanager.app.domain.usecase.transaction.GetTransactionsUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<CalculateTotalBalanceUseCase> calculateTotalBalanceUseCaseProvider;

  private final Provider<GetAccountsUseCase> getAccountsUseCaseProvider;

  private final Provider<GetTransactionsUseCase> getTransactionsUseCaseProvider;

  private final Provider<GetMonthlyIncomeUseCase> getMonthlyIncomeUseCaseProvider;

  private final Provider<GetMonthlyExpensesUseCase> getMonthlyExpensesUseCaseProvider;

  public DashboardViewModel_Factory(Provider<SessionManager> sessionManagerProvider,
      Provider<CalculateTotalBalanceUseCase> calculateTotalBalanceUseCaseProvider,
      Provider<GetAccountsUseCase> getAccountsUseCaseProvider,
      Provider<GetTransactionsUseCase> getTransactionsUseCaseProvider,
      Provider<GetMonthlyIncomeUseCase> getMonthlyIncomeUseCaseProvider,
      Provider<GetMonthlyExpensesUseCase> getMonthlyExpensesUseCaseProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.calculateTotalBalanceUseCaseProvider = calculateTotalBalanceUseCaseProvider;
    this.getAccountsUseCaseProvider = getAccountsUseCaseProvider;
    this.getTransactionsUseCaseProvider = getTransactionsUseCaseProvider;
    this.getMonthlyIncomeUseCaseProvider = getMonthlyIncomeUseCaseProvider;
    this.getMonthlyExpensesUseCaseProvider = getMonthlyExpensesUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(sessionManagerProvider.get(), calculateTotalBalanceUseCaseProvider.get(), getAccountsUseCaseProvider.get(), getTransactionsUseCaseProvider.get(), getMonthlyIncomeUseCaseProvider.get(), getMonthlyExpensesUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<SessionManager> sessionManagerProvider,
      Provider<CalculateTotalBalanceUseCase> calculateTotalBalanceUseCaseProvider,
      Provider<GetAccountsUseCase> getAccountsUseCaseProvider,
      Provider<GetTransactionsUseCase> getTransactionsUseCaseProvider,
      Provider<GetMonthlyIncomeUseCase> getMonthlyIncomeUseCaseProvider,
      Provider<GetMonthlyExpensesUseCase> getMonthlyExpensesUseCaseProvider) {
    return new DashboardViewModel_Factory(sessionManagerProvider, calculateTotalBalanceUseCaseProvider, getAccountsUseCaseProvider, getTransactionsUseCaseProvider, getMonthlyIncomeUseCaseProvider, getMonthlyExpensesUseCaseProvider);
  }

  public static DashboardViewModel newInstance(SessionManager sessionManager,
      CalculateTotalBalanceUseCase calculateTotalBalanceUseCase,
      GetAccountsUseCase getAccountsUseCase, GetTransactionsUseCase getTransactionsUseCase,
      GetMonthlyIncomeUseCase getMonthlyIncomeUseCase,
      GetMonthlyExpensesUseCase getMonthlyExpensesUseCase) {
    return new DashboardViewModel(sessionManager, calculateTotalBalanceUseCase, getAccountsUseCase, getTransactionsUseCase, getMonthlyIncomeUseCase, getMonthlyExpensesUseCase);
  }
}
