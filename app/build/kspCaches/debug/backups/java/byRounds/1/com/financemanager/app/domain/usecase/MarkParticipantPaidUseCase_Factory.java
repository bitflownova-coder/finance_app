package com.financemanager.app.domain.usecase;

import com.financemanager.app.domain.repository.SplitBillRepository;
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
public final class MarkParticipantPaidUseCase_Factory implements Factory<MarkParticipantPaidUseCase> {
  private final Provider<SplitBillRepository> repositoryProvider;

  public MarkParticipantPaidUseCase_Factory(Provider<SplitBillRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MarkParticipantPaidUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static MarkParticipantPaidUseCase_Factory create(
      Provider<SplitBillRepository> repositoryProvider) {
    return new MarkParticipantPaidUseCase_Factory(repositoryProvider);
  }

  public static MarkParticipantPaidUseCase newInstance(SplitBillRepository repository) {
    return new MarkParticipantPaidUseCase(repository);
  }
}
