package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Long = 0,
    val name: String,
    val phone: String,
    val email: String,
    val gender: String,
    val profileImageRes: Int,
    val password: String
)
