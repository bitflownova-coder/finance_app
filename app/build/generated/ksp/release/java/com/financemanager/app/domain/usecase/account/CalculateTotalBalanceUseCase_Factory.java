package com.financemanager.app.domain.usecase.account;

import com.financemanager.app.domain.repository.BankAccountRepository;
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
public final class CalculateTotalBalanceUseCase_Factory implements Factory<CalculateTotalBalanceUseCase> {
  private final Provider<BankAccountRepository> repositoryProvider;

  public CalculateTotalBalanceUseCase_Factory(Provider<BankAccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CalculateTotalBalanceUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CalculateTotalBalanceUseCase_Factory create(
      Provider<BankAccountRepository> repositoryProvider) {
    return new CalculateTotalBalanceUseCase_Factory(repositoryProvider);
  }

  public static CalculateTotalBalanceUseCase newInstance(BankAccountRepository repository) {
    return new CalculateTotalBalanceUseCase(repository);
  }
}
