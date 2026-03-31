package com.example.stylish.presentation.UserPreferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylish.domain.models.UserPreferencesState
import com.example.stylish.domain.usercase.GetUserPreferenceUseCase
import com.example.stylish.domain.usercase.SetUserPreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor (
    private val getUserPreferenceUseCase: GetUserPreferenceUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase
): ViewModel(){
    private val _state = MutableStateFlow(UserPreferencesState())
    val state: StateFlow<UserPreferencesState> = _state.asStateFlow()

    init{
        observeUserPreferences()
    }

    private fun observeUserPreferences(){
        viewModelScope.launch{
            combine(
                getUserPreferenceUseCase.isFirstTimeLogin(), // false
                getUserPreferenceUseCase.isLoggedIn() // true
            ){ isFirstTime , isLoggedIn ->
                Log.d("com.example.stylish.presentation.UserPreferences.UserPreferencesViewModel","DataStore values changed - isFirstTime: $isFirstTime, isLoggedIn: $isLoggedIn")
                UserPreferencesState(
                    isFirstTimeLogin = isFirstTime,
                    isLoggedIn = isLoggedIn,
                    isLoading = false
                )
            }.collect { newState ->
                Log.d("com.example.stylish.presentation.UserPreferences.UserPreferencesViewModel", "Updating state - isFirstTime: ${newState.isFirstTimeLogin}, isLoggedIn: ${newState.isLoggedIn}")
                _state.value = newState

            }
        }
    }
}
