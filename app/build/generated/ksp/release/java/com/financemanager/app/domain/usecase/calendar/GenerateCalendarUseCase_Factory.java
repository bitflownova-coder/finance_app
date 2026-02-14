package com.financemanager.app.domain.usecase.calendar;

import com.financemanager.app.domain.repository.TransactionRepository;
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
public final class GenerateCalendarUseCase_Factory implements Factory<GenerateCalendarUseCase> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public GenerateCalendarUseCase_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public GenerateCalendarUseCase get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static GenerateCalendarUseCase_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new GenerateCalendarUseCase_Factory(transactionRepositoryProvider);
  }

  public static GenerateCalendarUseCase newInstance(TransactionRepository transactionRepository) {
    return new GenerateCalendarUseCase(transactionRepository);
  }
}
