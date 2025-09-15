package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val staffId: Int = 0,
    val name: String,
    val email: String,                // 邮箱作为主键用于登录
    val password: String? = null,     // 添加密码字段用于登录
    val phone: String? = null,  // 添加电话号码字段
    val role: String,                 // waiter, cashier, chef, manager
    val status: String,               // active / inactive
    val jobTime: Long,                // shift start or handled order time
    val createdAt: Long = System.currentTimeMillis()
)
