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
public final class SetupPinUseCase_Factory implements Factory<SetupPinUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public SetupPinUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public SetupPinUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static SetupPinUseCase_Factory create(Provider<UserRepository> userRepositoryProvider) {
    return new SetupPinUseCase_Factory(userRepositoryProvider);
  }

  public static SetupPinUseCase newInstance(UserRepository userRepository) {
    return new SetupPinUseCase(userRepository);
  }
}
