package com.financemanager.app.di;

import com.financemanager.app.data.local.dao.SplitBillDao;
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
public final class DatabaseModule_ProvideSplitBillDaoFactory implements Factory<SplitBillDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideSplitBillDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SplitBillDao get() {
    return provideSplitBillDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSplitBillDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSplitBillDaoFactory(databaseProvider);
  }

  public static SplitBillDao provideSplitBillDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSplitBillDao(database));
  }
}
