package com.example.stylish.domain.models

data class UserPreferencesState(
    val isFirstTimeLogin: Boolean = true,
    val isLoggedIn: Boolean = false,
    val isLoading : Boolean = true
)