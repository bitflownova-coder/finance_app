package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.BankAccountDao;
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
public final class BankAccountRepositoryImpl_Factory implements Factory<BankAccountRepositoryImpl> {
  private final Provider<BankAccountDao> bankAccountDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public BankAccountRepositoryImpl_Factory(Provider<BankAccountDao> bankAccountDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.bankAccountDaoProvider = bankAccountDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public BankAccountRepositoryImpl get() {
    return newInstance(bankAccountDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static BankAccountRepositoryImpl_Factory create(
      Provider<BankAccountDao> bankAccountDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new BankAccountRepositoryImpl_Factory(bankAccountDaoProvider, ioDispatcherProvider);
  }

  public static BankAccountRepositoryImpl newInstance(BankAccountDao bankAccountDao,
      CoroutineDispatcher ioDispatcher) {
    return new BankAccountRepositoryImpl(bankAccountDao, ioDispatcher);
  }
}
