package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val staffId: Int = 0,
    val name: String,
    val email: String,                
    val password: String? = null,     
    val phone: String? = null,  
    val role: String,                 // waiter, cashier, chef, manager
    val status: String,               // active / inactive
    val jobTime: Long,                // shift start or handled order time
    val createdAt: Long = System.currentTimeMillis()
)
