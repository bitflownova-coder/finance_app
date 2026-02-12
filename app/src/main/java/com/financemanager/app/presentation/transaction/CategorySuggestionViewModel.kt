package com.financemanager.app.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.LearnFromTransactionHistoryUseCase
import com.financemanager.app.domain.usecase.PredictTransactionCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySuggestionViewModel @Inject constructor(
    private val predictCategoryUseCase: PredictTransactionCategoryUseCase,
    private val learnFromHistoryUseCase: LearnFromTransactionHistoryUseCase
) : ViewModel() {
    
    private val _suggestedCategories = MutableStateFlow<List<String>>(emptyList())
    val suggestedCategories: StateFlow<List<String>> = _suggestedCategories.asStateFlow()
    
    private val _confidence = MutableStateFlow(0f)
    val confidence: StateFlow<Float> = _confidence.asStateFlow()
    
    private var userId: Long = 1L // Get from session
    
    fun predictCategory(description: String, amount: Double? = null) {
        viewModelScope.launch {
            // Get predictions from keyword matching
            val predictions = predictCategoryUseCase(description, amount)
            
            if (predictions.isNotEmpty()) {
                _suggestedCategories.value = predictions.map { it.category }
                _confidence.value = predictions.first().confidence
            } else {
                // Fallback to learning from history
                val historicalCategory = learnFromHistoryUseCase.getMostCommonCategory(
                    userId, description
                )
                
                if (historicalCategory != null) {
                    _suggestedCategories.value = listOf(historicalCategory)
                    _confidence.value = 0.6f
                } else {
                    _suggestedCategories.value = emptyList()
                    _confidence.value = 0f
                }
            }
        }
    }
    
    fun getBestSuggestion(): String? {
        return _suggestedCategories.value.firstOrNull()
    }
    
    fun shouldAutoFill(): Boolean {
        return _confidence.value >= 0.8f && _suggestedCategories.value.isNotEmpty()
    }
}
