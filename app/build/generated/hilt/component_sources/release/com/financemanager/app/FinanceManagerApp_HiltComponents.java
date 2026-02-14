package com.financemanager.app;

import androidx.hilt.work.HiltWrapper_WorkerFactoryModule;
import com.financemanager.app.di.AppModule;
import com.financemanager.app.di.DatabaseModule;
import com.financemanager.app.di.RepositoryModule;
import com.financemanager.app.presentation.MainActivity_GeneratedInjector;
import com.financemanager.app.presentation.analytics.AnalyticsFragment_GeneratedInjector;
import com.financemanager.app.presentation.analytics.AnalyticsViewModel_HiltModules;
import com.financemanager.app.presentation.auth.BankSetupFragment_GeneratedInjector;
import com.financemanager.app.presentation.auth.BankSetupViewModel_HiltModules;
import com.financemanager.app.presentation.auth.PinResetFragment_GeneratedInjector;
import com.financemanager.app.presentation.auth.PinResetViewModel_HiltModules;
import com.financemanager.app.presentation.auth.PinSetupFragment_GeneratedInjector;
import com.financemanager.app.presentation.auth.PinSetupViewModel_HiltModules;
import com.financemanager.app.presentation.auth.PinVerifyFragment_GeneratedInjector;
import com.financemanager.app.presentation.auth.PinVerifyViewModel_HiltModules;
import com.financemanager.app.presentation.auth.RegisterFragment_GeneratedInjector;
import com.financemanager.app.presentation.auth.RegisterViewModel_HiltModules;
import com.financemanager.app.presentation.auth.SplashFragment_GeneratedInjector;
import com.financemanager.app.presentation.budget.AddEditBudgetDialog_GeneratedInjector;
import com.financemanager.app.presentation.budget.BudgetFragment_GeneratedInjector;
import com.financemanager.app.presentation.budget.BudgetViewModel_HiltModules;
import com.financemanager.app.presentation.calendar.CalendarFragment_GeneratedInjector;
import com.financemanager.app.presentation.calendar.CalendarViewModel_HiltModules;
import com.financemanager.app.presentation.dashboard.DashboardFragment_GeneratedInjector;
import com.financemanager.app.presentation.dashboard.DashboardViewModel_HiltModules;
import com.financemanager.app.presentation.goals.GoalsFragment_GeneratedInjector;
import com.financemanager.app.presentation.goals.GoalsViewModel_HiltModules;
import com.financemanager.app.presentation.help.HelpFragment_GeneratedInjector;
import com.financemanager.app.presentation.insights.InsightViewModel_HiltModules;
import com.financemanager.app.presentation.insights.InsightsFragment_GeneratedInjector;
import com.financemanager.app.presentation.onboarding.OnboardingFragment_GeneratedInjector;
import com.financemanager.app.presentation.profile.ProfileFragment_GeneratedInjector;
import com.financemanager.app.presentation.profile.ProfileViewModel_HiltModules;
import com.financemanager.app.presentation.recurring.RecurringTransactionFragment_GeneratedInjector;
import com.financemanager.app.presentation.recurring.RecurringTransactionViewModel_HiltModules;
import com.financemanager.app.presentation.reports.ReportsFragment_GeneratedInjector;
import com.financemanager.app.presentation.reports.ReportsViewModel_HiltModules;
import com.financemanager.app.presentation.settings.SettingsFragment_GeneratedInjector;
import com.financemanager.app.presentation.settings.SettingsViewModel_HiltModules;
import com.financemanager.app.presentation.splitbill.AddSplitBillDialog_GeneratedInjector;
import com.financemanager.app.presentation.splitbill.SplitBillFragment_GeneratedInjector;
import com.financemanager.app.presentation.splitbill.SplitBillViewModel_HiltModules;
import com.financemanager.app.presentation.transaction.AddEditTransactionDialog_GeneratedInjector;
import com.financemanager.app.presentation.transaction.CategorySuggestionViewModel_HiltModules;
import com.financemanager.app.presentation.transaction.TransactionFragment_GeneratedInjector;
import com.financemanager.app.presentation.transaction.TransactionViewModel_HiltModules;
import com.financemanager.app.worker.NotificationWorker_HiltModule;
import com.financemanager.app.worker.RecurringTransactionWorker_HiltModule;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.annotation.processing.Generated;
import javax.inject.Singleton;

