package com.example.stylish.presentation.Auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylish.domain.usercase.GoogleSignInUseCase
import com.example.stylish.domain.usercase.LoginUseCase
import com.example.stylish.domain.usercase.SetUserPreferenceUseCase
import com.example.stylish.domain.usercase.SignupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.stylish.domain.util.Result
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<Result<String>>(Result.Idle)
    val authState: StateFlow<Result<String>> = _authState.asStateFlow()

    fun login(email: String,password:String){
        _authState.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = loginUseCase(email,password)
                _authState.value = result
                if (result is Result.Success){
                    setUserPreferenceUseCase.setLoggedIn(true)
                    setUserPreferenceUseCase.setFirstTimeLogin(false)
                }
            } catch (e: Exception){
                _authState.value = Result.Failure(e.message ?: "Login failed")
            }
        }
    }
    fun signup(email: String,password: String){
        _authState.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = signupUseCase(email,password)
                _authState.value = result
                if (result is Result.Success){
                    // Decide auto login after signup ?
                    setUserPreferenceUseCase.setLoggedIn(true)
                    setUserPreferenceUseCase.setFirstTimeLogin(false)
                }
            }catch (e: Exception){
                _authState.value = Result.Failure(e.message ?: "Signup failed")
            }
        }
    }
    fun signInWithGoogle(account: GoogleSignInAccount){
        Log.d("AuthViewModel","Starting Google sign-in for: ${account.email}")
        _authState.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = googleSignInUseCase(account)
                Log.d("AuthViewModel","GoogleSignInUseCase result: $result")
                _authState.value = result
                if (result is Result.Success){
                    setUserPreferenceUseCase.setLoggedIn(true)
                    setUserPreferenceUseCase.setFirstTimeLogin(false)
                }else if (result is Result.Failure){
                    Log.e("AuthViewModel","Google sign-in failed: ${result.message}")
                }
            } catch (e:Exception){
                Log.e("AuthViewModel","Exception in Google sign-in: ${e.message}",e)
                _authState.value = Result.Failure(e.message ?: "Google sign-in failed")
            }
        }
    }

    // Fix: reset state to avoid repeated navigation
    fun resetAuthState(){
        _authState.value = Result.Idle
    }
}