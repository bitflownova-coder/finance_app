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
public final class GetTopSpendingCategoriesUseCase_Factory implements Factory<GetTopSpendingCategoriesUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public GetTopSpendingCategoriesUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GetTopSpendingCategoriesUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static GetTopSpendingCategoriesUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new GetTopSpendingCategoriesUseCase_Factory(transactionRepositoryProvider);
  }

  public static GetTopSpendingCategoriesUseCase newInstance(
      TransactionRepository transactionRepository) {
    return new GetTopSpendingCategoriesUseCase(transactionRepository);
  }
}
