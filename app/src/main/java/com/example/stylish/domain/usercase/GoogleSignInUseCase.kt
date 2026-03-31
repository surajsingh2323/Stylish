package com.example.stylish.domain.usercase

import com.example.stylish.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount // can still use. Easy for firebase
import com.example.stylish.domain.util.Result
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(account: GoogleSignInAccount): Result<String>{
        return repository.signInWithGoogle(account) // we have to change return type to string by (Alt + Shift + enter)
    // as by default it is (AuthRepository.signInWithGoogle) return type
    }
}