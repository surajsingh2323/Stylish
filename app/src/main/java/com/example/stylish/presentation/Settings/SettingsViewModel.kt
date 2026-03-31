package com.example.stylish.presentation.Settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylish.domain.models.UserProfile
import com.example.stylish.domain.repository.UserSettingsRepository
import com.example.stylish.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val userProfile: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val profilePhotoUrl: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = firebaseAuth.currentUser
        val email = currentUser?.email ?: ""
        val photoUrl = currentUser?.photoUrl?.toString()

        _state.value = _state.value.copy(
            userProfile = _state.value.userProfile.copy(emailAddress = email),
            profilePhotoUrl = photoUrl
        )
    }

    fun updateUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                val userId = firebaseAuth.currentUser?.uid

                if (userId == null){
                    _state.value = _state.value.copy(
                        isSaving = false,
                        saveSuccess = false,
                        error = "User not logged in"
                    )
                    return@launch
                }
                _state.value = _state.value.copy(isSaving = true, saveSuccess = false, error = null)

                val profileWithUserId = userProfile.copy(userId = userId)

                when (val result = userSettingsRepository.saveUserProfile(profileWithUserId)){
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            userProfile = profileWithUserId,
                            isSaving = false,
                            saveSuccess = true,
                            error = null
                        )
                    }
                    is Result.Failure -> {
                        _state.value = _state.value.copy(
                            isSaving = false,
                            saveSuccess = false,
                            error = result.message
                        )
                    }
                    else -> {
                        _state.value = _state.value.copy(
                            isSaving = false,
                            saveSuccess = false,
                            error = "Unknown error occurred"
                        )
                    }
                }
            } catch (e: Exception){
                _state.value = _state.value.copy(
                    isSaving = false,
                    saveSuccess = false,
                    error = e.message ?: "Failed to save profile"
                )
            }
        }
    }

    fun resetSaveSuccess() {
        _state.value = _state.value.copy(saveSuccess = false)
    }

    fun clearError(){
        _state.value = _state.value.copy(error = null)
    }
}