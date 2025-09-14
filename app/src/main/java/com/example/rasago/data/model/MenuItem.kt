package com.example.rasago.data.model

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val photo: String,
    val isRecommended: Boolean,
    var quantity: Int = 0
)