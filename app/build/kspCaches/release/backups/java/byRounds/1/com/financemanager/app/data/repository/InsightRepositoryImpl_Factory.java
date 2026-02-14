package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.BudgetDao;
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
public final class InsightRepositoryImpl_Factory implements Factory<InsightRepositoryImpl> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public InsightRepositoryImpl_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<BudgetDao> budgetDaoProvider, Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.budgetDaoProvider = budgetDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public InsightRepositoryImpl get() {
    return newInstance(transactionDaoProvider.get(), budgetDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static InsightRepositoryImpl_Factory create(
      Provider<TransactionDao> transactionDaoProvider, Provider<BudgetDao> budgetDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new InsightRepositoryImpl_Factory(transactionDaoProvider, budgetDaoProvider, ioDispatcherProvider);
  }

  public static InsightRepositoryImpl newInstance(TransactionDao transactionDao,
      BudgetDao budgetDao, CoroutineDispatcher ioDispatcher) {
    return new InsightRepositoryImpl(transactionDao, budgetDao, ioDispatcher);
  }
}
