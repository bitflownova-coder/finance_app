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
public final class GenerateInsightsUseCase_Factory implements Factory<GenerateInsightsUseCase> {
  private final Provider<InsightRepository> repositoryProvider;

  public GenerateInsightsUseCase_Factory(Provider<InsightRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GenerateInsightsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GenerateInsightsUseCase_Factory create(
      Provider<InsightRepository> repositoryProvider) {
    return new GenerateInsightsUseCase_Factory(repositoryProvider);
  }

  public static GenerateInsightsUseCase newInstance(InsightRepository repository) {
    return new GenerateInsightsUseCase(repository);
  }
}
