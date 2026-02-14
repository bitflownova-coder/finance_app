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
public final class DeleteGoalUseCase_Factory implements Factory<DeleteGoalUseCase> {
  private final Provider<SavingsGoalRepository> repositoryProvider;

  public DeleteGoalUseCase_Factory(Provider<SavingsGoalRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteGoalUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteGoalUseCase_Factory create(
      Provider<SavingsGoalRepository> repositoryProvider) {
    return new DeleteGoalUseCase_Factory(repositoryProvider);
  }

  public static DeleteGoalUseCase newInstance(SavingsGoalRepository repository) {
    return new DeleteGoalUseCase(repository);
  }
}
