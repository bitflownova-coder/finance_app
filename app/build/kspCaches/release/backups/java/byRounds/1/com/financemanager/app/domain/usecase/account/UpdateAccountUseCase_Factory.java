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
public final class UpdateAccountUseCase_Factory implements Factory<UpdateAccountUseCase> {
  private final Provider<BankAccountRepository> repositoryProvider;

  public UpdateAccountUseCase_Factory(Provider<BankAccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateAccountUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateAccountUseCase_Factory create(
      Provider<BankAccountRepository> repositoryProvider) {
    return new UpdateAccountUseCase_Factory(repositoryProvider);
  }

  public static UpdateAccountUseCase newInstance(BankAccountRepository repository) {
    return new UpdateAccountUseCase(repository);
  }
}
