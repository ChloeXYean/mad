package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.model.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderWithItems(orderId: Int): Flow<OrderWithItems?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getCount(): Int

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrdersWithItems(): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY orderTime DESC")
    fun getOrdersByCustomerId(customerId: Int): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY orderId DESC LIMIT 1")
    suspend fun getMostRecentOrderForCustomer(customerId: Int): OrderWithItems?

    @Query("UPDATE orders SET foodStatus = :newStatus WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: Int, newStatus: String)
}

