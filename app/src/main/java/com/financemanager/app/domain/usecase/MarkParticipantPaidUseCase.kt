package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.repository.SplitBillRepository
import javax.inject.Inject

class MarkParticipantPaidUseCase @Inject constructor(
    private val repository: SplitBillRepository
) {
    suspend operator fun invoke(participantId: Long) {
        repository.markParticipantAsPaid(participantId)
    }
}
