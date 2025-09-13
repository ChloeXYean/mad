package com.example.rasago.data.repository

import com.example.rasago.DummyData
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.mapper.toEntity
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderWithItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

//Repository = “bridge” between DAO (entities, database) and ViewModel (Ui, app logic)
@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
){
    suspend fun insertOrder(orderEntity: OrderEntity, orderItems: List<OrderItemEntity>): Long = withContext(Dispatchers.IO) {
        val orderId = orderDao.insertOrder(orderEntity)
        val itemsWithOrderId = orderItems.map { it.copy(orderId = orderId) }
        orderItemDao.insertItem(itemsWithOrderId)
        orderId
    }

    suspend fun updateOrder(orderEntity: OrderEntity) = withContext(Dispatchers.IO) {
        orderDao.updateOrder(orderEntity)
    }

    suspend fun deleteOrder(orderEntity: OrderEntity) = withContext(Dispatchers.IO) {
        orderDao.deleteOrder(orderEntity)
    }

    suspend fun getOrderById(orderId: Long): Order? = withContext(Dispatchers.IO) {
        orderDao.getOrderWithItems(orderId).toOrder()
    }

    suspend fun updateOrderStatus(orderNo: String, newStatus: String) = withContext(Dispatchers.IO) {
        orderDao.updateStatus(orderNo, newStatus)
    }

    suspend fun getAllOrders(): Flow<List<OrderWithItems>> {
        return orderDao.getAllOrdersWithItems()
    }
}