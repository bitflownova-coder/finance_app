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
public final class GetBudgetsUseCase_Factory implements Factory<GetBudgetsUseCase> {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  public GetBudgetsUseCase_Factory(Provider<BudgetRepository> budgetRepositoryProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
  }

  @Override
  public GetBudgetsUseCase get() {
    return newInstance(budgetRepositoryProvider.get());
  }

  public static GetBudgetsUseCase_Factory create(
      Provider<BudgetRepository> budgetRepositoryProvider) {
    return new GetBudgetsUseCase_Factory(budgetRepositoryProvider);
  }

  public static GetBudgetsUseCase newInstance(BudgetRepository budgetRepository) {
    return new GetBudgetsUseCase(budgetRepository);
  }
}
