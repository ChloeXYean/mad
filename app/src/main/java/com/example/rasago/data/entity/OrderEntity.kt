package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Main Transaction -> Payment?
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val orderNo: String,
    val orderTime: String,
    val subtotal: Double,
    val serviceCharge: Double,
    val sst: Double,
    val totalPayment: Double,
    val paymentMethod: String,
    val remarks: String?,
    val orderType: String,           // dine-in / takeaway
    val foodStatus: String,           // preparing / done / cancelled
    val customerId: Int
)
