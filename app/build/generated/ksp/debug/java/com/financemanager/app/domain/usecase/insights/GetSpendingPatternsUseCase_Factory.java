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
public final class GetSpendingPatternsUseCase_Factory implements Factory<GetSpendingPatternsUseCase> {
  private final Provider<InsightRepository> repositoryProvider;

  public GetSpendingPatternsUseCase_Factory(Provider<InsightRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetSpendingPatternsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetSpendingPatternsUseCase_Factory create(
      Provider<InsightRepository> repositoryProvider) {
    return new GetSpendingPatternsUseCase_Factory(repositoryProvider);
  }

  public static GetSpendingPatternsUseCase newInstance(InsightRepository repository) {
    return new GetSpendingPatternsUseCase(repository);
  }
}
