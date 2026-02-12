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
public final class UpdateGoalUseCase_Factory implements Factory<UpdateGoalUseCase> {
  private final Provider<SavingsGoalRepository> repositoryProvider;

  public UpdateGoalUseCase_Factory(Provider<SavingsGoalRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateGoalUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateGoalUseCase_Factory create(
      Provider<SavingsGoalRepository> repositoryProvider) {
    return new UpdateGoalUseCase_Factory(repositoryProvider);
  }

  public static UpdateGoalUseCase newInstance(SavingsGoalRepository repository) {
    return new UpdateGoalUseCase(repository);
  }
}
