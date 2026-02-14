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
public final class GetTransactionSummaryUseCase_Factory implements Factory<GetTransactionSummaryUseCase> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public GetTransactionSummaryUseCase_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public GetTransactionSummaryUseCase get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static GetTransactionSummaryUseCase_Factory create(
      Provider<ReportRepository> reportRepositoryProvider) {
    return new GetTransactionSummaryUseCase_Factory(reportRepositoryProvider);
  }

  public static GetTransactionSummaryUseCase newInstance(ReportRepository reportRepository) {
    return new GetTransactionSummaryUseCase(reportRepository);
  }
}
