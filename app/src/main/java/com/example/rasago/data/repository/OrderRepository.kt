package com.example.rasago.data.repository

import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderDetails
import com.example.rasago.data.model.OrderWithItems
import com.example.rasago.data.model.OrderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val menuItemDao: MenuItemDao
) {

    fun getOrderDetails(orderId: Int): Flow<OrderDetails?> {
        // Use flatMapLatest to switch from the order flow to a new flow that combines order and menu item details.
        return orderDao.getOrderWithItems(orderId).flatMapLatest { orderWithItems ->
            if (orderWithItems == null) {
                // If there's no order, emit null immediately.
                flowOf(null)
            } else {
                // First, get all the unique menu item IDs from the order items.
                val menuItemIds = orderWithItems.items.mapNotNull { it.menuItemId }
                if (menuItemIds.isEmpty()) {
                    // If there are no items, return the order with an empty list.
                    flowOf(OrderDetails(order = orderWithItems.order.toOrder(), items = emptyList()))
                } else {
                    // Fetch all menu item details at once using the IDs. This returns a Flow.
                    menuItemDao.getMenuItemsByIds(menuItemIds).map { menuItemEntities ->
                        // Now, map the order items to the detailed OrderItem model.
                        val detailedItems = orderWithItems.items.mapNotNull { orderItemEntity ->
                            // Find the corresponding menu item from the list we just fetched.
                            val menuItemEntity = menuItemEntities.find { it.id == orderItemEntity.menuItemId }
                            menuItemEntity?.let { menuItem ->
                                OrderItem(
                                    id = orderItemEntity.id,
                                    menuItemId = menuItem.id,
                                    name = menuItem.name,
                                    photo = menuItem.photo,
                                    price = menuItem.price,
                                    quantity = 1, // Each row in OrderItemEntity represents one item.
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

    fun getOrdersByCustomerId(customerId: Int): Flow<List<Order>> {
        // Add explicit type information to the lambda parameter to help the compiler.
        return orderDao.getOrdersByCustomerId(customerId).map { list: List<OrderWithItems> ->
            list.map { it.order.toOrder() }
        }
    }

    suspend fun getMostRecentOrderForCustomer(customerId: Int): Order? {
        return orderDao.getMostRecentOrderForCustomer(customerId)?.order?.toOrder()
    }

    suspend fun saveOrderWithItems(order: OrderEntity, items: List<OrderItemEntity>) {
        val newOrderId = orderDao.insertOrder(order)
        // Update each item with the new orderId and insert it
        val itemsWithOrderId = items.map { it.copy(orderId = newOrderId.toInt()) }
        orderItemDao.insertAll(itemsWithOrderId)
    }

    suspend fun updateOrderItemStatus(orderItemId: Int, newStatus: String) {
        orderItemDao.updateOrderItemStatus(orderItemId, newStatus)
    }
}

