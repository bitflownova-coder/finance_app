package com.financemanager.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.HiltWrapper_WorkerFactoryModule;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.financemanager.app.data.local.dao.BankAccountDao;
import com.financemanager.app.data.local.dao.BudgetDao;
import com.financemanager.app.data.local.dao.RecurringTransactionDao;
import com.financemanager.app.data.local.dao.SavingsGoalDao;
import com.financemanager.app.data.local.dao.SplitBillDao;
import com.financemanager.app.data.local.dao.TransactionDao;
import com.financemanager.app.data.local.dao.UserDao;
import com.financemanager.app.data.local.database.AppDatabase;
import com.financemanager.app.data.mapper.SplitBillMapper;
import com.financemanager.app.data.repository.BankAccountRepositoryImpl;
import com.financemanager.app.data.repository.BudgetRepositoryImpl;
import com.financemanager.app.data.repository.InsightRepositoryImpl;
import com.financemanager.app.data.repository.RecurringTransactionRepositoryImpl;
import com.financemanager.app.data.repository.ReportRepositoryImpl;
import com.financemanager.app.data.repository.SavingsGoalRepositoryImpl;
import com.financemanager.app.data.repository.SplitBillRepositoryImpl;
import com.financemanager.app.data.repository.TransactionRepositoryImpl;
import com.financemanager.app.data.repository.UserRepositoryImpl;
import com.financemanager.app.di.AppModule;
import com.financemanager.app.di.AppModule_ProvideEncryptedSharedPreferencesFactory;
import com.financemanager.app.di.AppModule_ProvideIoDispatcherFactory;
import com.financemanager.app.di.DatabaseModule;
import com.financemanager.app.di.DatabaseModule_ProvideAppDatabaseFactory;
import com.financemanager.app.di.DatabaseModule_ProvideBankAccountDaoFactory;
import com.financemanager.app.di.DatabaseModule_ProvideBudgetDaoFactory;
import com.financemanager.app.di.DatabaseModule_ProvideRecurringTransactionDaoFactory;
import com.financemanager.app.di.DatabaseModule_ProvideSavingsGoalDaoFactory;
import com.financemanager.app.di.DatabaseModule_ProvideSplitBillDaoFactory;
import com.financemanager.app.di.DatabaseModule_ProvideTransactionDaoFactory;
import com.financemanager.app.di.DatabaseModule_ProvideUserDaoFactory;
import com.financemanager.app.domain.repository.BudgetRepository;
import com.financemanager.app.domain.repository.InsightRepository;
import com.financemanager.app.domain.repository.RecurringTransactionRepository;
import com.financemanager.app.domain.repository.ReportRepository;
import com.financemanager.app.domain.repository.SavingsGoalRepository;
import com.financemanager.app.domain.repository.SplitBillRepository;
import com.financemanager.app.domain.usecase.AddSplitBillUseCase;
import com.financemanager.app.domain.usecase.GetSplitBillsUseCase;
import com.financemanager.app.domain.usecase.LearnFromTransactionHistoryUseCase;
import com.financemanager.app.domain.usecase.MarkParticipantPaidUseCase;
import com.financemanager.app.domain.usecase.PredictTransactionCategoryUseCase;
import com.financemanager.app.domain.usecase.UpdateUserPinUseCase;
import com.financemanager.app.domain.usecase.account.AddAccountUseCase;
import com.financemanager.app.domain.usecase.account.CalculateTotalBalanceUseCase;
import com.financemanager.app.domain.usecase.account.DeleteAccountUseCase;
import com.financemanager.app.domain.usecase.account.GetAccountsUseCase;
import com.financemanager.app.domain.usecase.account.UpdateAccountUseCase;
import com.financemanager.app.domain.usecase.analytics.GetExpenseByCategoryUseCase;
import com.financemanager.app.domain.usecase.analytics.GetFinancialSummaryUseCase;
import com.financemanager.app.domain.usecase.analytics.GetMonthlyTrendUseCase;
import com.financemanager.app.domain.usecase.analytics.GetTopSpendingCategoriesUseCase;
import com.financemanager.app.domain.usecase.auth.RegisterUseCase;
import com.financemanager.app.domain.usecase.auth.SetupPinUseCase;
import com.financemanager.app.domain.usecase.auth.VerifyPinUseCase;
import com.financemanager.app.domain.usecase.budget.AddBudgetUseCase;
import com.financemanager.app.domain.usecase.budget.CheckBudgetStatusUseCase;
import com.financemanager.app.domain.usecase.budget.DeleteBudgetUseCase;
import com.financemanager.app.domain.usecase.budget.GetBudgetsUseCase;
import com.financemanager.app.domain.usecase.budget.UpdateBudgetUseCase;
import com.financemanager.app.domain.usecase.calendar.GenerateCalendarUseCase;
import com.financemanager.app.domain.usecase.goal.AddContributionUseCase;
import com.financemanager.app.domain.usecase.goal.AddGoalUseCase;
import com.financemanager.app.domain.usecase.goal.DeleteGoalUseCase;
import com.financemanager.app.domain.usecase.goal.GetActiveGoalsUseCase;
import com.financemanager.app.domain.usecase.goal.GetGoalStatisticsUseCase;
import com.financemanager.app.domain.usecase.goal.UpdateGoalUseCase;
import com.financemanager.app.domain.usecase.insights.GenerateInsightsUseCase;
import com.financemanager.app.domain.usecase.insights.GetCategoryInsightsUseCase;
import com.financemanager.app.domain.usecase.insights.GetMonthlySummaryUseCase;
import com.financemanager.app.domain.usecase.insights.GetSpendingPatternsUseCase;
import com.financemanager.app.domain.usecase.insights.PredictExpensesUseCase;
import com.financemanager.app.domain.usecase.recurring.AddRecurringTransactionUseCase;
import com.financemanager.app.domain.usecase.recurring.DeleteRecurringTransactionUseCase;
import com.financemanager.app.domain.usecase.recurring.GetRecurringTransactionsUseCase;
import com.financemanager.app.domain.usecase.recurring.ProcessRecurringTransactionsUseCase;
import com.financemanager.app.domain.usecase.recurring.ToggleRecurringActiveUseCase;
import com.financemanager.app.domain.usecase.recurring.UpdateRecurringTransactionUseCase;
import com.financemanager.app.domain.usecase.report.GenerateCsvReportUseCase;
import com.financemanager.app.domain.usecase.report.GeneratePdfReportUseCase;
import com.financemanager.app.domain.usecase.report.GetBudgetComparisonUseCase;
import com.financemanager.app.domain.usecase.report.GetTransactionSummaryUseCase;
import com.financemanager.app.domain.usecase.transaction.AddTransactionUseCase;
import com.financemanager.app.domain.usecase.transaction.DeleteTransactionUseCase;
import com.financemanager.app.domain.usecase.transaction.GetMonthlyExpensesUseCase;
import com.financemanager.app.domain.usecase.transaction.GetMonthlyIncomeUseCase;
import com.financemanager.app.domain.usecase.transaction.GetTransactionsUseCase;
import com.financemanager.app.domain.usecase.transaction.SearchTransactionsUseCase;
import com.financemanager.app.domain.usecase.transaction.UpdateTransactionUseCase;
import com.financemanager.app.presentation.MainActivity;
import com.financemanager.app.presentation.MainActivity_MembersInjector;
import com.financemanager.app.presentation.analytics.AnalyticsFragment;
import com.financemanager.app.presentation.analytics.AnalyticsViewModel;
import com.financemanager.app.presentation.analytics.AnalyticsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.auth.BankSetupFragment;
import com.financemanager.app.presentation.auth.BankSetupViewModel;
import com.financemanager.app.presentation.auth.BankSetupViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.auth.PinResetFragment;
import com.financemanager.app.presentation.auth.PinResetViewModel;
import com.financemanager.app.presentation.auth.PinResetViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.auth.PinSetupFragment;
import com.financemanager.app.presentation.auth.PinSetupViewModel;
import com.financemanager.app.presentation.auth.PinSetupViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.auth.PinVerifyFragment;
import com.financemanager.app.presentation.auth.PinVerifyViewModel;
import com.financemanager.app.presentation.auth.PinVerifyViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.auth.RegisterFragment;
import com.financemanager.app.presentation.auth.RegisterViewModel;
import com.financemanager.app.presentation.auth.RegisterViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.auth.SplashFragment;
import com.financemanager.app.presentation.auth.SplashFragment_MembersInjector;
import com.financemanager.app.presentation.budget.AddEditBudgetDialog;
import com.financemanager.app.presentation.budget.BudgetFragment;
import com.financemanager.app.presentation.budget.BudgetViewModel;
import com.financemanager.app.presentation.budget.BudgetViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.calendar.CalendarFragment;
import com.financemanager.app.presentation.calendar.CalendarViewModel;
import com.financemanager.app.presentation.calendar.CalendarViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.dashboard.DashboardFragment;
import com.financemanager.app.presentation.dashboard.DashboardFragment_MembersInjector;
import com.financemanager.app.presentation.dashboard.DashboardViewModel;
import com.financemanager.app.presentation.dashboard.DashboardViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.goals.GoalsFragment;
import com.financemanager.app.presentation.goals.GoalsFragment_MembersInjector;
import com.financemanager.app.presentation.goals.GoalsViewModel;
import com.financemanager.app.presentation.goals.GoalsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.help.HelpFragment;
import com.financemanager.app.presentation.insights.InsightViewModel;
import com.financemanager.app.presentation.insights.InsightViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.insights.InsightsFragment;
import com.financemanager.app.presentation.insights.InsightsFragment_MembersInjector;
import com.financemanager.app.presentation.onboarding.OnboardingFragment;
import com.financemanager.app.presentation.profile.ProfileFragment;
import com.financemanager.app.presentation.profile.ProfileViewModel;
import com.financemanager.app.presentation.profile.ProfileViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.recurring.RecurringTransactionFragment;
import com.financemanager.app.presentation.recurring.RecurringTransactionFragment_MembersInjector;
import com.financemanager.app.presentation.recurring.RecurringTransactionViewModel;
import com.financemanager.app.presentation.recurring.RecurringTransactionViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.reports.ReportsFragment;
import com.financemanager.app.presentation.reports.ReportsViewModel;
import com.financemanager.app.presentation.reports.ReportsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.settings.SettingsFragment;
import com.financemanager.app.presentation.settings.SettingsViewModel;
import com.financemanager.app.presentation.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.splitbill.AddSplitBillDialog;
import com.financemanager.app.presentation.splitbill.SplitBillFragment;
import com.financemanager.app.presentation.splitbill.SplitBillViewModel;
import com.financemanager.app.presentation.splitbill.SplitBillViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.transaction.AddEditTransactionDialog;
import com.financemanager.app.presentation.transaction.AddEditTransactionDialog_MembersInjector;
import com.financemanager.app.presentation.transaction.CategorySuggestionViewModel;
import com.financemanager.app.presentation.transaction.CategorySuggestionViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.presentation.transaction.TransactionFragment;
import com.financemanager.app.presentation.transaction.TransactionViewModel;
import com.financemanager.app.presentation.transaction.TransactionViewModel_HiltModules_KeyModule_ProvideFactory;
import com.financemanager.app.util.CsvGenerator;
import com.financemanager.app.util.ImageStorageHelper;
import com.financemanager.app.util.NotificationHelper;
import com.financemanager.app.util.NotificationScheduler;
import com.financemanager.app.util.PdfGenerator;
import com.financemanager.app.util.ReceiptOcrHelper;
import com.financemanager.app.util.SessionManager;
import com.financemanager.app.worker.NotificationWorker;
import com.financemanager.app.worker.NotificationWorker_AssistedFactory;
import com.financemanager.app.worker.RecurringTransactionWorker;
import com.financemanager.app.worker.RecurringTransactionWorker_AssistedFactory;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.SetBuilder;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DaggerFinanceManagerApp_HiltComponents_SingletonC {
  private DaggerFinanceManagerApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder appModule(AppModule appModule) {
      Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder databaseModule(DatabaseModule databaseModule) {
      Preconditions.checkNotNull(databaseModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule(
        HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule) {
      Preconditions.checkNotNull(hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder hiltWrapper_WorkerFactoryModule(
        HiltWrapper_WorkerFactoryModule hiltWrapper_WorkerFactoryModule) {
      Preconditions.checkNotNull(hiltWrapper_WorkerFactoryModule);
      return this;
    }

    public FinanceManagerApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements FinanceManagerApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public FinanceManagerApp_HiltComponents.ActivityRetainedC build() {
      return new ActivityRetainedCImpl(singletonCImpl);
    }
  }

  private static final class ActivityCBuilder implements FinanceManagerApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public FinanceManagerApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements FinanceManagerApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public FinanceManagerApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements FinanceManagerApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public FinanceManagerApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements FinanceManagerApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public FinanceManagerApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements FinanceManagerApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public FinanceManagerApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements FinanceManagerApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public FinanceManagerApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends FinanceManagerApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends FinanceManagerApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    private ReceiptOcrHelper receiptOcrHelper() {
      return new ReceiptOcrHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.imageStorageHelperProvider.get());
    }

    @Override
    public void injectAnalyticsFragment(AnalyticsFragment analyticsFragment) {
    }

    @Override
    public void injectBankSetupFragment(BankSetupFragment bankSetupFragment) {
    }

    @Override
    public void injectPinResetFragment(PinResetFragment pinResetFragment) {
    }

    @Override
    public void injectPinSetupFragment(PinSetupFragment pinSetupFragment) {
    }

    @Override
    public void injectPinVerifyFragment(PinVerifyFragment pinVerifyFragment) {
    }

    @Override
    public void injectRegisterFragment(RegisterFragment registerFragment) {
    }

    @Override
    public void injectSplashFragment(SplashFragment splashFragment) {
      injectSplashFragment2(splashFragment);
    }

    @Override
    public void injectAddEditBudgetDialog(AddEditBudgetDialog addEditBudgetDialog) {
    }

    @Override
    public void injectBudgetFragment(BudgetFragment budgetFragment) {
    }

    @Override
    public void injectCalendarFragment(CalendarFragment calendarFragment) {
    }

    @Override
    public void injectDashboardFragment(DashboardFragment dashboardFragment) {
      injectDashboardFragment2(dashboardFragment);
    }

    @Override
    public void injectGoalsFragment(GoalsFragment goalsFragment) {
      injectGoalsFragment2(goalsFragment);
    }

    @Override
    public void injectHelpFragment(HelpFragment helpFragment) {
    }

    @Override
    public void injectInsightsFragment(InsightsFragment insightsFragment) {
      injectInsightsFragment2(insightsFragment);
    }

    @Override
    public void injectOnboardingFragment(OnboardingFragment onboardingFragment) {
    }

    @Override
    public void injectProfileFragment(ProfileFragment profileFragment) {
    }

    @Override
    public void injectRecurringTransactionFragment(
        RecurringTransactionFragment recurringTransactionFragment) {
      injectRecurringTransactionFragment2(recurringTransactionFragment);
    }

    @Override
    public void injectReportsFragment(ReportsFragment reportsFragment) {
    }

    @Override
    public void injectSettingsFragment(SettingsFragment settingsFragment) {
    }

    @Override
    public void injectAddSplitBillDialog(AddSplitBillDialog addSplitBillDialog) {
    }

    @Override
    public void injectSplitBillFragment(SplitBillFragment splitBillFragment) {
    }

    @Override
    public void injectAddEditTransactionDialog(AddEditTransactionDialog addEditTransactionDialog) {
      injectAddEditTransactionDialog2(addEditTransactionDialog);
    }

    @Override
    public void injectTransactionFragment(TransactionFragment transactionFragment) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }

    @CanIgnoreReturnValue
    private SplashFragment injectSplashFragment2(SplashFragment instance) {
      SplashFragment_MembersInjector.injectSessionManager(instance, singletonCImpl.sessionManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private DashboardFragment injectDashboardFragment2(DashboardFragment instance) {
      DashboardFragment_MembersInjector.injectSessionManager(instance, singletonCImpl.sessionManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private GoalsFragment injectGoalsFragment2(GoalsFragment instance) {
      GoalsFragment_MembersInjector.injectSessionManager(instance, singletonCImpl.sessionManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private InsightsFragment injectInsightsFragment2(InsightsFragment instance) {
      InsightsFragment_MembersInjector.injectSessionManager(instance, singletonCImpl.sessionManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private RecurringTransactionFragment injectRecurringTransactionFragment2(
        RecurringTransactionFragment instance) {
      RecurringTransactionFragment_MembersInjector.injectSessionManager(instance, singletonCImpl.sessionManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private AddEditTransactionDialog injectAddEditTransactionDialog2(
        AddEditTransactionDialog instance) {
      AddEditTransactionDialog_MembersInjector.injectImageStorageHelper(instance, singletonCImpl.imageStorageHelperProvider.get());
      AddEditTransactionDialog_MembersInjector.injectReceiptOcrHelper(instance, receiptOcrHelper());
      return instance;
    }
  }

  private static final class ViewCImpl extends FinanceManagerApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends FinanceManagerApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(18).add(AnalyticsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(BankSetupViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(BudgetViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(CalendarViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(CategorySuggestionViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(DashboardViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(GoalsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(InsightViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(PinResetViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(PinSetupViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(PinVerifyViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ProfileViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(RecurringTransactionViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(RegisterViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ReportsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SplitBillViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(TransactionViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectSessionManager(instance, singletonCImpl.sessionManagerProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends FinanceManagerApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnalyticsViewModel> analyticsViewModelProvider;

    private Provider<BankSetupViewModel> bankSetupViewModelProvider;

    private Provider<BudgetViewModel> budgetViewModelProvider;

    private Provider<CalendarViewModel> calendarViewModelProvider;

    private Provider<CategorySuggestionViewModel> categorySuggestionViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<GoalsViewModel> goalsViewModelProvider;

    private Provider<InsightViewModel> insightViewModelProvider;

    private Provider<PinResetViewModel> pinResetViewModelProvider;

    private Provider<PinSetupViewModel> pinSetupViewModelProvider;

    private Provider<PinVerifyViewModel> pinVerifyViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<RecurringTransactionViewModel> recurringTransactionViewModelProvider;

    private Provider<RegisterViewModel> registerViewModelProvider;

    private Provider<ReportsViewModel> reportsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<SplitBillViewModel> splitBillViewModelProvider;

    private Provider<TransactionViewModel> transactionViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetExpenseByCategoryUseCase getExpenseByCategoryUseCase() {
      return new GetExpenseByCategoryUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private GetMonthlyTrendUseCase getMonthlyTrendUseCase() {
      return new GetMonthlyTrendUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private GetTopSpendingCategoriesUseCase getTopSpendingCategoriesUseCase() {
      return new GetTopSpendingCategoriesUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private GetFinancialSummaryUseCase getFinancialSummaryUseCase() {
      return new GetFinancialSummaryUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private AddAccountUseCase addAccountUseCase() {
      return new AddAccountUseCase(singletonCImpl.bankAccountRepositoryImplProvider.get());
    }

    private GetBudgetsUseCase getBudgetsUseCase() {
      return new GetBudgetsUseCase(singletonCImpl.bindBudgetRepositoryProvider.get());
    }

    private AddBudgetUseCase addBudgetUseCase() {
      return new AddBudgetUseCase(singletonCImpl.bindBudgetRepositoryProvider.get());
    }

    private UpdateBudgetUseCase updateBudgetUseCase() {
      return new UpdateBudgetUseCase(singletonCImpl.bindBudgetRepositoryProvider.get());
    }

    private DeleteBudgetUseCase deleteBudgetUseCase() {
      return new DeleteBudgetUseCase(singletonCImpl.bindBudgetRepositoryProvider.get());
    }

    private GenerateCalendarUseCase generateCalendarUseCase() {
      return new GenerateCalendarUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private LearnFromTransactionHistoryUseCase learnFromTransactionHistoryUseCase() {
      return new LearnFromTransactionHistoryUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private CalculateTotalBalanceUseCase calculateTotalBalanceUseCase() {
      return new CalculateTotalBalanceUseCase(singletonCImpl.bankAccountRepositoryImplProvider.get());
    }

    private GetAccountsUseCase getAccountsUseCase() {
      return new GetAccountsUseCase(singletonCImpl.bankAccountRepositoryImplProvider.get());
    }

    private GetTransactionsUseCase getTransactionsUseCase() {
      return new GetTransactionsUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private GetMonthlyIncomeUseCase getMonthlyIncomeUseCase() {
      return new GetMonthlyIncomeUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private GetMonthlyExpensesUseCase getMonthlyExpensesUseCase() {
      return new GetMonthlyExpensesUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private GetActiveGoalsUseCase getActiveGoalsUseCase() {
      return new GetActiveGoalsUseCase(singletonCImpl.bindSavingsGoalRepositoryProvider.get());
    }

    private AddGoalUseCase addGoalUseCase() {
      return new AddGoalUseCase(singletonCImpl.bindSavingsGoalRepositoryProvider.get());
    }

    private UpdateGoalUseCase updateGoalUseCase() {
      return new UpdateGoalUseCase(singletonCImpl.bindSavingsGoalRepositoryProvider.get());
    }

    private DeleteGoalUseCase deleteGoalUseCase() {
      return new DeleteGoalUseCase(singletonCImpl.bindSavingsGoalRepositoryProvider.get());
    }

    private AddContributionUseCase addContributionUseCase() {
      return new AddContributionUseCase(singletonCImpl.bindSavingsGoalRepositoryProvider.get());
    }

    private GetGoalStatisticsUseCase getGoalStatisticsUseCase() {
      return new GetGoalStatisticsUseCase(singletonCImpl.bindSavingsGoalRepositoryProvider.get());
    }

    private GenerateInsightsUseCase generateInsightsUseCase() {
      return new GenerateInsightsUseCase(singletonCImpl.bindInsightRepositoryProvider.get());
    }

    private GetSpendingPatternsUseCase getSpendingPatternsUseCase() {
      return new GetSpendingPatternsUseCase(singletonCImpl.bindInsightRepositoryProvider.get());
    }

    private GetMonthlySummaryUseCase getMonthlySummaryUseCase() {
      return new GetMonthlySummaryUseCase(singletonCImpl.bindInsightRepositoryProvider.get());
    }

    private GetCategoryInsightsUseCase getCategoryInsightsUseCase() {
      return new GetCategoryInsightsUseCase(singletonCImpl.bindInsightRepositoryProvider.get());
    }

    private PredictExpensesUseCase predictExpensesUseCase() {
      return new PredictExpensesUseCase(singletonCImpl.bindInsightRepositoryProvider.get());
    }

    private UpdateUserPinUseCase updateUserPinUseCase() {
      return new UpdateUserPinUseCase(singletonCImpl.userRepositoryImplProvider.get());
    }

    private SetupPinUseCase setupPinUseCase() {
      return new SetupPinUseCase(singletonCImpl.userRepositoryImplProvider.get());
    }

    private VerifyPinUseCase verifyPinUseCase() {
      return new VerifyPinUseCase(singletonCImpl.userRepositoryImplProvider.get());
    }

    private UpdateAccountUseCase updateAccountUseCase() {
      return new UpdateAccountUseCase(singletonCImpl.bankAccountRepositoryImplProvider.get());
    }

    private DeleteAccountUseCase deleteAccountUseCase() {
      return new DeleteAccountUseCase(singletonCImpl.bankAccountRepositoryImplProvider.get());
    }

    private GetRecurringTransactionsUseCase getRecurringTransactionsUseCase() {
      return new GetRecurringTransactionsUseCase(singletonCImpl.bindRecurringTransactionRepositoryProvider.get());
    }

    private AddRecurringTransactionUseCase addRecurringTransactionUseCase() {
      return new AddRecurringTransactionUseCase(singletonCImpl.bindRecurringTransactionRepositoryProvider.get());
    }

    private UpdateRecurringTransactionUseCase updateRecurringTransactionUseCase() {
      return new UpdateRecurringTransactionUseCase(singletonCImpl.bindRecurringTransactionRepositoryProvider.get());
    }

    private DeleteRecurringTransactionUseCase deleteRecurringTransactionUseCase() {
      return new DeleteRecurringTransactionUseCase(singletonCImpl.bindRecurringTransactionRepositoryProvider.get());
    }

    private ToggleRecurringActiveUseCase toggleRecurringActiveUseCase() {
      return new ToggleRecurringActiveUseCase(singletonCImpl.bindRecurringTransactionRepositoryProvider.get());
    }

    private RegisterUseCase registerUseCase() {
      return new RegisterUseCase(singletonCImpl.userRepositoryImplProvider.get());
    }

    private GetTransactionSummaryUseCase getTransactionSummaryUseCase() {
      return new GetTransactionSummaryUseCase(singletonCImpl.bindReportRepositoryProvider.get());
    }

    private GetBudgetComparisonUseCase getBudgetComparisonUseCase() {
      return new GetBudgetComparisonUseCase(singletonCImpl.bindReportRepositoryProvider.get());
    }

    private GeneratePdfReportUseCase generatePdfReportUseCase() {
      return new GeneratePdfReportUseCase(singletonCImpl.bindReportRepositoryProvider.get());
    }

    private GenerateCsvReportUseCase generateCsvReportUseCase() {
      return new GenerateCsvReportUseCase(singletonCImpl.bindReportRepositoryProvider.get());
    }

    private GetSplitBillsUseCase getSplitBillsUseCase() {
      return new GetSplitBillsUseCase(singletonCImpl.bindSplitBillRepositoryProvider.get());
    }

    private AddSplitBillUseCase addSplitBillUseCase() {
      return new AddSplitBillUseCase(singletonCImpl.bindSplitBillRepositoryProvider.get());
    }

    private MarkParticipantPaidUseCase markParticipantPaidUseCase() {
      return new MarkParticipantPaidUseCase(singletonCImpl.bindSplitBillRepositoryProvider.get());
    }

    private AddTransactionUseCase addTransactionUseCase() {
      return new AddTransactionUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private UpdateTransactionUseCase updateTransactionUseCase() {
      return new UpdateTransactionUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private DeleteTransactionUseCase deleteTransactionUseCase() {
      return new DeleteTransactionUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    private SearchTransactionsUseCase searchTransactionsUseCase() {
      return new SearchTransactionsUseCase(singletonCImpl.transactionRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.analyticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.bankSetupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.budgetViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.calendarViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.categorySuggestionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.goalsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.insightViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.pinResetViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.pinSetupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.pinVerifyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.recurringTransactionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.registerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.reportsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.splitBillViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
      this.transactionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17);
    }

    @Override
    public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, Provider<ViewModel>>newMapBuilder(18).put("com.financemanager.app.presentation.analytics.AnalyticsViewModel", ((Provider) analyticsViewModelProvider)).put("com.financemanager.app.presentation.auth.BankSetupViewModel", ((Provider) bankSetupViewModelProvider)).put("com.financemanager.app.presentation.budget.BudgetViewModel", ((Provider) budgetViewModelProvider)).put("com.financemanager.app.presentation.calendar.CalendarViewModel", ((Provider) calendarViewModelProvider)).put("com.financemanager.app.presentation.transaction.CategorySuggestionViewModel", ((Provider) categorySuggestionViewModelProvider)).put("com.financemanager.app.presentation.dashboard.DashboardViewModel", ((Provider) dashboardViewModelProvider)).put("com.financemanager.app.presentation.goals.GoalsViewModel", ((Provider) goalsViewModelProvider)).put("com.financemanager.app.presentation.insights.InsightViewModel", ((Provider) insightViewModelProvider)).put("com.financemanager.app.presentation.auth.PinResetViewModel", ((Provider) pinResetViewModelProvider)).put("com.financemanager.app.presentation.auth.PinSetupViewModel", ((Provider) pinSetupViewModelProvider)).put("com.financemanager.app.presentation.auth.PinVerifyViewModel", ((Provider) pinVerifyViewModelProvider)).put("com.financemanager.app.presentation.profile.ProfileViewModel", ((Provider) profileViewModelProvider)).put("com.financemanager.app.presentation.recurring.RecurringTransactionViewModel", ((Provider) recurringTransactionViewModelProvider)).put("com.financemanager.app.presentation.auth.RegisterViewModel", ((Provider) registerViewModelProvider)).put("com.financemanager.app.presentation.reports.ReportsViewModel", ((Provider) reportsViewModelProvider)).put("com.financemanager.app.presentation.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).put("com.financemanager.app.presentation.splitbill.SplitBillViewModel", ((Provider) splitBillViewModelProvider)).put("com.financemanager.app.presentation.transaction.TransactionViewModel", ((Provider) transactionViewModelProvider)).build();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.financemanager.app.presentation.analytics.AnalyticsViewModel 
          return (T) new AnalyticsViewModel(viewModelCImpl.getExpenseByCategoryUseCase(), viewModelCImpl.getMonthlyTrendUseCase(), viewModelCImpl.getTopSpendingCategoriesUseCase(), viewModelCImpl.getFinancialSummaryUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 1: // com.financemanager.app.presentation.auth.BankSetupViewModel 
          return (T) new BankSetupViewModel(viewModelCImpl.addAccountUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 2: // com.financemanager.app.presentation.budget.BudgetViewModel 
          return (T) new BudgetViewModel(viewModelCImpl.getBudgetsUseCase(), viewModelCImpl.addBudgetUseCase(), viewModelCImpl.updateBudgetUseCase(), viewModelCImpl.deleteBudgetUseCase(), new CheckBudgetStatusUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 3: // com.financemanager.app.presentation.calendar.CalendarViewModel 
          return (T) new CalendarViewModel(viewModelCImpl.generateCalendarUseCase(), singletonCImpl.transactionRepositoryImplProvider.get(), singletonCImpl.sessionManagerProvider.get());

          case 4: // com.financemanager.app.presentation.transaction.CategorySuggestionViewModel 
          return (T) new CategorySuggestionViewModel(new PredictTransactionCategoryUseCase(), viewModelCImpl.learnFromTransactionHistoryUseCase());

          case 5: // com.financemanager.app.presentation.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.sessionManagerProvider.get(), viewModelCImpl.calculateTotalBalanceUseCase(), viewModelCImpl.getAccountsUseCase(), viewModelCImpl.getTransactionsUseCase(), viewModelCImpl.getMonthlyIncomeUseCase(), viewModelCImpl.getMonthlyExpensesUseCase());

          case 6: // com.financemanager.app.presentation.goals.GoalsViewModel 
          return (T) new GoalsViewModel(viewModelCImpl.getActiveGoalsUseCase(), viewModelCImpl.addGoalUseCase(), viewModelCImpl.updateGoalUseCase(), viewModelCImpl.deleteGoalUseCase(), viewModelCImpl.addContributionUseCase(), viewModelCImpl.getGoalStatisticsUseCase(), singletonCImpl.bindSavingsGoalRepositoryProvider.get());

          case 7: // com.financemanager.app.presentation.insights.InsightViewModel 
          return (T) new InsightViewModel(viewModelCImpl.generateInsightsUseCase(), viewModelCImpl.getSpendingPatternsUseCase(), viewModelCImpl.getMonthlySummaryUseCase(), viewModelCImpl.getCategoryInsightsUseCase(), viewModelCImpl.predictExpensesUseCase());

          case 8: // com.financemanager.app.presentation.auth.PinResetViewModel 
          return (T) new PinResetViewModel(viewModelCImpl.updateUserPinUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 9: // com.financemanager.app.presentation.auth.PinSetupViewModel 
          return (T) new PinSetupViewModel(viewModelCImpl.setupPinUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 10: // com.financemanager.app.presentation.auth.PinVerifyViewModel 
          return (T) new PinVerifyViewModel(viewModelCImpl.verifyPinUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 11: // com.financemanager.app.presentation.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.sessionManagerProvider.get(), viewModelCImpl.getAccountsUseCase(), viewModelCImpl.addAccountUseCase(), viewModelCImpl.updateAccountUseCase(), viewModelCImpl.deleteAccountUseCase(), viewModelCImpl.calculateTotalBalanceUseCase());

          case 12: // com.financemanager.app.presentation.recurring.RecurringTransactionViewModel 
          return (T) new RecurringTransactionViewModel(viewModelCImpl.getRecurringTransactionsUseCase(), viewModelCImpl.addRecurringTransactionUseCase(), viewModelCImpl.updateRecurringTransactionUseCase(), viewModelCImpl.deleteRecurringTransactionUseCase(), viewModelCImpl.toggleRecurringActiveUseCase(), singletonCImpl.processRecurringTransactionsUseCase());

          case 13: // com.financemanager.app.presentation.auth.RegisterViewModel 
          return (T) new RegisterViewModel(viewModelCImpl.registerUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 14: // com.financemanager.app.presentation.reports.ReportsViewModel 
          return (T) new ReportsViewModel(viewModelCImpl.getTransactionSummaryUseCase(), viewModelCImpl.getBudgetComparisonUseCase(), viewModelCImpl.generatePdfReportUseCase(), viewModelCImpl.generateCsvReportUseCase(), singletonCImpl.sessionManagerProvider.get());

          case 15: // com.financemanager.app.presentation.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.sessionManagerProvider.get(), ApplicationContextModule_ProvideApplicationFactory.provideApplication(singletonCImpl.applicationContextModule));

          case 16: // com.financemanager.app.presentation.splitbill.SplitBillViewModel 
          return (T) new SplitBillViewModel(viewModelCImpl.getSplitBillsUseCase(), viewModelCImpl.addSplitBillUseCase(), viewModelCImpl.markParticipantPaidUseCase(), singletonCImpl.bindSplitBillRepositoryProvider.get(), singletonCImpl.sessionManagerProvider.get());

          case 17: // com.financemanager.app.presentation.transaction.TransactionViewModel 
          return (T) new TransactionViewModel(singletonCImpl.sessionManagerProvider.get(), viewModelCImpl.getTransactionsUseCase(), viewModelCImpl.addTransactionUseCase(), viewModelCImpl.updateTransactionUseCase(), viewModelCImpl.deleteTransactionUseCase(), viewModelCImpl.searchTransactionsUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends FinanceManagerApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;

      initialize();

    }

    @SuppressWarnings("unchecked")
    private void initialize() {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends FinanceManagerApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends FinanceManagerApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<BudgetRepositoryImpl> budgetRepositoryImplProvider;

    private Provider<BudgetRepository> bindBudgetRepositoryProvider;

    private Provider<SavingsGoalRepositoryImpl> savingsGoalRepositoryImplProvider;

    private Provider<SavingsGoalRepository> bindSavingsGoalRepositoryProvider;

    private Provider<SharedPreferences> provideEncryptedSharedPreferencesProvider;

    private Provider<SessionManager> sessionManagerProvider;

    private Provider<NotificationHelper> notificationHelperProvider;

    private Provider<NotificationWorker_AssistedFactory> notificationWorker_AssistedFactoryProvider;

    private Provider<RecurringTransactionRepositoryImpl> recurringTransactionRepositoryImplProvider;

    private Provider<RecurringTransactionRepository> bindRecurringTransactionRepositoryProvider;

    private Provider<RecurringTransactionWorker_AssistedFactory> recurringTransactionWorker_AssistedFactoryProvider;

    private Provider<NotificationScheduler> notificationSchedulerProvider;

    private Provider<ImageStorageHelper> imageStorageHelperProvider;

    private Provider<TransactionRepositoryImpl> transactionRepositoryImplProvider;

    private Provider<BankAccountRepositoryImpl> bankAccountRepositoryImplProvider;

    private Provider<InsightRepositoryImpl> insightRepositoryImplProvider;

    private Provider<InsightRepository> bindInsightRepositoryProvider;

    private Provider<UserRepositoryImpl> userRepositoryImplProvider;

    private Provider<ReportRepositoryImpl> reportRepositoryImplProvider;

    private Provider<ReportRepository> bindReportRepositoryProvider;

    private Provider<SplitBillRepositoryImpl> splitBillRepositoryImplProvider;

    private Provider<SplitBillRepository> bindSplitBillRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private BudgetDao budgetDao() {
      return DatabaseModule_ProvideBudgetDaoFactory.provideBudgetDao(provideAppDatabaseProvider.get());
    }

    private SavingsGoalDao savingsGoalDao() {
      return DatabaseModule_ProvideSavingsGoalDaoFactory.provideSavingsGoalDao(provideAppDatabaseProvider.get());
    }

    private RecurringTransactionDao recurringTransactionDao() {
      return DatabaseModule_ProvideRecurringTransactionDaoFactory.provideRecurringTransactionDao(provideAppDatabaseProvider.get());
    }

    private TransactionDao transactionDao() {
      return DatabaseModule_ProvideTransactionDaoFactory.provideTransactionDao(provideAppDatabaseProvider.get());
    }

    private ProcessRecurringTransactionsUseCase processRecurringTransactionsUseCase() {
      return new ProcessRecurringTransactionsUseCase(bindRecurringTransactionRepositoryProvider.get());
    }

    private Map<String, Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return MapBuilder.<String, Provider<WorkerAssistedFactory<? extends ListenableWorker>>>newMapBuilder(2).put("com.financemanager.app.worker.NotificationWorker", ((Provider) notificationWorker_AssistedFactoryProvider)).put("com.financemanager.app.worker.RecurringTransactionWorker", ((Provider) recurringTransactionWorker_AssistedFactoryProvider)).build();
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    private BankAccountDao bankAccountDao() {
      return DatabaseModule_ProvideBankAccountDaoFactory.provideBankAccountDao(provideAppDatabaseProvider.get());
    }

    private UserDao userDao() {
      return DatabaseModule_ProvideUserDaoFactory.provideUserDao(provideAppDatabaseProvider.get());
    }

    private PdfGenerator pdfGenerator() {
      return new PdfGenerator(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
    }

    private CsvGenerator csvGenerator() {
      return new CsvGenerator(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
    }

    private SplitBillDao splitBillDao() {
      return DatabaseModule_ProvideSplitBillDaoFactory.provideSplitBillDao(provideAppDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 2));
      this.budgetRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 1);
      this.bindBudgetRepositoryProvider = DoubleCheck.provider((Provider) budgetRepositoryImplProvider);
      this.savingsGoalRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 3);
      this.bindSavingsGoalRepositoryProvider = DoubleCheck.provider((Provider) savingsGoalRepositoryImplProvider);
      this.provideEncryptedSharedPreferencesProvider = DoubleCheck.provider(new SwitchingProvider<SharedPreferences>(singletonCImpl, 5));
      this.sessionManagerProvider = DoubleCheck.provider(new SwitchingProvider<SessionManager>(singletonCImpl, 4));
      this.notificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 6));
      this.notificationWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<NotificationWorker_AssistedFactory>(singletonCImpl, 0));
      this.recurringTransactionRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 8);
      this.bindRecurringTransactionRepositoryProvider = DoubleCheck.provider((Provider) recurringTransactionRepositoryImplProvider);
      this.recurringTransactionWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<RecurringTransactionWorker_AssistedFactory>(singletonCImpl, 7));
      this.notificationSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<NotificationScheduler>(singletonCImpl, 9));
      this.imageStorageHelperProvider = DoubleCheck.provider(new SwitchingProvider<ImageStorageHelper>(singletonCImpl, 10));
      this.transactionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<TransactionRepositoryImpl>(singletonCImpl, 11));
      this.bankAccountRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<BankAccountRepositoryImpl>(singletonCImpl, 12));
      this.insightRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 13);
      this.bindInsightRepositoryProvider = DoubleCheck.provider((Provider) insightRepositoryImplProvider);
      this.userRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserRepositoryImpl>(singletonCImpl, 14));
      this.reportRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 15);
      this.bindReportRepositoryProvider = DoubleCheck.provider((Provider) reportRepositoryImplProvider);
      this.splitBillRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 16);
      this.bindSplitBillRepositoryProvider = DoubleCheck.provider((Provider) splitBillRepositoryImplProvider);
    }

    @Override
    public void injectFinanceManagerApp(FinanceManagerApp financeManagerApp) {
      injectFinanceManagerApp2(financeManagerApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private FinanceManagerApp injectFinanceManagerApp2(FinanceManagerApp instance) {
      FinanceManagerApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      FinanceManagerApp_MembersInjector.injectNotificationScheduler(instance, notificationSchedulerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.financemanager.app.worker.NotificationWorker_AssistedFactory 
          return (T) new NotificationWorker_AssistedFactory() {
            @Override
            public NotificationWorker create(Context context, WorkerParameters workerParams) {
              return new NotificationWorker(context, workerParams, singletonCImpl.bindBudgetRepositoryProvider.get(), singletonCImpl.bindSavingsGoalRepositoryProvider.get(), singletonCImpl.sessionManagerProvider.get(), singletonCImpl.notificationHelperProvider.get());
            }
          };

          case 1: // com.financemanager.app.data.repository.BudgetRepositoryImpl 
          return (T) new BudgetRepositoryImpl(singletonCImpl.budgetDao());

          case 2: // com.financemanager.app.data.local.database.AppDatabase 
          return (T) DatabaseModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.financemanager.app.data.repository.SavingsGoalRepositoryImpl 
          return (T) new SavingsGoalRepositoryImpl(singletonCImpl.savingsGoalDao());

          case 4: // com.financemanager.app.util.SessionManager 
          return (T) new SessionManager(singletonCImpl.provideEncryptedSharedPreferencesProvider.get());

          case 5: // android.content.SharedPreferences 
          return (T) AppModule_ProvideEncryptedSharedPreferencesFactory.provideEncryptedSharedPreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.financemanager.app.util.NotificationHelper 
          return (T) new NotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.financemanager.app.worker.RecurringTransactionWorker_AssistedFactory 
          return (T) new RecurringTransactionWorker_AssistedFactory() {
            @Override
            public RecurringTransactionWorker create(Context context2,
                WorkerParameters workerParams2) {
              return new RecurringTransactionWorker(context2, workerParams2, singletonCImpl.processRecurringTransactionsUseCase());
            }
          };

          case 8: // com.financemanager.app.data.repository.RecurringTransactionRepositoryImpl 
          return (T) new RecurringTransactionRepositoryImpl(singletonCImpl.recurringTransactionDao(), singletonCImpl.transactionDao(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 9: // com.financemanager.app.util.NotificationScheduler 
          return (T) new NotificationScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.financemanager.app.util.ImageStorageHelper 
          return (T) new ImageStorageHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 11: // com.financemanager.app.data.repository.TransactionRepositoryImpl 
          return (T) new TransactionRepositoryImpl(singletonCImpl.transactionDao(), singletonCImpl.bankAccountDao(), singletonCImpl.budgetDao(), singletonCImpl.provideAppDatabaseProvider.get(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 12: // com.financemanager.app.data.repository.BankAccountRepositoryImpl 
          return (T) new BankAccountRepositoryImpl(singletonCImpl.bankAccountDao(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 13: // com.financemanager.app.data.repository.InsightRepositoryImpl 
          return (T) new InsightRepositoryImpl(singletonCImpl.transactionDao(), singletonCImpl.budgetDao(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 14: // com.financemanager.app.data.repository.UserRepositoryImpl 
          return (T) new UserRepositoryImpl(singletonCImpl.userDao(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          case 15: // com.financemanager.app.data.repository.ReportRepositoryImpl 
          return (T) new ReportRepositoryImpl(singletonCImpl.transactionDao(), singletonCImpl.budgetDao(), singletonCImpl.pdfGenerator(), singletonCImpl.csvGenerator());

          case 16: // com.financemanager.app.data.repository.SplitBillRepositoryImpl 
          return (T) new SplitBillRepositoryImpl(singletonCImpl.splitBillDao(), new SplitBillMapper(), AppModule_ProvideIoDispatcherFactory.provideIoDispatcher());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
