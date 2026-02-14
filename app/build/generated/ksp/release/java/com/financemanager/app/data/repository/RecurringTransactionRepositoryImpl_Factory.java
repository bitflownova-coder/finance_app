package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.RecurringTransactionDao;
import com.financemanager.app.data.local.dao.TransactionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
@QualifierMetadata("com.financemanager.app.di.IoDispatcher")
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
public final class RecurringTransactionRepositoryImpl_Factory implements Factory<RecurringTransactionRepositoryImpl> {
  private final Provider<RecurringTransactionDao> recurringTransactionDaoProvider;

  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public RecurringTransactionRepositoryImpl_Factory(
      Provider<RecurringTransactionDao> recurringTransactionDaoProvider,
      Provider<TransactionDao> transactionDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.recurringTransactionDaoProvider = recurringTransactionDaoProvider;
    this.transactionDaoProvider = transactionDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public RecurringTransactionRepositoryImpl get() {
    return newInstance(recurringTransactionDaoProvider.get(), transactionDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static RecurringTransactionRepositoryImpl_Factory create(
      Provider<RecurringTransactionDao> recurringTransactionDaoProvider,
      Provider<TransactionDao> transactionDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new RecurringTransactionRepositoryImpl_Factory(recurringTransactionDaoProvider, transactionDaoProvider, ioDispatcherProvider);
  }

  public static RecurringTransactionRepositoryImpl newInstance(
      RecurringTransactionDao recurringTransactionDao, TransactionDao transactionDao,
      CoroutineDispatcher ioDispatcher) {
    return new RecurringTransactionRepositoryImpl(recurringTransactionDao, transactionDao, ioDispatcher);
  }
}
