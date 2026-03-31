package com.example.stylish.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import com.example.stylish.data.local.UserPreferencesDataStore
import com.example.stylish.domain.repository.UserPreferenceRepository

class UserPreferencesRepositoryImpl (
    private val userPreferencesDataStore: UserPreferencesDataStore
    ): UserPreferenceRepository {
        override val isFirstTimeLogin: Flow<Boolean> = userPreferencesDataStore.isFirstTimeLogin
        override val isLoggedIn: Flow<Boolean> = userPreferencesDataStore.isLoggedIn
    override suspend fun setFirstTimeLogin(isFirstTime: Boolean){
        userPreferencesDataStore.setFirstTimeLogin(isFirstTime)
    }
    override suspend fun setLoggedIn(isLoggedIn: Boolean){
        userPreferencesDataStore.setLoggedIn(isLoggedIn)
    }
    }