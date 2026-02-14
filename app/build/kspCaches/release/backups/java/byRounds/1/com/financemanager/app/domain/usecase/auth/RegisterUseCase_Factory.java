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
public final class RegisterUseCase_Factory implements Factory<RegisterUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public RegisterUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public RegisterUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static RegisterUseCase_Factory create(Provider<UserRepository> userRepositoryProvider) {
    return new RegisterUseCase_Factory(userRepositoryProvider);
  }

  public static RegisterUseCase newInstance(UserRepository userRepository) {
    return new RegisterUseCase(userRepository);
  }
}
