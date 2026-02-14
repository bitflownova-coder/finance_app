package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.ParticipantEntity
import com.financemanager.app.data.local.entities.SplitBillEntity
import com.financemanager.app.domain.model.Participant
import com.financemanager.app.domain.model.SplitBill
import com.financemanager.app.domain.model.SplitType
import javax.inject.Inject

class SplitBillMapper @Inject constructor() {
    
    fun toDomain(entity: SplitBillEntity, participants: List<ParticipantEntity>): SplitBill {
        return SplitBill(
            splitId = entity.splitId,
            transactionId = entity.transactionId,
            userId = entity.userId,
            totalAmount = entity.totalAmount,
            description = entity.description,
            splitType = SplitType.valueOf(entity.splitType),
            participants = participants.map { participantToDomain(it) },
            createdAt = entity.createdAt,
            isSettled = entity.isSettled
        )
    }
    
    fun toEntity(splitBill: SplitBill): SplitBillEntity {
        return SplitBillEntity(
            splitId = splitBill.splitId,
            transactionId = splitBill.transactionId,
            userId = splitBill.userId,
            totalAmount = splitBill.totalAmount,
            description = splitBill.description,
            splitType = splitBill.splitType.name,
            createdAt = splitBill.createdAt,
            isSettled = splitBill.isSettled
        )
    }
    
    fun participantToDomain(entity: ParticipantEntity): Participant {
        return Participant(
            participantId = entity.participantId,
            splitId = entity.splitId,
            name = entity.name,
            phoneNumber = entity.phoneNumber,
            shareAmount = entity.shareAmount,
            isPaid = entity.isPaid,
            paidAt = entity.paidAt
        )
    }
    
    fun participantToEntity(participant: Participant): ParticipantEntity {
        return ParticipantEntity(
            participantId = participant.participantId,
            splitId = participant.splitId,
            name = participant.name,
            phoneNumber = participant.phoneNumber,
            shareAmount = participant.shareAmount,
            isPaid = participant.isPaid,
            paidAt = participant.paidAt
        )
    }
}
