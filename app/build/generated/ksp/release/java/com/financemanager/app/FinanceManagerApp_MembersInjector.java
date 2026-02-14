package com.financemanager.app;

import androidx.hilt.work.HiltWorkerFactory;
import com.financemanager.app.util.NotificationScheduler;
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
public final class FinanceManagerApp_MembersInjector implements MembersInjector<FinanceManagerApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public FinanceManagerApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  public static MembersInjector<FinanceManagerApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new FinanceManagerApp_MembersInjector(workerFactoryProvider, notificationSchedulerProvider);
  }

  @Override
  public void injectMembers(FinanceManagerApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectNotificationScheduler(instance, notificationSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.financemanager.app.FinanceManagerApp.workerFactory")
  public static void injectWorkerFactory(FinanceManagerApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.financemanager.app.FinanceManagerApp.notificationScheduler")
  public static void injectNotificationScheduler(FinanceManagerApp instance,
      NotificationScheduler notificationScheduler) {
    instance.notificationScheduler = notificationScheduler;
  }
}
