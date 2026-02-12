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
public final class DeleteAccountUseCase_Factory implements Factory<DeleteAccountUseCase> {
  private final Provider<BankAccountRepository> repositoryProvider;

  public DeleteAccountUseCase_Factory(Provider<BankAccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteAccountUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteAccountUseCase_Factory create(
      Provider<BankAccountRepository> repositoryProvider) {
    return new DeleteAccountUseCase_Factory(repositoryProvider);
  }

  public static DeleteAccountUseCase newInstance(BankAccountRepository repository) {
    return new DeleteAccountUseCase(repository);
  }
}
