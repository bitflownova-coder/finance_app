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
public final class CheckBudgetNotificationUseCase_Factory implements Factory<CheckBudgetNotificationUseCase> {
  private final Provider<NotificationHelper> notificationHelperProvider;

  public CheckBudgetNotificationUseCase_Factory(
      Provider<NotificationHelper> notificationHelperProvider) {
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public CheckBudgetNotificationUseCase get() {
    return newInstance(notificationHelperProvider.get());
  }

  public static CheckBudgetNotificationUseCase_Factory create(
      Provider<NotificationHelper> notificationHelperProvider) {
    return new CheckBudgetNotificationUseCase_Factory(notificationHelperProvider);
  }

  public static CheckBudgetNotificationUseCase newInstance(NotificationHelper notificationHelper) {
    return new CheckBudgetNotificationUseCase(notificationHelper);
  }
}
