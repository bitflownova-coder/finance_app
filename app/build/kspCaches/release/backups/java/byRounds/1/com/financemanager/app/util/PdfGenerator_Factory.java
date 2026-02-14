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
public final class PdfGenerator_Factory implements Factory<PdfGenerator> {
  private final Provider<Context> contextProvider;

  public PdfGenerator_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PdfGenerator get() {
    return newInstance(contextProvider.get());
  }

  public static PdfGenerator_Factory create(Provider<Context> contextProvider) {
    return new PdfGenerator_Factory(contextProvider);
  }

  public static PdfGenerator newInstance(Context context) {
    return new PdfGenerator(context);
  }
}
