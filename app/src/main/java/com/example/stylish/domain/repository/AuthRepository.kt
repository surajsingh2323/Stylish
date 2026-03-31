package com.example.stylish.domain.repository

import com.example.stylish.domain.util.Result
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthRepository {
    suspend fun login(email: String,password:String): com.example.stylish.domain.util.Result<String>
    suspend fun signup(email: String,password: String): com.example.stylish.domain.util.Result<String>
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<String>
}