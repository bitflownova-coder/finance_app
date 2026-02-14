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
public final class GetCategoryInsightsUseCase_Factory implements Factory<GetCategoryInsightsUseCase> {
  private final Provider<InsightRepository> repositoryProvider;

  public GetCategoryInsightsUseCase_Factory(Provider<InsightRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetCategoryInsightsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetCategoryInsightsUseCase_Factory create(
      Provider<InsightRepository> repositoryProvider) {
    return new GetCategoryInsightsUseCase_Factory(repositoryProvider);
  }

  public static GetCategoryInsightsUseCase newInstance(InsightRepository repository) {
    return new GetCategoryInsightsUseCase(repository);
  }
}
