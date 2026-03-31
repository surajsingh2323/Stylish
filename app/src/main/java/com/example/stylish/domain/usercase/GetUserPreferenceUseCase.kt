package com.example.stylish.domain.usercase

import com.example.stylish.domain.repository.UserPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferenceUseCase @Inject constructor (
    private val userPreferenceRepository: UserPreferenceRepository
){
    fun isFirstTimeLogin(): Flow<Boolean> = userPreferenceRepository.isFirstTimeLogin
    fun isLoggedIn(): Flow<Boolean> = userPreferenceRepository.isLoggedIn
}