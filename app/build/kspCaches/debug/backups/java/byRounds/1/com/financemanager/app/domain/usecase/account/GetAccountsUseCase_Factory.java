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
public final class GetAccountsUseCase_Factory implements Factory<GetAccountsUseCase> {
  private final Provider<BankAccountRepository> repositoryProvider;

  public GetAccountsUseCase_Factory(Provider<BankAccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAccountsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAccountsUseCase_Factory create(
      Provider<BankAccountRepository> repositoryProvider) {
    return new GetAccountsUseCase_Factory(repositoryProvider);
  }

  public static GetAccountsUseCase newInstance(BankAccountRepository repository) {
    return new GetAccountsUseCase(repository);
  }
}
