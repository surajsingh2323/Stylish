package com.example.stylish.domain.repository

import com.example.stylish.domain.models.UserProfile
import kotlinx.coroutines.flow.Flow
import com.example.stylish.domain.util.Result

interface UserSettingsRepository {
    suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit>
    fun getUserProfile(userId:String): Flow<Result<UserProfile>>
    suspend fun updateUserProfile(userId: String,updates: Map<String, Any>): Result<Unit>
}