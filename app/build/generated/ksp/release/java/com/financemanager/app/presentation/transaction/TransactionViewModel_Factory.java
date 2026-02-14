package com.financemanager.app.presentation.transaction;

import com.financemanager.app.domain.usecase.transaction.AddTransactionUseCase;
import com.financemanager.app.domain.usecase.transaction.DeleteTransactionUseCase;
import com.financemanager.app.domain.usecase.transaction.GetTransactionsUseCase;
import com.financemanager.app.domain.usecase.transaction.SearchTransactionsUseCase;
import com.financemanager.app.domain.usecase.transaction.UpdateTransactionUseCase;
import com.financemanager.app.util.SessionManager;
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
public final class TransactionViewModel_Factory implements Factory<TransactionViewModel> {
  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<GetTransactionsUseCase> getTransactionsUseCaseProvider;

  private final Provider<AddTransactionUseCase> addTransactionUseCaseProvider;

  private final Provider<UpdateTransactionUseCase> updateTransactionUseCaseProvider;

  private final Provider<DeleteTransactionUseCase> deleteTransactionUseCaseProvider;

  private final Provider<SearchTransactionsUseCase> searchTransactionsUseCaseProvider;

  public TransactionViewModel_Factory(Provider<SessionManager> sessionManagerProvider,
      Provider<GetTransactionsUseCase> getTransactionsUseCaseProvider,
      Provider<AddTransactionUseCase> addTransactionUseCaseProvider,
      Provider<UpdateTransactionUseCase> updateTransactionUseCaseProvider,
      Provider<DeleteTransactionUseCase> deleteTransactionUseCaseProvider,
      Provider<SearchTransactionsUseCase> searchTransactionsUseCaseProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.getTransactionsUseCaseProvider = getTransactionsUseCaseProvider;
    this.addTransactionUseCaseProvider = addTransactionUseCaseProvider;
    this.updateTransactionUseCaseProvider = updateTransactionUseCaseProvider;
    this.deleteTransactionUseCaseProvider = deleteTransactionUseCaseProvider;
    this.searchTransactionsUseCaseProvider = searchTransactionsUseCaseProvider;
  }

  @Override
  public TransactionViewModel get() {
    return newInstance(sessionManagerProvider.get(), getTransactionsUseCaseProvider.get(), addTransactionUseCaseProvider.get(), updateTransactionUseCaseProvider.get(), deleteTransactionUseCaseProvider.get(), searchTransactionsUseCaseProvider.get());
  }

  public static TransactionViewModel_Factory create(Provider<SessionManager> sessionManagerProvider,
      Provider<GetTransactionsUseCase> getTransactionsUseCaseProvider,
      Provider<AddTransactionUseCase> addTransactionUseCaseProvider,
      Provider<UpdateTransactionUseCase> updateTransactionUseCaseProvider,
      Provider<DeleteTransactionUseCase> deleteTransactionUseCaseProvider,
      Provider<SearchTransactionsUseCase> searchTransactionsUseCaseProvider) {
    return new TransactionViewModel_Factory(sessionManagerProvider, getTransactionsUseCaseProvider, addTransactionUseCaseProvider, updateTransactionUseCaseProvider, deleteTransactionUseCaseProvider, searchTransactionsUseCaseProvider);
  }

  public static TransactionViewModel newInstance(SessionManager sessionManager,
      GetTransactionsUseCase getTransactionsUseCase, AddTransactionUseCase addTransactionUseCase,
      UpdateTransactionUseCase updateTransactionUseCase,
      DeleteTransactionUseCase deleteTransactionUseCase,
      SearchTransactionsUseCase searchTransactionsUseCase) {
    return new TransactionViewModel(sessionManager, getTransactionsUseCase, addTransactionUseCase, updateTransactionUseCase, deleteTransactionUseCase, searchTransactionsUseCase);
  }
}
