package com.financemanager.app.presentation.goals;

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
public final class GoalsFragment_MembersInjector implements MembersInjector<GoalsFragment> {
  private final Provider<SessionManager> sessionManagerProvider;

  public GoalsFragment_MembersInjector(Provider<SessionManager> sessionManagerProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
  }

  public static MembersInjector<GoalsFragment> create(
      Provider<SessionManager> sessionManagerProvider) {
    return new GoalsFragment_MembersInjector(sessionManagerProvider);
  }

  @Override
  public void injectMembers(GoalsFragment instance) {
    injectSessionManager(instance, sessionManagerProvider.get());
  }

  @InjectedFieldSignature("com.financemanager.app.presentation.goals.GoalsFragment.sessionManager")
  public static void injectSessionManager(GoalsFragment instance, SessionManager sessionManager) {
    instance.sessionManager = sessionManager;
  }
}
