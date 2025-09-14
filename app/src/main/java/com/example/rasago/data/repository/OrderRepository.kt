package com.example.rasago.data.repository


import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.mapper.toEntity
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {

    suspend fun insertOrder(order: Order, orderItems: List<OrderItemEntity>): Long {
        val orderEntity = order.toEntity() // assume you have mapping extension
        val orderId = orderDao.insertOrder(orderEntity)
        val itemsWithOrderId = orderItems.map { it.copy(orderId = orderId) }
        orderItemDao.insertItem(itemsWithOrderId)
        return orderId
    }

    suspend fun getOrderById(orderId: Long): Order? {
        val orderWithItems = orderDao.getOrderWithItems(orderId)
        return orderWithItems.toOrder() // convert to your Order model
    }

    suspend fun updateOrderStatus(orderNo: String, newStatus: String) {
        orderDao.updateStatus(orderNo, newStatus)
    }

    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrdersWithItems()
            .map { list -> list.map { it.toOrder() } }
    }
}
