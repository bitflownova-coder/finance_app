package com.financemanager.app.domain.model

import androidx.annotation.DrawableRes

/**
 * Domain model for onboarding page
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val backgroundColor: Int
)
