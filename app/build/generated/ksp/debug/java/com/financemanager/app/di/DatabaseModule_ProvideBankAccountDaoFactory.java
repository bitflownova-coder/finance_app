package com.financemanager.app.di;

import com.financemanager.app.data.local.dao.BankAccountDao;
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
public final class DatabaseModule_ProvideBankAccountDaoFactory implements Factory<BankAccountDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideBankAccountDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BankAccountDao get() {
    return provideBankAccountDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBankAccountDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBankAccountDaoFactory(databaseProvider);
  }

  public static BankAccountDao provideBankAccountDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBankAccountDao(database));
  }
}
