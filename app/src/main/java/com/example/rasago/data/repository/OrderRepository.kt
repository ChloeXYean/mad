package com.example.rasago.data.repository

import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.mapper.toEntity
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderWithItems

//Repository = “bridge” between DAO (entities, database) and ViewModel (Ui, app logic)
class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
){
    suspend fun createOrder(order: OrderEntity, items: List<OrderItemEntity>){
        val orderId = orderDao.insertOrder(order)
        items.forEach { item ->
            orderItemDao.insertItem(item.copy(orderId = orderId))
        }
    }

    //Fetch one order with its items
    suspend fun getOrderWithItem(orderId: Long): OrderWithItems {
        return orderDao.getOrderWithItems(orderId)
    }

    //Fetch all orders (like order history)
    suspend fun getAllOrders(): List<OrderWithItems>{
        return orderDao.getAllOrdersWithItem()
    }

    suspend fun getAllOrdersWithItems(): List<OrderWithItems> = orderDao.getAllOrdersWithItem()

    suspend fun insertOrder(order: Order) {
        // Convert to entity
        val orderEntity = order.toEntity()

        // Insert into DB, get back new orderId
        val orderId = orderDao.insertOrder(orderEntity)

        // Insert each item
        order.orderItems.forEach { item ->
            val itemEntity = OrderItemEntity(
                orderId = orderId,
                menuItemName = item.name,
                price = item.price,
                quantity = item.quantity
            )
            orderItemDao.insertItem(itemEntity)
        }
    }
}