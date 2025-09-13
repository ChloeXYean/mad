package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.model.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao{

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long //return new row id

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query ("SELECT COUNT(*) FROM orders")
    suspend fun getOrderCount(): Int

    //From table named orders
    @Query("SELECT * FROM orders where orderId = :orderId")
    suspend fun getOrderById(orderId:Int): OrderEntity

    @Query ("UPDATE orders SET foodStatus = :newStatus WHERE orderNo = :orderNo")
    suspend fun updateStatus(orderNo: String, newStatus: String)

    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    suspend fun getAllOrders(): List<OrderEntity>

    @Transaction //Wrap multiple queries into one safe transactions
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderWithItems(orderId: Long): OrderWithItems

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    suspend fun getAllOrdersWithItems(): Flow<List<OrderWithItems>> }