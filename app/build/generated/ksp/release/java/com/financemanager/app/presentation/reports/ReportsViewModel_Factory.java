package com.financemanager.app.presentation.reports;

import com.financemanager.app.domain.usecase.report.GenerateCsvReportUseCase;
import com.financemanager.app.domain.usecase.report.GeneratePdfReportUseCase;
import com.financemanager.app.domain.usecase.report.GetBudgetComparisonUseCase;
import com.financemanager.app.domain.usecase.report.GetTransactionSummaryUseCase;
import com.financemanager.app.util.SessionManager;
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
public final class ReportsViewModel_Factory implements Factory<ReportsViewModel> {
  private final Provider<GetTransactionSummaryUseCase> getTransactionSummaryUseCaseProvider;

  private final Provider<GetBudgetComparisonUseCase> getBudgetComparisonUseCaseProvider;

  private final Provider<GeneratePdfReportUseCase> generatePdfReportUseCaseProvider;

  private final Provider<GenerateCsvReportUseCase> generateCsvReportUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public ReportsViewModel_Factory(
      Provider<GetTransactionSummaryUseCase> getTransactionSummaryUseCaseProvider,
      Provider<GetBudgetComparisonUseCase> getBudgetComparisonUseCaseProvider,
      Provider<GeneratePdfReportUseCase> generatePdfReportUseCaseProvider,
      Provider<GenerateCsvReportUseCase> generateCsvReportUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.getTransactionSummaryUseCaseProvider = getTransactionSummaryUseCaseProvider;
    this.getBudgetComparisonUseCaseProvider = getBudgetComparisonUseCaseProvider;
    this.generatePdfReportUseCaseProvider = generatePdfReportUseCaseProvider;
    this.generateCsvReportUseCaseProvider = generateCsvReportUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public ReportsViewModel get() {
    return newInstance(getTransactionSummaryUseCaseProvider.get(), getBudgetComparisonUseCaseProvider.get(), generatePdfReportUseCaseProvider.get(), generateCsvReportUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static ReportsViewModel_Factory create(
      Provider<GetTransactionSummaryUseCase> getTransactionSummaryUseCaseProvider,
      Provider<GetBudgetComparisonUseCase> getBudgetComparisonUseCaseProvider,
      Provider<GeneratePdfReportUseCase> generatePdfReportUseCaseProvider,
      Provider<GenerateCsvReportUseCase> generateCsvReportUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new ReportsViewModel_Factory(getTransactionSummaryUseCaseProvider, getBudgetComparisonUseCaseProvider, generatePdfReportUseCaseProvider, generateCsvReportUseCaseProvider, sessionManagerProvider);
  }

  public static ReportsViewModel newInstance(
      GetTransactionSummaryUseCase getTransactionSummaryUseCase,
      GetBudgetComparisonUseCase getBudgetComparisonUseCase,
      GeneratePdfReportUseCase generatePdfReportUseCase,
      GenerateCsvReportUseCase generateCsvReportUseCase, SessionManager sessionManager) {
    return new ReportsViewModel(getTransactionSummaryUseCase, getBudgetComparisonUseCase, generatePdfReportUseCase, generateCsvReportUseCase, sessionManager);
  }
}
