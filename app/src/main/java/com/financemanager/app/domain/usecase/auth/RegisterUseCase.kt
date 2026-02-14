package com.financemanager.app.domain.usecase.auth

import com.financemanager.app.domain.repository.UserRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for user registration — name + phone only
 */
class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(
        fullName: String,
        phone: String
    ): Result<Long> {
        if (fullName.isBlank()) {
            return Result.Error(Exception("Name cannot be empty"))
        }
        
        if (phone.isBlank()) {
            return Result.Error(Exception("Phone number cannot be empty"))
        }
        
        if (phone.length < 10) {
            return Result.Error(Exception("Enter a valid phone number"))
        }
        
        return userRepository.createUser(
            fullName = fullName.trim(),
            phone = phone.trim()
        )
    }
}
