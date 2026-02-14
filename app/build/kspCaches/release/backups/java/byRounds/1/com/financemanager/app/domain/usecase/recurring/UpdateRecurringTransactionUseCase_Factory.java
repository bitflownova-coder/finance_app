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
public final class UpdateRecurringTransactionUseCase_Factory implements Factory<UpdateRecurringTransactionUseCase> {
  private final Provider<RecurringTransactionRepository> repositoryProvider;

  public UpdateRecurringTransactionUseCase_Factory(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateRecurringTransactionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateRecurringTransactionUseCase_Factory create(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    return new UpdateRecurringTransactionUseCase_Factory(repositoryProvider);
  }

  public static UpdateRecurringTransactionUseCase newInstance(
      RecurringTransactionRepository repository) {
    return new UpdateRecurringTransactionUseCase(repository);
  }
}
