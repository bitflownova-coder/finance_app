package com.financemanager.app.di

import com.financemanager.app.data.repository.BankAccountRepositoryImpl
import com.financemanager.app.data.repository.BudgetRepositoryImpl
import com.financemanager.app.data.repository.InsightRepositoryImpl
import com.financemanager.app.data.repository.ReceiptRepositoryImpl
import com.financemanager.app.data.repository.RecurringTransactionRepositoryImpl
import com.financemanager.app.data.repository.ReportRepositoryImpl
import com.financemanager.app.data.repository.SavingsGoalRepositoryImpl
import com.financemanager.app.data.repository.SplitBillRepositoryImpl
import com.financemanager.app.data.repository.TransactionRepositoryImpl
import com.financemanager.app.data.repository.UserRepositoryImpl
import com.financemanager.app.domain.repository.BankAccountRepository
import com.financemanager.app.domain.repository.BudgetRepository
import com.financemanager.app.domain.repository.InsightRepository
import com.financemanager.app.domain.repository.ReceiptRepository
import com.financemanager.app.domain.repository.RecurringTransactionRepository
import com.financemanager.app.domain.repository.ReportRepository
import com.financemanager.app.domain.repository.SavingsGoalRepository
import com.financemanager.app.domain.repository.SplitBillRepository
import com.financemanager.app.domain.repository.TransactionRepository
import com.financemanager.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindBankAccountRepository(
        bankAccountRepositoryImpl: BankAccountRepositoryImpl
    ): BankAccountRepository
    
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
    
    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository
    
    @Binds
    @Singleton
    abstract fun bindRecurringTransactionRepository(
        recurringTransactionRepositoryImpl: RecurringTransactionRepositoryImpl
    ): RecurringTransactionRepository
    
    @Binds
    @Singleton
    abstract fun bindInsightRepository(
        insightRepositoryImpl: InsightRepositoryImpl
    ): InsightRepository
    
    @Binds
    @Singleton
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository
    
    @Binds
    @Singleton
    abstract fun bindSavingsGoalRepository(
        savingsGoalRepositoryImpl: SavingsGoalRepositoryImpl
    ): SavingsGoalRepository
    
    @Binds
    @Singleton
    abstract fun bindReceiptRepository(
        receiptRepositoryImpl: ReceiptRepositoryImpl
    ): ReceiptRepository
    
    @Binds
    @Singleton
    abstract fun bindSplitBillRepository(
        splitBillRepositoryImpl: SplitBillRepositoryImpl
    ): SplitBillRepository
}
