package com.financemanager.app.domain.usecase.goal;

import com.financemanager.app.domain.repository.SavingsGoalRepository;
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
public final class GetGoalStatisticsUseCase_Factory implements Factory<GetGoalStatisticsUseCase> {
  private final Provider<SavingsGoalRepository> repositoryProvider;

  public GetGoalStatisticsUseCase_Factory(Provider<SavingsGoalRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetGoalStatisticsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetGoalStatisticsUseCase_Factory create(
      Provider<SavingsGoalRepository> repositoryProvider) {
    return new GetGoalStatisticsUseCase_Factory(repositoryProvider);
  }

  public static GetGoalStatisticsUseCase newInstance(SavingsGoalRepository repository) {
    return new GetGoalStatisticsUseCase(repository);
  }
}
