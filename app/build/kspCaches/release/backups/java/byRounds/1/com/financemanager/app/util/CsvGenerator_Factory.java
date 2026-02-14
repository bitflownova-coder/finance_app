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
public final class CsvGenerator_Factory implements Factory<CsvGenerator> {
  private final Provider<Context> contextProvider;

  public CsvGenerator_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CsvGenerator get() {
    return newInstance(contextProvider.get());
  }

  public static CsvGenerator_Factory create(Provider<Context> contextProvider) {
    return new CsvGenerator_Factory(contextProvider);
  }

  public static CsvGenerator newInstance(Context context) {
    return new CsvGenerator(context);
  }
}
