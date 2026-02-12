package com.financemanager.app.data.repository;

import com.financemanager.app.data.local.dao.SavingsGoalDao;
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
public final class SavingsGoalRepositoryImpl_Factory implements Factory<SavingsGoalRepositoryImpl> {
  private final Provider<SavingsGoalDao> goalDaoProvider;

  public SavingsGoalRepositoryImpl_Factory(Provider<SavingsGoalDao> goalDaoProvider) {
    this.goalDaoProvider = goalDaoProvider;
  }

  @Override
  public SavingsGoalRepositoryImpl get() {
    return newInstance(goalDaoProvider.get());
  }

  public static SavingsGoalRepositoryImpl_Factory create(Provider<SavingsGoalDao> goalDaoProvider) {
    return new SavingsGoalRepositoryImpl_Factory(goalDaoProvider);
  }

  public static SavingsGoalRepositoryImpl newInstance(SavingsGoalDao goalDao) {
    return new SavingsGoalRepositoryImpl(goalDao);
  }
}
