package com.financemanager.app.domain.usecase.auth

import com.financemanager.app.domain.model.User
import com.financemanager.app.domain.repository.UserRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for verifying app PIN
 */
class VerifyPinUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(userId: Long, pin: String): Result<User> {
        if (pin.length != 4) {
            return Result.Error(Exception("PIN must be 4 digits"))
        }
        
        return userRepository.verifyPin(userId, pin)
    }
}
