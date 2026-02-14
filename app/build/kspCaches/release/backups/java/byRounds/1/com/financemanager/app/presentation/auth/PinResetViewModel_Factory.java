package com.financemanager.app.presentation.auth;

import com.financemanager.app.domain.usecase.UpdateUserPinUseCase;
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
public final class PinResetViewModel_Factory implements Factory<PinResetViewModel> {
  private final Provider<UpdateUserPinUseCase> updateUserPinUseCaseProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public PinResetViewModel_Factory(Provider<UpdateUserPinUseCase> updateUserPinUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.updateUserPinUseCaseProvider = updateUserPinUseCaseProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public PinResetViewModel get() {
    return newInstance(updateUserPinUseCaseProvider.get(), sessionManagerProvider.get());
  }

  public static PinResetViewModel_Factory create(
      Provider<UpdateUserPinUseCase> updateUserPinUseCaseProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new PinResetViewModel_Factory(updateUserPinUseCaseProvider, sessionManagerProvider);
  }

  public static PinResetViewModel newInstance(UpdateUserPinUseCase updateUserPinUseCase,
      SessionManager sessionManager) {
    return new PinResetViewModel(updateUserPinUseCase, sessionManager);
  }
}
