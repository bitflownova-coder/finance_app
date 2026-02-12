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
public final class AddAccountUseCase_Factory implements Factory<AddAccountUseCase> {
  private final Provider<BankAccountRepository> repositoryProvider;

  public AddAccountUseCase_Factory(Provider<BankAccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddAccountUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddAccountUseCase_Factory create(
      Provider<BankAccountRepository> repositoryProvider) {
    return new AddAccountUseCase_Factory(repositoryProvider);
  }

  public static AddAccountUseCase newInstance(BankAccountRepository repository) {
    return new AddAccountUseCase(repository);
  }
}
