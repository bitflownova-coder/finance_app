package com.financemanager.app.util;

import android.content.Context;
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
public final class ReceiptOcrHelper_Factory implements Factory<ReceiptOcrHelper> {
  private final Provider<Context> contextProvider;

  public ReceiptOcrHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ReceiptOcrHelper get() {
    return newInstance(contextProvider.get());
  }

  public static ReceiptOcrHelper_Factory create(Provider<Context> contextProvider) {
    return new ReceiptOcrHelper_Factory(contextProvider);
  }

  public static ReceiptOcrHelper newInstance(Context context) {
    return new ReceiptOcrHelper(context);
  }
}
