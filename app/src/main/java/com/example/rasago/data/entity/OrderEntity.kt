package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Main Transaction -> Payment?
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Long = 0L, //like int orderId = 0, but it is long, because onConflict returns long, not int
    val orderNo: String,
    val orderTime: Long,
    val subtotal: Double,
    val serviceCharge: Double,
    val sst: Double,
    val totalPayment: Double,
    val paymentMethod: String,       // cash, card, e-wallet
    val remarks: String?,
    val orderType: String,           // dine-in / takeaway
    val foodStatus: String           // preparing / done / cancelled
)
