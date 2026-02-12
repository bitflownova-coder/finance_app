package com.financemanager.app.data.mapper;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
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
public final class SplitBillMapper_Factory implements Factory<SplitBillMapper> {
  @Override
  public SplitBillMapper get() {
    return newInstance();
  }

  public static SplitBillMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SplitBillMapper newInstance() {
    return new SplitBillMapper();
  }

  private static final class InstanceHolder {
    private static final SplitBillMapper_Factory INSTANCE = new SplitBillMapper_Factory();
  }
}
