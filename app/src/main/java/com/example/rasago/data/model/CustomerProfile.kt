package com.example.rasago.data.model

data class CustomerProfile(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val profileImageRes: Int,
    val password: String
)