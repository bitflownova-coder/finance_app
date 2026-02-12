package com.financemanager.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.financemanager.app.domain.usecase.recurring.ProcessRecurringTransactionsUseCase;
import dagger.internal.DaggerGenerated;
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
public final class RecurringTransactionWorker_Factory {
  private final Provider<ProcessRecurringTransactionsUseCase> processRecurringTransactionsUseCaseProvider;

  public RecurringTransactionWorker_Factory(
      Provider<ProcessRecurringTransactionsUseCase> processRecurringTransactionsUseCaseProvider) {
    this.processRecurringTransactionsUseCaseProvider = processRecurringTransactionsUseCaseProvider;
  }

  public RecurringTransactionWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, processRecurringTransactionsUseCaseProvider.get());
  }

  public static RecurringTransactionWorker_Factory create(
      Provider<ProcessRecurringTransactionsUseCase> processRecurringTransactionsUseCaseProvider) {
    return new RecurringTransactionWorker_Factory(processRecurringTransactionsUseCaseProvider);
  }

  public static RecurringTransactionWorker newInstance(Context context,
      WorkerParameters workerParams,
      ProcessRecurringTransactionsUseCase processRecurringTransactionsUseCase) {
    return new RecurringTransactionWorker(context, workerParams, processRecurringTransactionsUseCase);
  }
}
