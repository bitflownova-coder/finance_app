package com.financemanager.app.domain.usecase.transaction;

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
public final class SearchTransactionsUseCase_Factory implements Factory<SearchTransactionsUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public SearchTransactionsUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public SearchTransactionsUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static SearchTransactionsUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new SearchTransactionsUseCase_Factory(transactionRepositoryProvider);
  }

  public static SearchTransactionsUseCase newInstance(TransactionRepository transactionRepository) {
    return new SearchTransactionsUseCase(transactionRepository);
  }
}
