package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.SavingsGoalDao
import com.financemanager.app.data.mapper.SavingsGoalMapper
import com.financemanager.app.domain.model.GoalContribution
import com.financemanager.app.domain.model.GoalStatus
import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavingsGoalRepositoryImpl @Inject constructor(
    private val goalDao: SavingsGoalDao
) : SavingsGoalRepository {
    
    override suspend fun insertGoal(goal: SavingsGoal): Long {
        val entity = SavingsGoalMapper.toEntity(goal)
        return goalDao.insertGoal(entity)
    }
    
    override suspend fun updateGoal(goal: SavingsGoal) {
        val entity = SavingsGoalMapper.toEntity(goal)
        goalDao.updateGoal(entity)
    }
    
    override suspend fun deleteGoal(goal: SavingsGoal) {
        val entity = SavingsGoalMapper.toEntity(goal)
        goalDao.deleteGoal(entity)
    }
    
    override suspend fun getGoalById(goalId: Long): SavingsGoal? {
        return goalDao.getGoalById(goalId)?.let { SavingsGoalMapper.toDomain(it) }
    }
    
    override fun getGoalByIdFlow(goalId: Long): Flow<SavingsGoal?> {
        return goalDao.getGoalByIdFlow(goalId).map { entity ->
            entity?.let { SavingsGoalMapper.toDomain(it) }
        }
    }
    
    override fun getActiveGoals(userId: Long): Flow<List<SavingsGoal>> {
        return goalDao.getActiveGoals(userId).map { entities ->
            entities.map { SavingsGoalMapper.toDomain(it) }
        }
    }
    
    override fun getGoalsByStatus(userId: Long, status: GoalStatus): Flow<List<SavingsGoal>> {
        return goalDao.getGoalsByStatus(userId, status.name).map { entities ->
            entities.map { SavingsGoalMapper.toDomain(it) }
        }
    }
    
    override fun getArchivedGoals(userId: Long): Flow<List<SavingsGoal>> {
        return goalDao.getArchivedGoals(userId).map { entities ->
            entities.map { SavingsGoalMapper.toDomain(it) }
        }
    }
    
    override fun getGoalsByCategory(userId: Long, category: String): Flow<List<SavingsGoal>> {
        return goalDao.getGoalsByCategory(userId, category).map { entities ->
            entities.map { SavingsGoalMapper.toDomain(it) }
        }
    }
    
    override suspend fun getTotalTargetAmount(userId: Long): Double {
        return goalDao.getTotalTargetAmount(userId) ?: 0.0
    }
    
    override suspend fun getTotalSavedAmount(userId: Long): Double {
        return goalDao.getTotalSavedAmount(userId) ?: 0.0
    }
    
    override suspend fun getCompletedGoalsCount(userId: Long): Int {
        return goalDao.getCompletedGoalsCount(userId)
    }
    
    override suspend fun getActiveGoalsCount(userId: Long): Int {
        return goalDao.getActiveGoalsCount(userId)
    }
    
    override suspend fun addContribution(
        goalId: Long,
        amount: Double,
        note: String,
        transactionId: Long?
    ) {
        goalDao.addContributionAndUpdateGoal(goalId, amount, note)
    }
    
    override suspend fun deleteContribution(contribution: GoalContribution) {
        val entity = SavingsGoalMapper.contributionToEntity(contribution)
        goalDao.deleteContribution(entity)
    }
    
    override fun getContributionsByGoalId(goalId: Long): Flow<List<GoalContribution>> {
        return goalDao.getContributionsByGoalId(goalId).map { entities ->
            entities.map { SavingsGoalMapper.contributionToDomain(it) }
        }
    }
    
    override suspend fun archiveGoal(goalId: Long) {
        goalDao.archiveGoal(goalId)
    }
    
    override suspend fun unarchiveGoal(goalId: Long) {
        goalDao.unarchiveGoal(goalId)
    }
    
    override suspend fun updateGoalStatus(goalId: Long, status: GoalStatus) {
        goalDao.updateGoalStatus(goalId, status.name)
    }
    
    override suspend fun markCompletedGoals() {
        goalDao.markCompletedGoals()
    }
}
