package com.example.rasago.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity

//Database join result = raw data rom db
data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)