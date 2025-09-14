package com.example.rasago.data.repository

import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderDetails
import com.example.rasago.data.model.OrderWithItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
                        // Group the individual items from the DB to display them with quantities in the UI
                        val detailedItems = orderWithItems.items
                            .groupBy { it.menuItemId }
                            .mapNotNull { (menuItemId, items) ->
                                val menuItemEntity = menuItemEntities.find { it.id == menuItemId }
                                menuItemEntity?.let {
                                    com.example.rasago.data.model.OrderItem(
                                        id = items.first().id,
                                        menuItemId = menuItemId,
                                        name = it.name,
                                        photo = it.photo,
                                        price = it.price,
                                        quantity = items.size, // The quantity is now the count of grouped items
                                        status = items.first().status // Can be enhanced to show multiple statuses
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
            list.map { it.toOrder() }
        }
    }

    suspend fun saveOrder(
        items: List<MenuItem>,
        subtotal: Double,
        serviceCharge: Double,
        tax: Double,
        total: Double,
        orderType: String,
        paymentMethod: String,
        customerId: Int
    ) {
        val orderEntity = OrderEntity(
            orderNo = "T${System.currentTimeMillis()}",
            orderTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
            subtotal = subtotal,
            serviceCharge = serviceCharge,
            sst = tax,
            totalPayment = total,
            paymentMethod = paymentMethod,
            remarks = "",
            orderType = orderType,
            foodStatus = "Preparing",
            customerId = customerId
        )
        val newOrderId = orderDao.insertOrder(orderEntity)

        // Create a list of individual item entities.
        val orderItemEntities = mutableListOf<OrderItemEntity>()
        items.forEach { menuItem ->
            // Loop for the quantity of each menu item to create separate rows
            repeat(menuItem.quantity) {
                orderItemEntities.add(
                    OrderItemEntity(
                        orderId = newOrderId.toInt(),
                        menuItemId = menuItem.id,
                        price = menuItem.price,
                        status = "Preparing"
                    )
                )
            }
        }
        orderItemDao.insertAll(orderItemEntities)
    }
}
