package com.example.stylish.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryItem(
    val slug: String,
    val name: String,
    val url: String
)