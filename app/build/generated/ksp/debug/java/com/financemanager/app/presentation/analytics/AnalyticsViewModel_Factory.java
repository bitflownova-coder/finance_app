package com.financemanager.app.presentation.analytics;

import com.financemanager.app.domain.usecase.analytics.GetExpenseByCategoryUseCase;
import com.financemanager.app.domain.usecase.analytics.GetFinancialSummaryUseCase;
import com.financemanager.app.domain.usecase.analytics.GetMonthlyTrendUseCase;
import com.financemanager.app.domain.usecase.analytics.GetTopSpendingCategoriesUseCase;
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<GetExpenseByCategoryUseCase> getExpenseByCategoryUseCaseProvider;

  private final Provider<GetMonthlyTrendUseCase> getMonthlyTrendUseCaseProvider;

  private final Provider<GetTopSpendingCategoriesUseCase> getTopSpendingCategoriesUseCaseProvider;

  private final Provider<GetFinancialSummaryUseCase> getFinancialSummaryUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public AnalyticsViewModel_Factory(
      Provider<GetExpenseByCategoryUseCase> getExpenseByCategoryUseCaseProvider,
      Provider<GetMonthlyTrendUseCase> getMonthlyTrendUseCaseProvider,
      Provider<GetTopSpendingCategoriesUseCase> getTopSpendingCategoriesUseCaseProvider,
      Provider<GetFinancialSummaryUseCase> getFinancialSummaryUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.getExpenseByCategoryUseCaseProvider = getExpenseByCategoryUseCaseProvider;
    this.getMonthlyTrendUseCaseProvider = getMonthlyTrendUseCaseProvider;
    this.getTopSpendingCategoriesUseCaseProvider = getTopSpendingCategoriesUseCaseProvider;
    this.getFinancialSummaryUseCaseProvider = getFinancialSummaryUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(getExpenseByCategoryUseCaseProvider.get(), getMonthlyTrendUseCaseProvider.get(), getTopSpendingCategoriesUseCaseProvider.get(), getFinancialSummaryUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static AnalyticsViewModel_Factory create(
      Provider<GetExpenseByCategoryUseCase> getExpenseByCategoryUseCaseProvider,
      Provider<GetMonthlyTrendUseCase> getMonthlyTrendUseCaseProvider,
      Provider<GetTopSpendingCategoriesUseCase> getTopSpendingCategoriesUseCaseProvider,
      Provider<GetFinancialSummaryUseCase> getFinancialSummaryUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new AnalyticsViewModel_Factory(getExpenseByCategoryUseCaseProvider, getMonthlyTrendUseCaseProvider, getTopSpendingCategoriesUseCaseProvider, getFinancialSummaryUseCaseProvider, sessionManagerProvider);
  }

  public static AnalyticsViewModel newInstance(
      GetExpenseByCategoryUseCase getExpenseByCategoryUseCase,
      GetMonthlyTrendUseCase getMonthlyTrendUseCase,
      GetTopSpendingCategoriesUseCase getTopSpendingCategoriesUseCase,
      GetFinancialSummaryUseCase getFinancialSummaryUseCase, SessionManager sessionManager) {
    return new AnalyticsViewModel(getExpenseByCategoryUseCase, getMonthlyTrendUseCase, getTopSpendingCategoriesUseCase, getFinancialSummaryUseCase, sessionManager);
  }
}
