package com.financemanager.app.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.GoalCategory
import com.financemanager.app.domain.model.GoalStatus
import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.usecase.goal.AddContributionUseCase
import com.financemanager.app.domain.usecase.goal.AddGoalUseCase
import com.financemanager.app.domain.usecase.goal.DeleteGoalUseCase
import com.financemanager.app.domain.usecase.goal.GetActiveGoalsUseCase
import com.financemanager.app.domain.usecase.goal.GetGoalStatisticsUseCase
import com.financemanager.app.domain.usecase.goal.UpdateGoalUseCase
import com.financemanager.app.domain.repository.SavingsGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getActiveGoalsUseCase: GetActiveGoalsUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val addContributionUseCase: AddContributionUseCase,
    private val getGoalStatisticsUseCase: GetGoalStatisticsUseCase,
    private val repository: SavingsGoalRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: GoalEvent) {
        when (event) {
            is GoalEvent.LoadGoals -> loadGoals(event.userId)
            is GoalEvent.LoadStatistics -> loadStatistics(event.userId)
            is GoalEvent.AddGoalClicked -> showAddGoalDialog(event.userId)
            is GoalEvent.EditGoalClicked -> showEditGoalDialog(event.goal)
            is GoalEvent.DeleteGoalClicked -> deleteGoal(event.goal)
            is GoalEvent.SaveGoal -> saveGoal(event.goal)
            is GoalEvent.AddContributionClicked -> showContributionDialog(event.goal)
            is GoalEvent.SaveContribution -> saveContribution(event.goalId, event.amount, event.note)
            is GoalEvent.FilterByCategory -> filterByCategory(event.category)
            is GoalEvent.SortBy -> sortGoals(event.sortOption)
            is GoalEvent.ArchiveGoal -> archiveGoal(event.goalId)
            GoalEvent.DismissError -> _uiState.update { it.copy(error = null) }
            GoalEvent.DismissDialog -> dismissDialogs()
        }
    }
    
    private fun loadGoals(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getActiveGoalsUseCase(userId)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load goals"
                        )
                    }
                }
                .collect { goals ->
                    val sortedGoals = applySortingAndFiltering(goals)
                    _uiState.update { 
                        it.copy(
                            goals = sortedGoals,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
        }
    }
    
    private fun loadStatistics(userId: Long) {
        viewModelScope.launch {
            try {
                val stats = getGoalStatisticsUseCase(userId)
                _uiState.update { it.copy(statistics = stats) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to load statistics") 
                }
            }
        }
    }
    
    private fun saveGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            try {
                if (goal.goalId == 0L) {
                    addGoalUseCase(goal)
                } else {
                    updateGoalUseCase(goal)
                }
                dismissDialogs()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to save goal") 
                }
            }
        }
    }
    
    private fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            try {
                deleteGoalUseCase(goal)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to delete goal") 
                }
            }
        }
    }
    
    private fun saveContribution(goalId: Long, amount: Double, note: String) {
        viewModelScope.launch {
            try {
                addContributionUseCase(goalId, amount, note)
                dismissDialogs()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to add contribution") 
                }
            }
        }
    }
    
    private fun archiveGoal(goalId: Long) {
        viewModelScope.launch {
            try {
                repository.archiveGoal(goalId)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to archive goal") 
                }
            }
        }
    }
    
    private fun showAddGoalDialog(userId: Long) {
        val newGoal = SavingsGoal(
            userId = userId,
            name = "",
            description = "",
            targetAmount = 0.0,
            targetDate = java.time.LocalDate.now().plusMonths(6),
            category = GoalCategory.OTHER,
            priority = com.financemanager.app.domain.model.GoalPriority.MEDIUM,
            status = GoalStatus.ACTIVE
        )
        _uiState.update { 
            it.copy(
                selectedGoal = newGoal,
                isAddEditDialogVisible = true
            ) 
        }
    }
    
    private fun showEditGoalDialog(goal: SavingsGoal) {
        _uiState.update { 
            it.copy(
                selectedGoal = goal,
                isAddEditDialogVisible = true
            ) 
        }
    }
    
    private fun showContributionDialog(goal: SavingsGoal) {
        _uiState.update { 
            it.copy(
                selectedGoal = goal,
                isContributionDialogVisible = true
            ) 
        }
    }
    
    private fun dismissDialogs() {
        _uiState.update { 
            it.copy(
                selectedGoal = null,
                isAddEditDialogVisible = false,
                isContributionDialogVisible = false
            ) 
        }
    }
    
    private fun filterByCategory(category: GoalCategory?) {
        _uiState.update { it.copy(filterCategory = category) }
        val sorted = applySortingAndFiltering(_uiState.value.goals)
        _uiState.update { it.copy(goals = sorted) }
    }
    
    private fun sortGoals(sortOption: GoalSortOption) {
        _uiState.update { it.copy(sortBy = sortOption) }
        val sorted = applySortingAndFiltering(_uiState.value.goals)
        _uiState.update { it.copy(goals = sorted) }
    }
    
    private fun applySortingAndFiltering(goals: List<SavingsGoal>): List<SavingsGoal> {
        var filtered = goals
        
        // Filter by category
        _uiState.value.filterCategory?.let { category ->
            filtered = filtered.filter { it.category == category }
        }
        
        // Sort
        return when (_uiState.value.sortBy) {
            GoalSortOption.PRIORITY -> filtered.sortedWith(
                compareBy<SavingsGoal> { it.priority }.thenBy { it.targetDate }
            )
            GoalSortOption.TARGET_DATE -> filtered.sortedBy { it.targetDate }
            GoalSortOption.PROGRESS -> filtered.sortedByDescending { it.progressPercentage }
            GoalSortOption.AMOUNT -> filtered.sortedByDescending { it.targetAmount }
        }
    }
}
