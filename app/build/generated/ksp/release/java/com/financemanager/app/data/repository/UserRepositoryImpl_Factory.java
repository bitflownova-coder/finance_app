package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.UserDao;
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
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<UserDao> userDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public UserRepositoryImpl_Factory(Provider<UserDao> userDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.userDaoProvider = userDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(userDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static UserRepositoryImpl_Factory create(Provider<UserDao> userDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new UserRepositoryImpl_Factory(userDaoProvider, ioDispatcherProvider);
  }

  public static UserRepositoryImpl newInstance(UserDao userDao, CoroutineDispatcher ioDispatcher) {
    return new UserRepositoryImpl(userDao, ioDispatcher);
  }
}
