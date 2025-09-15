package com.example.rasago.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderDetails
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _allOrders = MutableStateFlow<List<Order>>(emptyList())
    val allOrders: StateFlow<List<Order>> = _allOrders.asStateFlow()
    
    init {
        loadOrders()
    }
    
    private fun loadOrders() {
        viewModelScope.launch {
            val customerId = savedStateHandle.get<Int>("customerId")
            println("DEBUG: OrderHistoryViewModel - customerId: $customerId")
            
            val ordersFlow = if (customerId == -1) {
                // Staff view - show all orders
                println("DEBUG: OrderHistoryViewModel - Loading all orders for staff")
                orderRepository.getAllOrders()
            } else if (customerId != null) {
                // Customer view - show specific customer orders
                println("DEBUG: OrderHistoryViewModel - Loading orders for customer: $customerId")
                orderRepository.getOrdersByCustomerId(customerId)
            } else {
                // Default - show all orders
                println("DEBUG: OrderHistoryViewModel - No customerId, loading all orders")
                orderRepository.getAllOrders()
            }
            
            ordersFlow.collect { orders ->
                println("DEBUG: OrderHistoryViewModel - Received ${orders.size} orders")
                _allOrders.value = orders
            }
        }
    }

    private val _selectedOrderDetails = MutableStateFlow<OrderDetails?>(null)
    val selectedOrderDetails: StateFlow<OrderDetails?> = _selectedOrderDetails.asStateFlow()

    // State to trigger navigation from the UI
    private val _navigateToOrderStatus = MutableStateFlow<Int?>(null)
    val navigateToOrderStatus: StateFlow<Int?> = _navigateToOrderStatus.asStateFlow()

    fun onOrdersTabClicked(customerId: Int) {
        viewModelScope.launch {
            val mostRecentOrder = orderRepository.getMostRecentOrderForCustomer(customerId)
            if (mostRecentOrder != null) {
                _navigateToOrderStatus.value = mostRecentOrder.orderId
            } else {
                // Use a special value to indicate navigation to the (empty) history screen
                _navigateToOrderStatus.value = -1
            }
        }
    }

    // Call this after navigation has been handled to reset the trigger
    fun onNavigationComplete() {
        _navigateToOrderStatus.value = null
    }

    fun loadOrderDetails(orderId: Int) {
        viewModelScope.launch {
            println("DEBUG: OrderHistoryViewModel - Loading order details for order $orderId")
            orderRepository.getOrderDetails(orderId).collect { orderDetails ->
                println("DEBUG: OrderHistoryViewModel - Received order details: ${orderDetails?.order?.orderNo}")
                _selectedOrderDetails.value = orderDetails
            }
        }
    }

    fun updateOrderItemStatus(orderItemId: Int, newStatus: String) {
        viewModelScope.launch {
            try {
                println("DEBUG: OrderHistoryViewModel - Updating item $orderItemId to status $newStatus")
                orderRepository.updateOrderItemStatus(orderItemId, newStatus)
                println("DEBUG: OrderHistoryViewModel - Status updated successfully")
                
                // After updating, reload the order details to get the fresh data
                _selectedOrderDetails.value?.order?.orderId?.let { orderId ->
                    println("DEBUG: OrderHistoryViewModel - Reloading order details for order $orderId")
                    loadOrderDetails(orderId)
                }
                
                // Reload all orders to reflect the status change
                println("DEBUG: OrderHistoryViewModel - Reloading all orders after status update")
                loadOrders()
            } catch (e: Exception) {
                println("DEBUG: OrderHistoryViewModel - Error updating status: ${e.message}")
            }
        }
    }
}

