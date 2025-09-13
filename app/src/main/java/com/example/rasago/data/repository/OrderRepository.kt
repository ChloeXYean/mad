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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//Repository = “bridge” between DAO (entities, database) and ViewModel (Ui, app logic)
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
){
    suspend fun getAllOrders(): List<OrderWithItems> {
        return orderDao.getAllOrdersWithItems();
    }

    suspend fun prepopulateDatabase(){
        //Check if data ady exist
        if (orderDao.getOrderCount() == 0){
            DummyData.orders.forEach { order ->
                val orderEntity = order.toEntity()
                val newOrderId = orderDao.insertOrder(orderEntity)

                order.orderItems.forEach { orderItem ->
                    val orderItemEntity = orderItem.toEntity(newOrderId)
                    orderItemDao.insertItem(orderItemEntity)
                }
            }
        }
    }

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