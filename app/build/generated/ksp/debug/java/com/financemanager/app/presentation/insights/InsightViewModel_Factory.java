package com.financemanager.app.presentation.insights;

import com.financemanager.app.domain.usecase.insights.GenerateInsightsUseCase;
import com.financemanager.app.domain.usecase.insights.GetCategoryInsightsUseCase;
import com.financemanager.app.domain.usecase.insights.GetMonthlySummaryUseCase;
import com.financemanager.app.domain.usecase.insights.GetSpendingPatternsUseCase;
import com.financemanager.app.domain.usecase.insights.PredictExpensesUseCase;
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
public final class InsightViewModel_Factory implements Factory<InsightViewModel> {
  private final Provider<GenerateInsightsUseCase> generateInsightsUseCaseProvider;

  private final Provider<GetSpendingPatternsUseCase> getSpendingPatternsUseCaseProvider;

  private final Provider<GetMonthlySummaryUseCase> getMonthlySummaryUseCaseProvider;

  private final Provider<GetCategoryInsightsUseCase> getCategoryInsightsUseCaseProvider;

  private final Provider<PredictExpensesUseCase> predictExpensesUseCaseProvider;

  public InsightViewModel_Factory(Provider<GenerateInsightsUseCase> generateInsightsUseCaseProvider,
      Provider<GetSpendingPatternsUseCase> getSpendingPatternsUseCaseProvider,
      Provider<GetMonthlySummaryUseCase> getMonthlySummaryUseCaseProvider,
      Provider<GetCategoryInsightsUseCase> getCategoryInsightsUseCaseProvider,
      Provider<PredictExpensesUseCase> predictExpensesUseCaseProvider) {
    this.generateInsightsUseCaseProvider = generateInsightsUseCaseProvider;
    this.getSpendingPatternsUseCaseProvider = getSpendingPatternsUseCaseProvider;
    this.getMonthlySummaryUseCaseProvider = getMonthlySummaryUseCaseProvider;
    this.getCategoryInsightsUseCaseProvider = getCategoryInsightsUseCaseProvider;
    this.predictExpensesUseCaseProvider = predictExpensesUseCaseProvider;
  }

  @Override
  public InsightViewModel get() {
    return newInstance(generateInsightsUseCaseProvider.get(), getSpendingPatternsUseCaseProvider.get(), getMonthlySummaryUseCaseProvider.get(), getCategoryInsightsUseCaseProvider.get(), predictExpensesUseCaseProvider.get());
  }

  public static InsightViewModel_Factory create(
      Provider<GenerateInsightsUseCase> generateInsightsUseCaseProvider,
      Provider<GetSpendingPatternsUseCase> getSpendingPatternsUseCaseProvider,
      Provider<GetMonthlySummaryUseCase> getMonthlySummaryUseCaseProvider,
      Provider<GetCategoryInsightsUseCase> getCategoryInsightsUseCaseProvider,
      Provider<PredictExpensesUseCase> predictExpensesUseCaseProvider) {
    return new InsightViewModel_Factory(generateInsightsUseCaseProvider, getSpendingPatternsUseCaseProvider, getMonthlySummaryUseCaseProvider, getCategoryInsightsUseCaseProvider, predictExpensesUseCaseProvider);
  }

  public static InsightViewModel newInstance(GenerateInsightsUseCase generateInsightsUseCase,
      GetSpendingPatternsUseCase getSpendingPatternsUseCase,
      GetMonthlySummaryUseCase getMonthlySummaryUseCase,
      GetCategoryInsightsUseCase getCategoryInsightsUseCase,
      PredictExpensesUseCase predictExpensesUseCase) {
    return new InsightViewModel(generateInsightsUseCase, getSpendingPatternsUseCase, getMonthlySummaryUseCase, getCategoryInsightsUseCase, predictExpensesUseCase);
  }
}
