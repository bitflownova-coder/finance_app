package com.financemanager.app.domain.usecase.budget;

import com.financemanager.app.domain.repository.BudgetRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class UpdateBudgetUseCase_Factory implements Factory<UpdateBudgetUseCase> {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  public UpdateBudgetUseCase_Factory(Provider<BudgetRepository> budgetRepositoryProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
  }

  @Override
  public UpdateBudgetUseCase get() {
    return newInstance(budgetRepositoryProvider.get());
  }

  public static UpdateBudgetUseCase_Factory create(
      Provider<BudgetRepository> budgetRepositoryProvider) {
    return new UpdateBudgetUseCase_Factory(budgetRepositoryProvider);
  }

  public static UpdateBudgetUseCase newInstance(BudgetRepository budgetRepository) {
    return new UpdateBudgetUseCase(budgetRepository);
  }
}
