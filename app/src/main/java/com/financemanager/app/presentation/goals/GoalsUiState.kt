package com.financemanager.app.presentation.goals

import com.financemanager.app.domain.model.GoalCategory
import com.financemanager.app.domain.model.GoalPriority
import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.usecase.goal.GoalStatistics

data class GoalsUiState(
    val goals: List<SavingsGoal> = emptyList(),
    val statistics: GoalStatistics? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGoal: SavingsGoal? = null,
    val isAddEditDialogVisible: Boolean = false,
    val isContributionDialogVisible: Boolean = false,
    val filterCategory: GoalCategory? = null,
    val sortBy: GoalSortOption = GoalSortOption.PRIORITY
)

enum class GoalSortOption(val displayName: String) {
    PRIORITY("Priority"),
    TARGET_DATE("Target Date"),
    PROGRESS("Progress"),
    AMOUNT("Amount")
}

sealed class GoalEvent {
    data class LoadGoals(val userId: Long) : GoalEvent()
    data class LoadStatistics(val userId: Long) : GoalEvent()
    data class AddGoalClicked(val userId: Long) : GoalEvent()
    data class EditGoalClicked(val goal: SavingsGoal) : GoalEvent()
    data class DeleteGoalClicked(val goal: SavingsGoal) : GoalEvent()
    data class SaveGoal(val goal: SavingsGoal) : GoalEvent()
    data class AddContributionClicked(val goal: SavingsGoal) : GoalEvent()
    data class SaveContribution(val goalId: Long, val amount: Double, val note: String) : GoalEvent()
    data class FilterByCategory(val category: GoalCategory?) : GoalEvent()
    data class SortBy(val sortOption: GoalSortOption) : GoalEvent()
    data class ArchiveGoal(val goalId: Long) : GoalEvent()
    object DismissError : GoalEvent()
    object DismissDialog : GoalEvent()
}
