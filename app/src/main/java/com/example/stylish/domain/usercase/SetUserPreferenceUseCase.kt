package com.example.stylish.domain.usercase

import com.example.stylish.domain.repository.UserPreferenceRepository
import javax.inject.Inject

class SetUserPreferenceUseCase @Inject constructor(private val userPreferenceRepository: UserPreferenceRepository) {
    suspend fun setFirstTimeLogin(isFirstTime: Boolean){
        userPreferenceRepository.setFirstTimeLogin(isFirstTime)
    }
    suspend fun setLoggedIn(isLoggedIn: Boolean){
        userPreferenceRepository.setLoggedIn(isLoggedIn)
    }
}