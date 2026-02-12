package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.UserEntity
import com.financemanager.app.domain.model.User

/**
 * Mapper to convert between UserEntity (data layer) and User (domain layer)
 */
object UserMapper {
    
    fun toDomain(entity: UserEntity): User {
        return User(
            userId = entity.userId,
            fullName = entity.fullName,
            phone = entity.phone,
            upiId = entity.upiId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isActive = entity.isActive
        )
    }
    
    fun toEntity(user: User, pinHash: String = ""): UserEntity {
        return UserEntity(
            userId = user.userId,
            pinHash = pinHash,
            fullName = user.fullName,
            phone = user.phone,
            upiId = user.upiId,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            isActive = user.isActive
        )
    }
}
