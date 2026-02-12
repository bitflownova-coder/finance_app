package com.financemanager.app.domain.usecase.report;

import com.financemanager.app.domain.repository.ReportRepository;
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
public final class GetBudgetComparisonUseCase_Factory implements Factory<GetBudgetComparisonUseCase> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public GetBudgetComparisonUseCase_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public GetBudgetComparisonUseCase get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static GetBudgetComparisonUseCase_Factory create(
      Provider<ReportRepository> reportRepositoryProvider) {
    return new GetBudgetComparisonUseCase_Factory(reportRepositoryProvider);
  }

  public static GetBudgetComparisonUseCase newInstance(ReportRepository reportRepository) {
    return new GetBudgetComparisonUseCase(reportRepository);
  }
}
