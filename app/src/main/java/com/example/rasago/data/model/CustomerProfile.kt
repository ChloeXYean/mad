package com.example.rasago.data.model

import androidx.annotation.DrawableRes

data class CustomerProfile(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val profileImageRes: Int,
    val password: String
)