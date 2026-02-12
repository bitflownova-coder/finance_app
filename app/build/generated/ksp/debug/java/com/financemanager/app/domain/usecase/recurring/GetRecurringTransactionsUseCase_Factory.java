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
public final class GetRecurringTransactionsUseCase_Factory implements Factory<GetRecurringTransactionsUseCase> {
  private final Provider<RecurringTransactionRepository> repositoryProvider;

  public GetRecurringTransactionsUseCase_Factory(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetRecurringTransactionsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetRecurringTransactionsUseCase_Factory create(
      Provider<RecurringTransactionRepository> repositoryProvider) {
    return new GetRecurringTransactionsUseCase_Factory(repositoryProvider);
  }

  public static GetRecurringTransactionsUseCase newInstance(
      RecurringTransactionRepository repository) {
    return new GetRecurringTransactionsUseCase(repository);
  }
}
