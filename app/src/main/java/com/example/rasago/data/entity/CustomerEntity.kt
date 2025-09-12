package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Int = 0,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val dob: Long?,                   // nullable
    val gender: String
)
