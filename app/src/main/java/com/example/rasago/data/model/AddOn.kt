package com.example.rasago.data.model

data class AddOn(
    val name: String,
    val price: Float,
    val imageRes: Int,
    var quantity: Int = 0
)