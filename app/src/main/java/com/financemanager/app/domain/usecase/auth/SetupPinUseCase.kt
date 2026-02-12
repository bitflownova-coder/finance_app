package com.financemanager.app.domain.usecase.auth

import com.financemanager.app.domain.repository.UserRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for setting up app PIN
 */
class SetupPinUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(userId: Long, pin: String, confirmPin: String): Result<Unit> {
        if (pin.length != 4) {
            return Result.Error(Exception("PIN must be 4 digits"))
        }
        
        if (!pin.all { it.isDigit() }) {
            return Result.Error(Exception("PIN must contain only digits"))
        }
        
        if (pin != confirmPin) {
            return Result.Error(Exception("PINs do not match"))
        }
        
        return userRepository.setupPin(userId, pin)
    }
}
