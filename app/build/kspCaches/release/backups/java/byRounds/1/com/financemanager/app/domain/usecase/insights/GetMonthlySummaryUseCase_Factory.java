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
public final class GetMonthlySummaryUseCase_Factory implements Factory<GetMonthlySummaryUseCase> {
  private final Provider<InsightRepository> repositoryProvider;

  public GetMonthlySummaryUseCase_Factory(Provider<InsightRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetMonthlySummaryUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetMonthlySummaryUseCase_Factory create(
      Provider<InsightRepository> repositoryProvider) {
    return new GetMonthlySummaryUseCase_Factory(repositoryProvider);
  }

  public static GetMonthlySummaryUseCase newInstance(InsightRepository repository) {
    return new GetMonthlySummaryUseCase(repository);
  }
}
