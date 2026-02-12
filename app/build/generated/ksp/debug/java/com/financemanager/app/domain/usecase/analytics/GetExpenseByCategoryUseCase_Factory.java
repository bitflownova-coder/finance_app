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
public final class GetExpenseByCategoryUseCase_Factory implements Factory<GetExpenseByCategoryUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public GetExpenseByCategoryUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GetExpenseByCategoryUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static GetExpenseByCategoryUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new GetExpenseByCategoryUseCase_Factory(transactionRepositoryProvider);
  }

  public static GetExpenseByCategoryUseCase newInstance(
      TransactionRepository transactionRepository) {
    return new GetExpenseByCategoryUseCase(transactionRepository);
  }
}
