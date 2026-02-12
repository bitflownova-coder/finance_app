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
public final class DeleteBudgetUseCase_Factory implements Factory<DeleteBudgetUseCase> {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  public DeleteBudgetUseCase_Factory(Provider<BudgetRepository> budgetRepositoryProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
  }

  @Override
  public DeleteBudgetUseCase get() {
    return newInstance(budgetRepositoryProvider.get());
  }

  public static DeleteBudgetUseCase_Factory create(
      Provider<BudgetRepository> budgetRepositoryProvider) {
    return new DeleteBudgetUseCase_Factory(budgetRepositoryProvider);
  }

  public static DeleteBudgetUseCase newInstance(BudgetRepository budgetRepository) {
    return new DeleteBudgetUseCase(budgetRepository);
  }
}
