package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rasago.data.entity.OrderItemEntity


@Dao
interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: OrderItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(items: List<OrderItemEntity>)

    @Update
    suspend fun updateItem(item: OrderItemEntity)

    @Delete
    suspend fun deleteItem(item: OrderItemEntity)

    @Query("SELECT * FROM order_items where orderId = :orderId")
    suspend fun getItemsForOrder(orderId: Int): List<OrderItemEntity>

    @Query("SELECT * FROM order_items")
    suspend fun getAllItemsOrder(): List<OrderItemEntity>


}