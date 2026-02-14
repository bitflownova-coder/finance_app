package com.financemanager.app.presentation.insights;

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
public final class InsightsFragment_MembersInjector implements MembersInjector<InsightsFragment> {
  private final Provider<SessionManager> sessionManagerProvider;

  public InsightsFragment_MembersInjector(Provider<SessionManager> sessionManagerProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
  }

  public static MembersInjector<InsightsFragment> create(
      Provider<SessionManager> sessionManagerProvider) {
    return new InsightsFragment_MembersInjector(sessionManagerProvider);
  }

  @Override
  public void injectMembers(InsightsFragment instance) {
    injectSessionManager(instance, sessionManagerProvider.get());
  }

  @InjectedFieldSignature("com.financemanager.app.presentation.insights.InsightsFragment.sessionManager")
  public static void injectSessionManager(InsightsFragment instance,
      SessionManager sessionManager) {
    instance.sessionManager = sessionManager;
  }
}
