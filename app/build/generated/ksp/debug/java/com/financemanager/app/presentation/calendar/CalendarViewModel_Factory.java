package com.financemanager.app.presentation.calendar;

import com.financemanager.app.domain.repository.TransactionRepository;
import com.financemanager.app.domain.usecase.calendar.GenerateCalendarUseCase;
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
public final class CalendarViewModel_Factory implements Factory<CalendarViewModel> {
  private final Provider<GenerateCalendarUseCase> generateCalendarUseCaseProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public CalendarViewModel_Factory(
      Provider<GenerateCalendarUseCase> generateCalendarUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.generateCalendarUseCaseProvider = generateCalendarUseCaseProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public CalendarViewModel get() {
    return newInstance(generateCalendarUseCaseProvider.get(), transactionRepositoryProvider.get(), sessionManagerProvider.get());
  }

  public static CalendarViewModel_Factory create(
      Provider<GenerateCalendarUseCase> generateCalendarUseCaseProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new CalendarViewModel_Factory(generateCalendarUseCaseProvider, transactionRepositoryProvider, sessionManagerProvider);
  }

  public static CalendarViewModel newInstance(GenerateCalendarUseCase generateCalendarUseCase,
      TransactionRepository transactionRepository, SessionManager sessionManager) {
    return new CalendarViewModel(generateCalendarUseCase, transactionRepository, sessionManager);
  }
}
