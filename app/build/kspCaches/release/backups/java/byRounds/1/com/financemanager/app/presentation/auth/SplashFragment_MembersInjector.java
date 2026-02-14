package com.financemanager.app.presentation.auth;

import com.financemanager.app.util.SessionManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SplashFragment_MembersInjector implements MembersInjector<SplashFragment> {
  private final Provider<SessionManager> sessionManagerProvider;

  public SplashFragment_MembersInjector(Provider<SessionManager> sessionManagerProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
  }

  public static MembersInjector<SplashFragment> create(
      Provider<SessionManager> sessionManagerProvider) {
    return new SplashFragment_MembersInjector(sessionManagerProvider);
  }

  @Override
  public void injectMembers(SplashFragment instance) {
    injectSessionManager(instance, sessionManagerProvider.get());
  }

  @InjectedFieldSignature("com.financemanager.app.presentation.auth.SplashFragment.sessionManager")
  public static void injectSessionManager(SplashFragment instance, SessionManager sessionManager) {
    instance.sessionManager = sessionManager;
  }
}
