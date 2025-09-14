//package com.example.rasago.data.repository
//
//
//import com.example.rasago.DummyData
//import com.example.rasago.data.dao.OrderDao
//import com.example.rasago.data.dao.OrderItemDao
//import com.example.rasago.data.entity.OrderItemEntity
//import com.example.rasago.data.mapper.toEntity
//import com.example.rasago.data.mapper.toOrder
//import com.example.rasago.data.model.Order
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class OrderRepository @Inject constructor(
//    private val orderDao: OrderDao,
//    private val orderItemDao: OrderItemDao
//) {
//
//    suspend fun insertOrder(order: Order, orderItems: List<OrderItemEntity>): Long {
//        val orderEntity = order.toEntity()
//        val orderId = orderDao.insertOrder(orderEntity)
//
//        val itemsWithOrderId = orderItems.map { it.copy(orderId = orderId) }
//
//        orderItemDao.insertAll(itemsWithOrderId)
//
//        return orderId
//    }
//
//    suspend fun getOrderById(orderId: Long): Order? {
//        val orderWithItems = orderDao.getOrderWithItems(orderId)
//        return orderWithItems.toOrder() // convert to your Order model
//    }
//
//    suspend fun updateOrderStatus(orderNo: String, newStatus: String) {
//        orderDao.updateStatus(orderNo, newStatus)
//    }
//
//    fun getAllOrders(): Flow<List<Order>> {
//        return orderDao.getAllOrdersWithItems()
//            .map { list -> list.map { it.toOrder() } }
//    }
//
//    suspend fun prepopulateDatabase() {
//        if (orderDao.getOrderCount() == 0) {
//            DummyData.orders.forEach { orderModel ->
//                val orderEntity = orderModel.toEntity()
//                val newOrderId = orderDao.insertOrder(orderEntity)
//                val itemEntities = orderModel.orderItems.map { orderItemModel ->
//                    orderItemModel.toEntity(orderId = newOrderId)
//                }
//                // 6. Insert all the items for this order into the database.
//                orderItemDao.insertAll(itemEntities)
//            }
//        }
//    }
//}


package com.example.rasago.data.repository

import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.model.OrderDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val menuItemDao: MenuItemDao,
) {

    /**
     * This function now fetches the complete order details. It gets the order,
     * its associated item IDs, then fetches the details for each item,
     * and combines everything into an `OrderDetails` object.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOrderDetails(orderId: Long): Flow<OrderDetails?> {
        return orderDao.getOrderWithItems(orderId).flatMapLatest { orderWithItems ->
            if (orderWithItems == null) {
                // If no order is found, emit null
                flowOf(null)
            } else {
                // Get the list of menu item IDs from the order
                val menuItemIds = orderWithItems.items.map { it.menuItemId }
                if (menuItemIds.isEmpty()) {
                    // If order has no items, return order details with an empty list
                    flowOf(OrderDetails(order = orderWithItems.order.toOrder(), items = emptyList()))
                } else {
                    // Fetch the corresponding menu item details from the database
                    menuItemDao.getMenuItemsByIds(menuItemIds).map { menuItemEntities ->
                        // Map the database entities to the UI models, including the correct quantity
                        val detailedItems = orderWithItems.items.mapNotNull { orderItemEntity ->
                            menuItemEntities.find { it.id == orderItemEntity.menuItemId }?.let { menuItemEntity ->
                                MenuItem(
                                    id = menuItemEntity.id,
                                    name = menuItemEntity.name,
                                    description = menuItemEntity.description, // Ensure description is mapped
                                    price = menuItemEntity.price,
                                    category = menuItemEntity.category,
                                    photo = menuItemEntity.photo,
                                    isRecommended = menuItemEntity.isRecommended,
                                    quantity = orderItemEntity.quantity
                                )
                            }
                        }
                        OrderDetails(order = orderWithItems.order.toOrder(), items = detailedItems)
                    }
                }
            }
        }
    }
}

