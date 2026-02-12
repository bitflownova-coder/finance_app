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
public final class GeneratePdfReportUseCase_Factory implements Factory<GeneratePdfReportUseCase> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public GeneratePdfReportUseCase_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public GeneratePdfReportUseCase get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static GeneratePdfReportUseCase_Factory create(
      Provider<ReportRepository> reportRepositoryProvider) {
    return new GeneratePdfReportUseCase_Factory(reportRepositoryProvider);
  }

  public static GeneratePdfReportUseCase newInstance(ReportRepository reportRepository) {
    return new GeneratePdfReportUseCase(reportRepository);
  }
}
