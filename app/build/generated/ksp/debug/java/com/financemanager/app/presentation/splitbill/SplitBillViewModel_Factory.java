package com.financemanager.app.presentation.splitbill;

import com.financemanager.app.domain.repository.SplitBillRepository;
import com.financemanager.app.domain.usecase.AddSplitBillUseCase;
import com.financemanager.app.domain.usecase.GetSplitBillsUseCase;
import com.financemanager.app.domain.usecase.MarkParticipantPaidUseCase;
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
public final class SplitBillViewModel_Factory implements Factory<SplitBillViewModel> {
  private final Provider<GetSplitBillsUseCase> getSplitBillsUseCaseProvider;

  private final Provider<AddSplitBillUseCase> addSplitBillUseCaseProvider;

  private final Provider<MarkParticipantPaidUseCase> markParticipantPaidUseCaseProvider;

  private final Provider<SplitBillRepository> repositoryProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public SplitBillViewModel_Factory(Provider<GetSplitBillsUseCase> getSplitBillsUseCaseProvider,
      Provider<AddSplitBillUseCase> addSplitBillUseCaseProvider,
      Provider<MarkParticipantPaidUseCase> markParticipantPaidUseCaseProvider,
      Provider<SplitBillRepository> repositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.getSplitBillsUseCaseProvider = getSplitBillsUseCaseProvider;
    this.addSplitBillUseCaseProvider = addSplitBillUseCaseProvider;
    this.markParticipantPaidUseCaseProvider = markParticipantPaidUseCaseProvider;
    this.repositoryProvider = repositoryProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public SplitBillViewModel get() {
    return newInstance(getSplitBillsUseCaseProvider.get(), addSplitBillUseCaseProvider.get(), markParticipantPaidUseCaseProvider.get(), repositoryProvider.get(), sessionManagerProvider.get());
  }

  public static SplitBillViewModel_Factory create(
      Provider<GetSplitBillsUseCase> getSplitBillsUseCaseProvider,
      Provider<AddSplitBillUseCase> addSplitBillUseCaseProvider,
      Provider<MarkParticipantPaidUseCase> markParticipantPaidUseCaseProvider,
      Provider<SplitBillRepository> repositoryProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new SplitBillViewModel_Factory(getSplitBillsUseCaseProvider, addSplitBillUseCaseProvider, markParticipantPaidUseCaseProvider, repositoryProvider, sessionManagerProvider);
  }

  public static SplitBillViewModel newInstance(GetSplitBillsUseCase getSplitBillsUseCase,
      AddSplitBillUseCase addSplitBillUseCase,
      MarkParticipantPaidUseCase markParticipantPaidUseCase, SplitBillRepository repository,
      SessionManager sessionManager) {
    return new SplitBillViewModel(getSplitBillsUseCase, addSplitBillUseCase, markParticipantPaidUseCase, repository, sessionManager);
  }
}
