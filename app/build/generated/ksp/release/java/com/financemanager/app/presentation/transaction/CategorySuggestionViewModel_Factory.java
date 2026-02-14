package com.financemanager.app.presentation.transaction;

import com.financemanager.app.domain.usecase.LearnFromTransactionHistoryUseCase;
import com.financemanager.app.domain.usecase.PredictTransactionCategoryUseCase;
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
public final class CategorySuggestionViewModel_Factory implements Factory<CategorySuggestionViewModel> {
  private final Provider<PredictTransactionCategoryUseCase> predictCategoryUseCaseProvider;

  private final Provider<LearnFromTransactionHistoryUseCase> learnFromHistoryUseCaseProvider;

  public CategorySuggestionViewModel_Factory(
      Provider<PredictTransactionCategoryUseCase> predictCategoryUseCaseProvider,
      Provider<LearnFromTransactionHistoryUseCase> learnFromHistoryUseCaseProvider) {
    this.predictCategoryUseCaseProvider = predictCategoryUseCaseProvider;
    this.learnFromHistoryUseCaseProvider = learnFromHistoryUseCaseProvider;
  }

  @Override
  public CategorySuggestionViewModel get() {
    return newInstance(predictCategoryUseCaseProvider.get(), learnFromHistoryUseCaseProvider.get());
  }

  public static CategorySuggestionViewModel_Factory create(
      Provider<PredictTransactionCategoryUseCase> predictCategoryUseCaseProvider,
      Provider<LearnFromTransactionHistoryUseCase> learnFromHistoryUseCaseProvider) {
    return new CategorySuggestionViewModel_Factory(predictCategoryUseCaseProvider, learnFromHistoryUseCaseProvider);
  }

  public static CategorySuggestionViewModel newInstance(
      PredictTransactionCategoryUseCase predictCategoryUseCase,
      LearnFromTransactionHistoryUseCase learnFromHistoryUseCase) {
    return new CategorySuggestionViewModel(predictCategoryUseCase, learnFromHistoryUseCase);
  }
}
