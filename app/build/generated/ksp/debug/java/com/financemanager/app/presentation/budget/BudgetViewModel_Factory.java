package com.financemanager.app.presentation.budget;

import com.financemanager.app.domain.usecase.budget.AddBudgetUseCase;
import com.financemanager.app.domain.usecase.budget.CheckBudgetStatusUseCase;
import com.financemanager.app.domain.usecase.budget.DeleteBudgetUseCase;
import com.financemanager.app.domain.usecase.budget.GetBudgetsUseCase;
import com.financemanager.app.domain.usecase.budget.UpdateBudgetUseCase;
import com.financemanager.app.util.SessionManager;
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
public final class BudgetViewModel_Factory implements Factory<BudgetViewModel> {
  private final Provider<GetBudgetsUseCase> getBudgetsUseCaseProvider;

  private final Provider<AddBudgetUseCase> addBudgetUseCaseProvider;

  private final Provider<UpdateBudgetUseCase> updateBudgetUseCaseProvider;

  private final Provider<DeleteBudgetUseCase> deleteBudgetUseCaseProvider;

  private final Provider<CheckBudgetStatusUseCase> checkBudgetStatusUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public BudgetViewModel_Factory(Provider<GetBudgetsUseCase> getBudgetsUseCaseProvider,
      Provider<AddBudgetUseCase> addBudgetUseCaseProvider,
      Provider<UpdateBudgetUseCase> updateBudgetUseCaseProvider,
      Provider<DeleteBudgetUseCase> deleteBudgetUseCaseProvider,
      Provider<CheckBudgetStatusUseCase> checkBudgetStatusUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.getBudgetsUseCaseProvider = getBudgetsUseCaseProvider;
    this.addBudgetUseCaseProvider = addBudgetUseCaseProvider;
    this.updateBudgetUseCaseProvider = updateBudgetUseCaseProvider;
    this.deleteBudgetUseCaseProvider = deleteBudgetUseCaseProvider;
    this.checkBudgetStatusUseCaseProvider = checkBudgetStatusUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public BudgetViewModel get() {
    return newInstance(getBudgetsUseCaseProvider.get(), addBudgetUseCaseProvider.get(), updateBudgetUseCaseProvider.get(), deleteBudgetUseCaseProvider.get(), checkBudgetStatusUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static BudgetViewModel_Factory create(
      Provider<GetBudgetsUseCase> getBudgetsUseCaseProvider,
      Provider<AddBudgetUseCase> addBudgetUseCaseProvider,
      Provider<UpdateBudgetUseCase> updateBudgetUseCaseProvider,
      Provider<DeleteBudgetUseCase> deleteBudgetUseCaseProvider,
      Provider<CheckBudgetStatusUseCase> checkBudgetStatusUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new BudgetViewModel_Factory(getBudgetsUseCaseProvider, addBudgetUseCaseProvider, updateBudgetUseCaseProvider, deleteBudgetUseCaseProvider, checkBudgetStatusUseCaseProvider, sessionManagerProvider);
  }

  public static BudgetViewModel newInstance(GetBudgetsUseCase getBudgetsUseCase,
      AddBudgetUseCase addBudgetUseCase, UpdateBudgetUseCase updateBudgetUseCase,
      DeleteBudgetUseCase deleteBudgetUseCase, CheckBudgetStatusUseCase checkBudgetStatusUseCase,
      SessionManager sessionManager) {
    return new BudgetViewModel(getBudgetsUseCase, addBudgetUseCase, updateBudgetUseCase, deleteBudgetUseCase, checkBudgetStatusUseCase, sessionManager);
  }
}
