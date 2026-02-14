package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.SplitBillDao;
import com.financemanager.app.data.mapper.SplitBillMapper;
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
public final class SplitBillRepositoryImpl_Factory implements Factory<SplitBillRepositoryImpl> {
  private final Provider<SplitBillDao> splitBillDaoProvider;

  private final Provider<SplitBillMapper> mapperProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public SplitBillRepositoryImpl_Factory(Provider<SplitBillDao> splitBillDaoProvider,
      Provider<SplitBillMapper> mapperProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.splitBillDaoProvider = splitBillDaoProvider;
    this.mapperProvider = mapperProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public SplitBillRepositoryImpl get() {
    return newInstance(splitBillDaoProvider.get(), mapperProvider.get(), ioDispatcherProvider.get());
  }

  public static SplitBillRepositoryImpl_Factory create(Provider<SplitBillDao> splitBillDaoProvider,
      Provider<SplitBillMapper> mapperProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new SplitBillRepositoryImpl_Factory(splitBillDaoProvider, mapperProvider, ioDispatcherProvider);
  }

  public static SplitBillRepositoryImpl newInstance(SplitBillDao splitBillDao,
      SplitBillMapper mapper, CoroutineDispatcher ioDispatcher) {
    return new SplitBillRepositoryImpl(splitBillDao, mapper, ioDispatcher);
  }
}
