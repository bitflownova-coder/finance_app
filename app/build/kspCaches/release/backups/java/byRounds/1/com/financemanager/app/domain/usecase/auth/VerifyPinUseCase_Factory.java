package com.financemanager.app.domain.usecase.auth;

import com.financemanager.app.domain.repository.UserRepository;
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
public final class VerifyPinUseCase_Factory implements Factory<VerifyPinUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public VerifyPinUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public VerifyPinUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static VerifyPinUseCase_Factory create(Provider<UserRepository> userRepositoryProvider) {
    return new VerifyPinUseCase_Factory(userRepositoryProvider);
  }

  public static VerifyPinUseCase newInstance(UserRepository userRepository) {
    return new VerifyPinUseCase(userRepository);
  }
}
