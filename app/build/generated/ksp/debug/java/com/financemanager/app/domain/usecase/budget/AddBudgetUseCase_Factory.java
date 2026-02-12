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
public final class AddBudgetUseCase_Factory implements Factory<AddBudgetUseCase> {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  public AddBudgetUseCase_Factory(Provider<BudgetRepository> budgetRepositoryProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
  }

  @Override
  public AddBudgetUseCase get() {
    return newInstance(budgetRepositoryProvider.get());
  }

  public static AddBudgetUseCase_Factory create(
      Provider<BudgetRepository> budgetRepositoryProvider) {
    return new AddBudgetUseCase_Factory(budgetRepositoryProvider);
  }

  public static AddBudgetUseCase newInstance(BudgetRepository budgetRepository) {
    return new AddBudgetUseCase(budgetRepository);
  }
}
