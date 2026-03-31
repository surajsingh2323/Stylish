package com.example.stylish.data.repositoryimpl

import android.util.Log
import com.example.stylish.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.example.stylish.domain.util.Result
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepositoryImpl (
    private val firebaseAuth: FirebaseAuth // creating object of firebase function or class
): AuthRepository{

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            // success & failure also given by firebase however we are using Success & Failure defined in our Result class
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Result.Success("Login Successful")// manual data emit karta hai Result class mai
        }catch (e: Exception){
            Result.Failure(e.localizedMessage ?:"Unknown error during login")
        }
    }

    override suspend fun signup(email: String, password: String): Result<String> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            Result.Success("Signup successful")
        } catch (e: Exception){
            Result.Failure(e.localizedMessage?:"Unknown error during signup")
        }
    }

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<String> {
        return try {
            Log.d("AuthRepositoryImpl","Starting Google sign-in with account: ${account.email}")
            Log.d("AuthRepositoryImpl","ID Token avialable: ${account.idToken != null}")

            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            Log.d("AuthRepositoryImpl","Created credential, signing in with Firebase")

            val authResult = firebaseAuth.signInWithCredential(credential).await()
            Log.d("AuthRepositoryImpl","Firebase sign-in successful , User: ${authResult.user?.email}")

            Result.Success("Google sign-in successful")

        }catch (e: Exception){
            Log.e("AuthRepositoryImpl","Google sign-in failed:${e.message}",e)
            Result.Failure(e.localizedMessage ?: "Unknown error during Google sign-in")
        }

    }
}