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
public final class GetMonthlyIncomeUseCase_Factory implements Factory<GetMonthlyIncomeUseCase> {
  private final Provider<TransactionRepository> repositoryProvider;

  public GetMonthlyIncomeUseCase_Factory(Provider<TransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetMonthlyIncomeUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetMonthlyIncomeUseCase_Factory create(
      Provider<TransactionRepository> repositoryProvider) {
    return new GetMonthlyIncomeUseCase_Factory(repositoryProvider);
  }

  public static GetMonthlyIncomeUseCase newInstance(TransactionRepository repository) {
    return new GetMonthlyIncomeUseCase(repository);
  }
}
