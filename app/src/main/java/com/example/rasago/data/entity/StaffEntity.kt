package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val staffId: Long = 0,
    val email: String,
    val phone: String,
    val name: String,
    val gender: String,
    val role: String,                 // waiter, cashier, chef
    val status: String,               // active / inactive
    val jobTime: Long                 // shift start or handled order time
)
