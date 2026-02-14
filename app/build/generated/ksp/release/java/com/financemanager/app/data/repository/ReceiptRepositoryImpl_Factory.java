package com.financemanager.app.data.repository;

import android.content.Context;
import com.financemanager.app.data.local.dao.ReceiptDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ReceiptRepositoryImpl_Factory implements Factory<ReceiptRepositoryImpl> {
  private final Provider<ReceiptDao> receiptDaoProvider;

  private final Provider<Context> contextProvider;

  public ReceiptRepositoryImpl_Factory(Provider<ReceiptDao> receiptDaoProvider,
      Provider<Context> contextProvider) {
    this.receiptDaoProvider = receiptDaoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ReceiptRepositoryImpl get() {
    return newInstance(receiptDaoProvider.get(), contextProvider.get());
  }

  public static ReceiptRepositoryImpl_Factory create(Provider<ReceiptDao> receiptDaoProvider,
      Provider<Context> contextProvider) {
    return new ReceiptRepositoryImpl_Factory(receiptDaoProvider, contextProvider);
  }

  public static ReceiptRepositoryImpl newInstance(ReceiptDao receiptDao, Context context) {
    return new ReceiptRepositoryImpl(receiptDao, context);
  }
}
