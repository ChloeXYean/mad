package com.example.rasago.data.model

data class MenuItem(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageRes: Int
)