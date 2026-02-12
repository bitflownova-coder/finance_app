package com.financemanager.app.di;

import com.financemanager.app.data.local.dao.RecurringTransactionDao;
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
public final class DatabaseModule_ProvideRecurringTransactionDaoFactory implements Factory<RecurringTransactionDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideRecurringTransactionDaoFactory(
      Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public RecurringTransactionDao get() {
    return provideRecurringTransactionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideRecurringTransactionDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideRecurringTransactionDaoFactory(databaseProvider);
  }

  public static RecurringTransactionDao provideRecurringTransactionDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRecurringTransactionDao(database));
  }
}
