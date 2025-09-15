package com.example.rasago.data.repository

import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderDetails
import com.example.rasago.data.model.OrderItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val menuItemDao: MenuItemDao,
    private val orderItemDao: OrderItemDao
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOrderDetails(orderId: Int): Flow<OrderDetails?> {
        return orderDao.getOrderWithItems(orderId).flatMapLatest { orderWithItems ->
            if (orderWithItems == null) {
                flowOf(null)
            } else {
                val menuItemIds = orderWithItems.items.mapNotNull { it.menuItemId }
                if (menuItemIds.isEmpty()) {
                    flowOf(OrderDetails(order = orderWithItems.order.toOrder(), items = emptyList()))
                } else {
                    menuItemDao.getMenuItemsByIds(menuItemIds).map { menuItemEntities ->
                        // Create a separate OrderItem for each entity from the database
                        val detailedItems = orderWithItems.items.mapNotNull { orderItemEntity ->
                            val menuItemEntity = menuItemEntities.find { it.id == orderItemEntity.menuItemId }
                            menuItemEntity?.let {
                                OrderItem(
                                    id = orderItemEntity.id,
                                    menuItemId = it.id,
                                    name = it.name,
                                    photo = it.photo,
                                    price = it.price,
                                    quantity = 1, // Each row represents a quantity of 1
                                    status = orderItemEntity.status
                                )
                            }
                        }
                        OrderDetails(order = orderWithItems.order.toOrder(), items = detailedItems)
                    }
                }
            }
        }
    }

    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrdersWithItems().map { list ->
            list.map { it.order.toOrder() }
        }
    }

    suspend fun saveOrderWithItems(order: OrderEntity, items: List<OrderItemEntity>) {
        val newOrderId = orderDao.insertOrder(order)
        val itemsWithOrderId = items.map { it.copy(orderId = newOrderId.toInt()) }
        orderItemDao.insertAll(itemsWithOrderId)
    }
}

