package com.financemanager.app.presentation.recurring;

import com.financemanager.app.domain.usecase.recurring.AddRecurringTransactionUseCase;
import com.financemanager.app.domain.usecase.recurring.DeleteRecurringTransactionUseCase;
import com.financemanager.app.domain.usecase.recurring.GetRecurringTransactionsUseCase;
import com.financemanager.app.domain.usecase.recurring.ProcessRecurringTransactionsUseCase;
import com.financemanager.app.domain.usecase.recurring.ToggleRecurringActiveUseCase;
import com.financemanager.app.domain.usecase.recurring.UpdateRecurringTransactionUseCase;
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
public final class RecurringTransactionViewModel_Factory implements Factory<RecurringTransactionViewModel> {
  private final Provider<GetRecurringTransactionsUseCase> getRecurringTransactionsUseCaseProvider;

  private final Provider<AddRecurringTransactionUseCase> addRecurringTransactionUseCaseProvider;

  private final Provider<UpdateRecurringTransactionUseCase> updateRecurringTransactionUseCaseProvider;

  private final Provider<DeleteRecurringTransactionUseCase> deleteRecurringTransactionUseCaseProvider;

  private final Provider<ToggleRecurringActiveUseCase> toggleRecurringActiveUseCaseProvider;

  private final Provider<ProcessRecurringTransactionsUseCase> processRecurringTransactionsUseCaseProvider;

  public RecurringTransactionViewModel_Factory(
      Provider<GetRecurringTransactionsUseCase> getRecurringTransactionsUseCaseProvider,
      Provider<AddRecurringTransactionUseCase> addRecurringTransactionUseCaseProvider,
      Provider<UpdateRecurringTransactionUseCase> updateRecurringTransactionUseCaseProvider,
      Provider<DeleteRecurringTransactionUseCase> deleteRecurringTransactionUseCaseProvider,
      Provider<ToggleRecurringActiveUseCase> toggleRecurringActiveUseCaseProvider,
      Provider<ProcessRecurringTransactionsUseCase> processRecurringTransactionsUseCaseProvider) {
    this.getRecurringTransactionsUseCaseProvider = getRecurringTransactionsUseCaseProvider;
    this.addRecurringTransactionUseCaseProvider = addRecurringTransactionUseCaseProvider;
    this.updateRecurringTransactionUseCaseProvider = updateRecurringTransactionUseCaseProvider;
    this.deleteRecurringTransactionUseCaseProvider = deleteRecurringTransactionUseCaseProvider;
    this.toggleRecurringActiveUseCaseProvider = toggleRecurringActiveUseCaseProvider;
    this.processRecurringTransactionsUseCaseProvider = processRecurringTransactionsUseCaseProvider;
  }

  @Override
  public RecurringTransactionViewModel get() {
    return newInstance(getRecurringTransactionsUseCaseProvider.get(), addRecurringTransactionUseCaseProvider.get(), updateRecurringTransactionUseCaseProvider.get(), deleteRecurringTransactionUseCaseProvider.get(), toggleRecurringActiveUseCaseProvider.get(), processRecurringTransactionsUseCaseProvider.get());
  }

  public static RecurringTransactionViewModel_Factory create(
      Provider<GetRecurringTransactionsUseCase> getRecurringTransactionsUseCaseProvider,
      Provider<AddRecurringTransactionUseCase> addRecurringTransactionUseCaseProvider,
      Provider<UpdateRecurringTransactionUseCase> updateRecurringTransactionUseCaseProvider,
      Provider<DeleteRecurringTransactionUseCase> deleteRecurringTransactionUseCaseProvider,
      Provider<ToggleRecurringActiveUseCase> toggleRecurringActiveUseCaseProvider,
      Provider<ProcessRecurringTransactionsUseCase> processRecurringTransactionsUseCaseProvider) {
    return new RecurringTransactionViewModel_Factory(getRecurringTransactionsUseCaseProvider, addRecurringTransactionUseCaseProvider, updateRecurringTransactionUseCaseProvider, deleteRecurringTransactionUseCaseProvider, toggleRecurringActiveUseCaseProvider, processRecurringTransactionsUseCaseProvider);
  }

  public static RecurringTransactionViewModel newInstance(
      GetRecurringTransactionsUseCase getRecurringTransactionsUseCase,
      AddRecurringTransactionUseCase addRecurringTransactionUseCase,
      UpdateRecurringTransactionUseCase updateRecurringTransactionUseCase,
      DeleteRecurringTransactionUseCase deleteRecurringTransactionUseCase,
      ToggleRecurringActiveUseCase toggleRecurringActiveUseCase,
      ProcessRecurringTransactionsUseCase processRecurringTransactionsUseCase) {
    return new RecurringTransactionViewModel(getRecurringTransactionsUseCase, addRecurringTransactionUseCase, updateRecurringTransactionUseCase, deleteRecurringTransactionUseCase, toggleRecurringActiveUseCase, processRecurringTransactionsUseCase);
  }
}
