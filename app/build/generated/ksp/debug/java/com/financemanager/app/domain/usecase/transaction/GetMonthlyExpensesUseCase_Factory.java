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
public final class GetMonthlyExpensesUseCase_Factory implements Factory<GetMonthlyExpensesUseCase> {
  private final Provider<TransactionRepository> repositoryProvider;

  public GetMonthlyExpensesUseCase_Factory(Provider<TransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetMonthlyExpensesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetMonthlyExpensesUseCase_Factory create(
      Provider<TransactionRepository> repositoryProvider) {
    return new GetMonthlyExpensesUseCase_Factory(repositoryProvider);
  }

  public static GetMonthlyExpensesUseCase newInstance(TransactionRepository repository) {
    return new GetMonthlyExpensesUseCase(repository);
  }
}
