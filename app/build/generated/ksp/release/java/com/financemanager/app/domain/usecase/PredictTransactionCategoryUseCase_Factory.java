package com.financemanager.app.domain.usecase;

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
public final class PredictTransactionCategoryUseCase_Factory implements Factory<PredictTransactionCategoryUseCase> {
  @Override
  public PredictTransactionCategoryUseCase get() {
    return newInstance();
  }

  public static PredictTransactionCategoryUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PredictTransactionCategoryUseCase newInstance() {
    return new PredictTransactionCategoryUseCase();
  }

  private static final class InstanceHolder {
    private static final PredictTransactionCategoryUseCase_Factory INSTANCE = new PredictTransactionCategoryUseCase_Factory();
  }
}
