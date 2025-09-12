package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

//What was ordered
@Entity(tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"], //orders
            childColumns = ["orderId"], //order_items
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")])
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, //= primary key for each item row in order_items, where orderId => foreign key to link to parent order
    val orderId: Long,                // child column
    val menuItemName: String,
    val price: Double,
    val quantity: Int
)
