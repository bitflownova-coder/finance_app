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
public final class AddContributionUseCase_Factory implements Factory<AddContributionUseCase> {
  private final Provider<SavingsGoalRepository> repositoryProvider;

  public AddContributionUseCase_Factory(Provider<SavingsGoalRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddContributionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddContributionUseCase_Factory create(
      Provider<SavingsGoalRepository> repositoryProvider) {
    return new AddContributionUseCase_Factory(repositoryProvider);
  }

  public static AddContributionUseCase newInstance(SavingsGoalRepository repository) {
    return new AddContributionUseCase(repository);
  }
}
