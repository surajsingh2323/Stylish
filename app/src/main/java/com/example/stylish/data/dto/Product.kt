package com.example.stylish.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = "wishlist")
@Serializable
data class Product(

    @PrimaryKey
    val id : Int,
    val title : String,
    val description: String,
    val price: Double,
    val discountPercentage: Double = 0.0,
    val rating : Double = 0.0,
    val stock : Int = 0,
    val brand: String = "",
    val category: String = "",
    val thumbnail: String = "",
    val images: List<String> = emptyList()
)