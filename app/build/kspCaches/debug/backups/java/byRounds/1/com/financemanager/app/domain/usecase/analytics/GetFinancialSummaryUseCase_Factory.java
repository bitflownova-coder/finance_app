package com.financemanager.app.domain.usecase.analytics;

import com.financemanager.app.domain.repository.TransactionRepository;
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
public final class GetFinancialSummaryUseCase_Factory implements Factory<GetFinancialSummaryUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public GetFinancialSummaryUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GetFinancialSummaryUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static GetFinancialSummaryUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new GetFinancialSummaryUseCase_Factory(transactionRepositoryProvider);
  }

  public static GetFinancialSummaryUseCase newInstance(
      TransactionRepository transactionRepository) {
    return new GetFinancialSummaryUseCase(transactionRepository);
  }
}
