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
public final class GenerateCsvReportUseCase_Factory implements Factory<GenerateCsvReportUseCase> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public GenerateCsvReportUseCase_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public GenerateCsvReportUseCase get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static GenerateCsvReportUseCase_Factory create(
      Provider<ReportRepository> reportRepositoryProvider) {
    return new GenerateCsvReportUseCase_Factory(reportRepositoryProvider);
  }

  public static GenerateCsvReportUseCase newInstance(ReportRepository reportRepository) {
    return new GenerateCsvReportUseCase(reportRepository);
  }
}
