package com.example.stylish.domain.usercase

import com.example.stylish.domain.repository.AuthRepository
import com.example.stylish.domain.util.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String,password: String):Result<String>{
        return repository.login(email,password)
    }
}