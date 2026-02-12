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
public final class GetMonthlyTrendUseCase_Factory implements Factory<GetMonthlyTrendUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public GetMonthlyTrendUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GetMonthlyTrendUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static GetMonthlyTrendUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new GetMonthlyTrendUseCase_Factory(transactionRepositoryProvider);
  }

  public static GetMonthlyTrendUseCase newInstance(TransactionRepository transactionRepository) {
    return new GetMonthlyTrendUseCase(transactionRepository);
  }
}
