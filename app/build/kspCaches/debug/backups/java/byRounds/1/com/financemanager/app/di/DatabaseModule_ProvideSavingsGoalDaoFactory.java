package com.financemanager.app.di;

import com.financemanager.app.data.local.dao.SavingsGoalDao;
import com.financemanager.app.data.local.database.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideSavingsGoalDaoFactory implements Factory<SavingsGoalDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideSavingsGoalDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SavingsGoalDao get() {
    return provideSavingsGoalDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSavingsGoalDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSavingsGoalDaoFactory(databaseProvider);
  }

  public static SavingsGoalDao provideSavingsGoalDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSavingsGoalDao(database));
  }
}