@Generated("dagger.hilt.processor.internal.root.RootProcessor")
public final class FinanceManagerApp_HiltComponents {
  private FinanceManagerApp_HiltComponents() {
  }

  @Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          AppModule.class,
          ApplicationContextModule.class,
          DatabaseModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          HiltWrapper_WorkerFactoryModule.class,
          NotificationWorker_HiltModule.class,
          RecurringTransactionWorker_HiltModule.class,
          RepositoryModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements FinanceManagerApp_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AnalyticsViewModel_HiltModules.KeyModule.class,
          BankSetupViewModel_HiltModules.KeyModule.class,
          BudgetViewModel_HiltModules.KeyModule.class,
          CalendarViewModel_HiltModules.KeyModule.class,
          CategorySuggestionViewModel_HiltModules.KeyModule.class,
          DashboardViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          GoalsViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          InsightViewModel_HiltModules.KeyModule.class,
          PinResetViewModel_HiltModules.KeyModule.class,
          PinSetupViewModel_HiltModules.KeyModule.class,
          PinVerifyViewModel_HiltModules.KeyModule.class,
          ProfileViewModel_HiltModules.KeyModule.class,
          RecurringTransactionViewModel_HiltModules.KeyModule.class,
          RegisterViewModel_HiltModules.KeyModule.class,
          ReportsViewModel_HiltModules.KeyModule.class,
          SettingsViewModel_HiltModules.KeyModule.class,
          SplitBillViewModel_HiltModules.KeyModule.class,
          TransactionViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class,
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements MainActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AnalyticsViewModel_HiltModules.BindsModule.class,
          BankSetupViewModel_HiltModules.BindsModule.class,
          BudgetViewModel_HiltModules.BindsModule.class,
          CalendarViewModel_HiltModules.BindsModule.class,
          CategorySuggestionViewModel_HiltModules.BindsModule.class,
          DashboardViewModel_HiltModules.BindsModule.class,
          GoalsViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          InsightViewModel_HiltModules.BindsModule.class,
          PinResetViewModel_HiltModules.BindsModule.class,
          PinSetupViewModel_HiltModules.BindsModule.class,
          PinVerifyViewModel_HiltModules.BindsModule.class,
          ProfileViewModel_HiltModules.BindsModule.class,
          RecurringTransactionViewModel_HiltModules.BindsModule.class,
          RegisterViewModel_HiltModules.BindsModule.class,
          ReportsViewModel_HiltModules.BindsModule.class,
          SettingsViewModel_HiltModules.BindsModule.class,
          SplitBillViewModel_HiltModules.BindsModule.class,
          TransactionViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements AnalyticsFragment_GeneratedInjector,
      BankSetupFragment_GeneratedInjector,
      PinResetFragment_GeneratedInjector,
      PinSetupFragment_GeneratedInjector,
      PinVerifyFragment_GeneratedInjector,
      RegisterFragment_GeneratedInjector,
      SplashFragment_GeneratedInjector,
      AddEditBudgetDialog_GeneratedInjector,
      BudgetFragment_GeneratedInjector,
      CalendarFragment_GeneratedInjector,
      DashboardFragment_GeneratedInjector,
      GoalsFragment_GeneratedInjector,
      HelpFragment_GeneratedInjector,
      InsightsFragment_GeneratedInjector,
      OnboardingFragment_GeneratedInjector,
      ProfileFragment_GeneratedInjector,
      RecurringTransactionFragment_GeneratedInjector,
      ReportsFragment_GeneratedInjector,
      SettingsFragment_GeneratedInjector,
      AddSplitBillDialog_GeneratedInjector,
      SplitBillFragment_GeneratedInjector,
      AddEditTransactionDialog_GeneratedInjector,
      TransactionFragment_GeneratedInjector,
      FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
