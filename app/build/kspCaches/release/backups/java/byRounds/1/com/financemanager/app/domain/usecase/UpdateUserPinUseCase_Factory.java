package com.financemanager.app.domain.usecase;

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
public final class UpdateUserPinUseCase_Factory implements Factory<UpdateUserPinUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  public UpdateUserPinUseCase_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public UpdateUserPinUseCase get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static UpdateUserPinUseCase_Factory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new UpdateUserPinUseCase_Factory(userRepositoryProvider);
  }

  public static UpdateUserPinUseCase newInstance(UserRepository userRepository) {
    return new UpdateUserPinUseCase(userRepository);
  }
}
