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
public final class GetSplitBillsUseCase_Factory implements Factory<GetSplitBillsUseCase> {
  private final Provider<SplitBillRepository> repositoryProvider;

  public GetSplitBillsUseCase_Factory(Provider<SplitBillRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetSplitBillsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetSplitBillsUseCase_Factory create(
      Provider<SplitBillRepository> repositoryProvider) {
    return new GetSplitBillsUseCase_Factory(repositoryProvider);
  }

  public static GetSplitBillsUseCase newInstance(SplitBillRepository repository) {
    return new GetSplitBillsUseCase(repository);
  }
}
