package com.financemanager.app.domain.usecase.notification;

import com.financemanager.app.util.NotificationHelper;
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
public final class CheckGoalNotificationUseCase_Factory implements Factory<CheckGoalNotificationUseCase> {
  private final Provider<NotificationHelper> notificationHelperProvider;

  public CheckGoalNotificationUseCase_Factory(
      Provider<NotificationHelper> notificationHelperProvider) {
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public CheckGoalNotificationUseCase get() {
    return newInstance(notificationHelperProvider.get());
  }

  public static CheckGoalNotificationUseCase_Factory create(
      Provider<NotificationHelper> notificationHelperProvider) {
    return new CheckGoalNotificationUseCase_Factory(notificationHelperProvider);
  }

  public static CheckGoalNotificationUseCase newInstance(NotificationHelper notificationHelper) {
    return new CheckGoalNotificationUseCase(notificationHelper);
  }
}
