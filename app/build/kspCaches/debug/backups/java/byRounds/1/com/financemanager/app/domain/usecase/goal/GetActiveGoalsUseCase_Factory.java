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
public final class GetActiveGoalsUseCase_Factory implements Factory<GetActiveGoalsUseCase> {
  private final Provider<SavingsGoalRepository> repositoryProvider;

  public GetActiveGoalsUseCase_Factory(Provider<SavingsGoalRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetActiveGoalsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetActiveGoalsUseCase_Factory create(
      Provider<SavingsGoalRepository> repositoryProvider) {
    return new GetActiveGoalsUseCase_Factory(repositoryProvider);
  }

  public static GetActiveGoalsUseCase newInstance(SavingsGoalRepository repository) {
    return new GetActiveGoalsUseCase(repository);
  }
}
