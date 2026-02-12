package com.financemanager.app.presentation.recurring;

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
public final class RecurringTransactionFragment_MembersInjector implements MembersInjector<RecurringTransactionFragment> {
  private final Provider<SessionManager> sessionManagerProvider;

  public RecurringTransactionFragment_MembersInjector(
      Provider<SessionManager> sessionManagerProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
  }

  public static MembersInjector<RecurringTransactionFragment> create(
      Provider<SessionManager> sessionManagerProvider) {
    return new RecurringTransactionFragment_MembersInjector(sessionManagerProvider);
  }

  @Override
  public void injectMembers(RecurringTransactionFragment instance) {
    injectSessionManager(instance, sessionManagerProvider.get());
  }

  @InjectedFieldSignature("com.financemanager.app.presentation.recurring.RecurringTransactionFragment.sessionManager")
  public static void injectSessionManager(RecurringTransactionFragment instance,
      SessionManager sessionManager) {
    instance.sessionManager = sessionManager;
  }
}
