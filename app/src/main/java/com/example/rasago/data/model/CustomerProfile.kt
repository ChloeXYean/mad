package com.example.rasago.data.model

import androidx.annotation.DrawableRes

data class CustomerProfile(
    val name: String,
    val email: String,
    val phone: String,
    @DrawableRes val profileImageRes: Int
)