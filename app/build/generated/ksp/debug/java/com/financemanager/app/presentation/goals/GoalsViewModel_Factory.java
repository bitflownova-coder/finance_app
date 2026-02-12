package com.financemanager.app.presentation.goals;

import com.financemanager.app.domain.repository.SavingsGoalRepository;
import com.financemanager.app.domain.usecase.goal.AddContributionUseCase;
import com.financemanager.app.domain.usecase.goal.AddGoalUseCase;
import com.financemanager.app.domain.usecase.goal.DeleteGoalUseCase;
import com.financemanager.app.domain.usecase.goal.GetActiveGoalsUseCase;
import com.financemanager.app.domain.usecase.goal.GetGoalStatisticsUseCase;
import com.financemanager.app.domain.usecase.goal.UpdateGoalUseCase;
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
public final class GoalsViewModel_Factory implements Factory<GoalsViewModel> {
  private final Provider<GetActiveGoalsUseCase> getActiveGoalsUseCaseProvider;

  private final Provider<AddGoalUseCase> addGoalUseCaseProvider;

  private final Provider<UpdateGoalUseCase> updateGoalUseCaseProvider;

  private final Provider<DeleteGoalUseCase> deleteGoalUseCaseProvider;

  private final Provider<AddContributionUseCase> addContributionUseCaseProvider;

  private final Provider<GetGoalStatisticsUseCase> getGoalStatisticsUseCaseProvider;

  private final Provider<SavingsGoalRepository> repositoryProvider;

  public GoalsViewModel_Factory(Provider<GetActiveGoalsUseCase> getActiveGoalsUseCaseProvider,
      Provider<AddGoalUseCase> addGoalUseCaseProvider,
      Provider<UpdateGoalUseCase> updateGoalUseCaseProvider,
      Provider<DeleteGoalUseCase> deleteGoalUseCaseProvider,
      Provider<AddContributionUseCase> addContributionUseCaseProvider,
      Provider<GetGoalStatisticsUseCase> getGoalStatisticsUseCaseProvider,
      Provider<SavingsGoalRepository> repositoryProvider) {
    this.getActiveGoalsUseCaseProvider = getActiveGoalsUseCaseProvider;
    this.addGoalUseCaseProvider = addGoalUseCaseProvider;
    this.updateGoalUseCaseProvider = updateGoalUseCaseProvider;
    this.deleteGoalUseCaseProvider = deleteGoalUseCaseProvider;
    this.addContributionUseCaseProvider = addContributionUseCaseProvider;
    this.getGoalStatisticsUseCaseProvider = getGoalStatisticsUseCaseProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GoalsViewModel get() {
    return newInstance(getActiveGoalsUseCaseProvider.get(), addGoalUseCaseProvider.get(), updateGoalUseCaseProvider.get(), deleteGoalUseCaseProvider.get(), addContributionUseCaseProvider.get(), getGoalStatisticsUseCaseProvider.get(), repositoryProvider.get());
  }

  public static GoalsViewModel_Factory create(
      Provider<GetActiveGoalsUseCase> getActiveGoalsUseCaseProvider,
      Provider<AddGoalUseCase> addGoalUseCaseProvider,
      Provider<UpdateGoalUseCase> updateGoalUseCaseProvider,
      Provider<DeleteGoalUseCase> deleteGoalUseCaseProvider,
      Provider<AddContributionUseCase> addContributionUseCaseProvider,
      Provider<GetGoalStatisticsUseCase> getGoalStatisticsUseCaseProvider,
      Provider<SavingsGoalRepository> repositoryProvider) {
    return new GoalsViewModel_Factory(getActiveGoalsUseCaseProvider, addGoalUseCaseProvider, updateGoalUseCaseProvider, deleteGoalUseCaseProvider, addContributionUseCaseProvider, getGoalStatisticsUseCaseProvider, repositoryProvider);
  }

  public static GoalsViewModel newInstance(GetActiveGoalsUseCase getActiveGoalsUseCase,
      AddGoalUseCase addGoalUseCase, UpdateGoalUseCase updateGoalUseCase,
      DeleteGoalUseCase deleteGoalUseCase, AddContributionUseCase addContributionUseCase,
      GetGoalStatisticsUseCase getGoalStatisticsUseCase, SavingsGoalRepository repository) {
    return new GoalsViewModel(getActiveGoalsUseCase, addGoalUseCase, updateGoalUseCase, deleteGoalUseCase, addContributionUseCase, getGoalStatisticsUseCase, repository);
  }
}
