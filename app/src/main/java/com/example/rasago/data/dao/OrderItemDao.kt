package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rasago.data.entity.OrderItemEntity

@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orderItems: List<OrderItemEntity>)

    @Query("UPDATE order_items SET status = :newStatus WHERE id = :orderItemId")
    suspend fun updateOrderItemStatus(orderItemId: Int, newStatus: String)
}

