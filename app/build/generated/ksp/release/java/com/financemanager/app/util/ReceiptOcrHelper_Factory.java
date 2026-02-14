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

  private final Provider<ImageStorageHelper> imageStorageHelperProvider;

  public ReceiptOcrHelper_Factory(Provider<Context> contextProvider,
      Provider<ImageStorageHelper> imageStorageHelperProvider) {
    this.contextProvider = contextProvider;
    this.imageStorageHelperProvider = imageStorageHelperProvider;
  }

  @Override
  public ReceiptOcrHelper get() {
    return newInstance(contextProvider.get(), imageStorageHelperProvider.get());
  }

  public static ReceiptOcrHelper_Factory create(Provider<Context> contextProvider,
      Provider<ImageStorageHelper> imageStorageHelperProvider) {
    return new ReceiptOcrHelper_Factory(contextProvider, imageStorageHelperProvider);
  }

  public static ReceiptOcrHelper newInstance(Context context,
      ImageStorageHelper imageStorageHelper) {
    return new ReceiptOcrHelper(context, imageStorageHelper);
  }
}
