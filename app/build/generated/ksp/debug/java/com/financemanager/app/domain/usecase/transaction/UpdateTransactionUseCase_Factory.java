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
public final class UpdateTransactionUseCase_Factory implements Factory<UpdateTransactionUseCase> {
  private final Provider<TransactionRepository> repositoryProvider;

  public UpdateTransactionUseCase_Factory(Provider<TransactionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateTransactionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateTransactionUseCase_Factory create(
      Provider<TransactionRepository> repositoryProvider) {
    return new UpdateTransactionUseCase_Factory(repositoryProvider);
  }

  public static UpdateTransactionUseCase newInstance(TransactionRepository repository) {
    return new UpdateTransactionUseCase(repository);
  }
}
