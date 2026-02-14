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
public final class ToggleRecurringActiveUseCase_Factory implements Factory<ToggleRecurringActiveUseCase> {
  private final Provider<RecurringTransactionRepository> repositoryProvider;

  public ToggleRecurringActiveUseCase_Factory(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ToggleRecurringActiveUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ToggleRecurringActiveUseCase_Factory create(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    return new ToggleRecurringActiveUseCase_Factory(repositoryProvider);
  }

  public static ToggleRecurringActiveUseCase newInstance(
      RecurringTransactionRepository repository) {
    return new ToggleRecurringActiveUseCase(repository);
  }
}
