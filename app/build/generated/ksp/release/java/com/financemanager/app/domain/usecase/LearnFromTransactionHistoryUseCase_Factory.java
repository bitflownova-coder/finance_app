package com.financemanager.app.domain.usecase;

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
public final class LearnFromTransactionHistoryUseCase_Factory implements Factory<LearnFromTransactionHistoryUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public LearnFromTransactionHistoryUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public LearnFromTransactionHistoryUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static LearnFromTransactionHistoryUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new LearnFromTransactionHistoryUseCase_Factory(transactionRepositoryProvider);
  }

  public static LearnFromTransactionHistoryUseCase newInstance(
      TransactionRepository transactionRepository) {
    return new LearnFromTransactionHistoryUseCase(transactionRepository);
  }
}
