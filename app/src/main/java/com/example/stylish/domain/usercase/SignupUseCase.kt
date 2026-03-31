package com.example.stylish.domain.usercase

import com.example.stylish.domain.repository.AuthRepository
import com.example.stylish.domain.util.Result
import javax.inject.Inject

class SignupUseCase @Inject constructor (private val repository: AuthRepository){
    suspend operator fun invoke(email: String,password: String): Result<String>{
        // Rule 1: Email should not be empty
        if (email.isBlank()){
            return Result.Failure(message = "Email can't be empty")
        }
        // Rule 2: Password shouldn't be small
        if (password.length < 6){
            return Result.Failure("Password must be at least 6 characters")
        }
        // Rule 3 (optional): Email format basic check
        if (!email.contains("@") || !email.contains(".")){
            return Result.Failure("Invalid email format")
        }
        // all is good , now call repository
        return repository.signup(email,password)
    }
}