package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.BudgetDao;
import com.financemanager.app.data.local.dao.TransactionDao;
import com.financemanager.app.util.CsvGenerator;
import com.financemanager.app.util.PdfGenerator;
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
public final class ReportRepositoryImpl_Factory implements Factory<ReportRepositoryImpl> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<PdfGenerator> pdfGeneratorProvider;

  private final Provider<CsvGenerator> csvGeneratorProvider;

  public ReportRepositoryImpl_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<BudgetDao> budgetDaoProvider, Provider<PdfGenerator> pdfGeneratorProvider,
      Provider<CsvGenerator> csvGeneratorProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.budgetDaoProvider = budgetDaoProvider;
    this.pdfGeneratorProvider = pdfGeneratorProvider;
    this.csvGeneratorProvider = csvGeneratorProvider;
  }

  @Override
  public ReportRepositoryImpl get() {
    return newInstance(transactionDaoProvider.get(), budgetDaoProvider.get(), pdfGeneratorProvider.get(), csvGeneratorProvider.get());
  }

  public static ReportRepositoryImpl_Factory create(Provider<TransactionDao> transactionDaoProvider,
      Provider<BudgetDao> budgetDaoProvider, Provider<PdfGenerator> pdfGeneratorProvider,
      Provider<CsvGenerator> csvGeneratorProvider) {
    return new ReportRepositoryImpl_Factory(transactionDaoProvider, budgetDaoProvider, pdfGeneratorProvider, csvGeneratorProvider);
  }

  public static ReportRepositoryImpl newInstance(TransactionDao transactionDao, BudgetDao budgetDao,
      PdfGenerator pdfGenerator, CsvGenerator csvGenerator) {
    return new ReportRepositoryImpl(transactionDao, budgetDao, pdfGenerator, csvGenerator);
  }
}
