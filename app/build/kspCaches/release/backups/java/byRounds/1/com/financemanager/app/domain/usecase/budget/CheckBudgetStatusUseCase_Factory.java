package com.financemanager.app.domain.usecase.budget;

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
public final class CheckBudgetStatusUseCase_Factory implements Factory<CheckBudgetStatusUseCase> {
  @Override
  public CheckBudgetStatusUseCase get() {
    return newInstance();
  }

  public static CheckBudgetStatusUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CheckBudgetStatusUseCase newInstance() {
    return new CheckBudgetStatusUseCase();
  }

  private static final class InstanceHolder {
    private static final CheckBudgetStatusUseCase_Factory INSTANCE = new CheckBudgetStatusUseCase_Factory();
  }
}
