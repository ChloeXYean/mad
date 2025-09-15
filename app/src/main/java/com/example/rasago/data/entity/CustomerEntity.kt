package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Int = 0,
    val name: String,
    val phone: String,
    val email: String,
    val password: String? = null,     // 添加密码字段用于登录
    val gender: String,
    val isActive: Boolean = true,     // 账户状态
    val createdAt: Long = System.currentTimeMillis()
)