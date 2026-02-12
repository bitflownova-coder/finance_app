package com.financemanager.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.financemanager.app.domain.repository.BudgetRepository;
import com.financemanager.app.domain.repository.SavingsGoalRepository;
import com.financemanager.app.util.NotificationHelper;
import com.financemanager.app.util.SessionManager;
import dagger.internal.DaggerGenerated;
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
public final class NotificationWorker_Factory {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  private final Provider<SavingsGoalRepository> savingsGoalRepositoryProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public NotificationWorker_Factory(Provider<BudgetRepository> budgetRepositoryProvider,
      Provider<SavingsGoalRepository> savingsGoalRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
    this.savingsGoalRepositoryProvider = savingsGoalRepositoryProvider;
    this.sessionManagerProvider = sessionManagerProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public NotificationWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, budgetRepositoryProvider.get(), savingsGoalRepositoryProvider.get(), sessionManagerProvider.get(), notificationHelperProvider.get());
  }

  public static NotificationWorker_Factory create(
      Provider<BudgetRepository> budgetRepositoryProvider,
      Provider<SavingsGoalRepository> savingsGoalRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new NotificationWorker_Factory(budgetRepositoryProvider, savingsGoalRepositoryProvider, sessionManagerProvider, notificationHelperProvider);
  }

  public static NotificationWorker newInstance(Context context, WorkerParameters workerParams,
      BudgetRepository budgetRepository, SavingsGoalRepository savingsGoalRepository,
      SessionManager sessionManager, NotificationHelper notificationHelper) {
    return new NotificationWorker(context, workerParams, budgetRepository, savingsGoalRepository, sessionManager, notificationHelper);
  }
}
