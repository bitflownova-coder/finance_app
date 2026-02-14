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
public final class DeleteRecurringTransactionUseCase_Factory implements Factory<DeleteRecurringTransactionUseCase> {
  private final Provider<RecurringTransactionRepository> repositoryProvider;

  public DeleteRecurringTransactionUseCase_Factory(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteRecurringTransactionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteRecurringTransactionUseCase_Factory create(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    return new DeleteRecurringTransactionUseCase_Factory(repositoryProvider);
  }

  public static DeleteRecurringTransactionUseCase newInstance(
      RecurringTransactionRepository repository) {
    return new DeleteRecurringTransactionUseCase(repository);
  }
}
