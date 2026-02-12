package com.financemanager.app.domain.usecase.recurring;

import com.financemanager.app.domain.repository.RecurringTransactionRepository;
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
public final class ProcessRecurringTransactionsUseCase_Factory implements Factory<ProcessRecurringTransactionsUseCase> {
  private final Provider<RecurringTransactionRepository> repositoryProvider;

  public ProcessRecurringTransactionsUseCase_Factory(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ProcessRecurringTransactionsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ProcessRecurringTransactionsUseCase_Factory create(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    return new ProcessRecurringTransactionsUseCase_Factory(repositoryProvider);
  }

  public static ProcessRecurringTransactionsUseCase newInstance(
      RecurringTransactionRepository repository) {
    return new ProcessRecurringTransactionsUseCase(repository);
  }
}
