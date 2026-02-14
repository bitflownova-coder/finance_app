package com.financemanager.app.domain.usecase.insights;

import com.financemanager.app.domain.repository.InsightRepository;
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
public final class PredictExpensesUseCase_Factory implements Factory<PredictExpensesUseCase> {
  private final Provider<InsightRepository> repositoryProvider;

  public PredictExpensesUseCase_Factory(Provider<InsightRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PredictExpensesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static PredictExpensesUseCase_Factory create(
      Provider<InsightRepository> repositoryProvider) {
    return new PredictExpensesUseCase_Factory(repositoryProvider);
  }

  public static PredictExpensesUseCase newInstance(InsightRepository repository) {
    return new PredictExpensesUseCase(repository);
  }
}
