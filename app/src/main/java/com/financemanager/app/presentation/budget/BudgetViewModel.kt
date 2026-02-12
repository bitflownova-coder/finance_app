package com.financemanager.app.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.usecase.budget.AddBudgetUseCase
import com.financemanager.app.domain.usecase.budget.DeleteBudgetUseCase
import com.financemanager.app.domain.usecase.budget.GetBudgetsUseCase
import com.financemanager.app.domain.usecase.budget.UpdateBudgetUseCase
import com.financemanager.app.domain.usecase.budget.CheckBudgetStatusUseCase
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Budget management
 * Handles budget creation, viewing, updating, and deletion
 */
@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val addBudgetUseCase: AddBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val checkBudgetStatusUseCase: CheckBudgetStatusUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()
    
    private var budgetCollectionJob: Job? = null
    
    init {
        startBudgetCollection()
    }
    
    fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.LoadBudgets -> refreshBudgets()
            is BudgetEvent.AddBudget -> addBudget(event.budget)
            is BudgetEvent.UpdateBudget -> updateBudget(event.budget)
            is BudgetEvent.DeleteBudget -> deleteBudget(event.budget)
        }
    }
    
    private fun startBudgetCollection() {
        val userId = sessionManager.getUserId() ?: return
        
        budgetCollectionJob?.cancel()
        budgetCollectionJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            getBudgetsUseCase(userId)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load budgets"
                        )
                    }
                }
                .collect { budgets ->
                    _uiState.update {
                        it.copy(
                            budgets = budgets,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    
    private fun refreshBudgets() {
        startBudgetCollection()
    }
    
    private fun addBudget(budget: Budget) {
        val userId = sessionManager.getUserId() ?: return
        
        viewModelScope.launch {
            try {
                val budgetWithUserId = budget.copy(userId = userId)
                addBudgetUseCase(budgetWithUserId)
                // Refresh budget list
                refreshBudgets()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to add budget")
                }
            }
        }
    }
    
    private fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            try {
                updateBudgetUseCase(budget)
                // Refresh budget list
                refreshBudgets()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to update budget")
                }
            }
        }
    }
    
    private fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            try {
                deleteBudgetUseCase(budget)
                // Refresh budget list
                refreshBudgets()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to delete budget")
                }
            }
        }
    }
    
    fun getBudgetStatus(): List<CheckBudgetStatusUseCase.BudgetStatus> {
        return checkBudgetStatusUseCase(_uiState.value.budgets)
    }
}
