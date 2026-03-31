package com.example.stylish.domain.models

data class UserProfile(
    val userId: String = "",
    val emailAddress: String = "",
    val password: String = "",
    val pincode: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val bankAccountNumber: String = "",
    val accountHolderName: String = "",
    val ifscCode: String = ""
)