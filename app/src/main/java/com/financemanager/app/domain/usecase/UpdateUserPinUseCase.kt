package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.repository.UserRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for updating user's PIN
 */
class UpdateUserPinUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long, newPinHash: String): Result<Unit> {
        require(newPinHash.isNotBlank()) { "PIN hash cannot be blank" }
        return userRepository.updatePin(userId, newPinHash)
    }
}
