package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.BankAccountDao;
import com.financemanager.app.data.local.dao.BudgetDao;
import com.financemanager.app.data.local.dao.TransactionDao;
import com.financemanager.app.data.local.database.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TransactionRepositoryImpl_Factory implements Factory<TransactionRepositoryImpl> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<BankAccountDao> bankAccountDaoProvider;

  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<AppDatabase> databaseProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public TransactionRepositoryImpl_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<BankAccountDao> bankAccountDaoProvider, Provider<BudgetDao> budgetDaoProvider,
      Provider<AppDatabase> databaseProvider, Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.bankAccountDaoProvider = bankAccountDaoProvider;
    this.budgetDaoProvider = budgetDaoProvider;
    this.databaseProvider = databaseProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public TransactionRepositoryImpl get() {
    return newInstance(transactionDaoProvider.get(), bankAccountDaoProvider.get(), budgetDaoProvider.get(), databaseProvider.get(), ioDispatcherProvider.get());
  }

  public static TransactionRepositoryImpl_Factory create(
      Provider<TransactionDao> transactionDaoProvider,
      Provider<BankAccountDao> bankAccountDaoProvider, Provider<BudgetDao> budgetDaoProvider,
      Provider<AppDatabase> databaseProvider, Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new TransactionRepositoryImpl_Factory(transactionDaoProvider, bankAccountDaoProvider, budgetDaoProvider, databaseProvider, ioDispatcherProvider);
  }

  public static TransactionRepositoryImpl newInstance(TransactionDao transactionDao,
      BankAccountDao bankAccountDao, BudgetDao budgetDao, AppDatabase database,
      CoroutineDispatcher ioDispatcher) {
    return new TransactionRepositoryImpl(transactionDao, bankAccountDao, budgetDao, database, ioDispatcher);
  }
}
