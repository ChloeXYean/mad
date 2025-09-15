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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ordersFlow = savedStateHandle.get<Int>("customerId")?.let { customerId ->
        orderRepository.getOrdersByCustomerId(customerId)
    } ?: orderRepository.getAllOrders()

    val allOrders: StateFlow<List<Order>> = ordersFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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
            orderRepository.getOrderDetails(orderId).collect {
                _selectedOrderDetails.value = it
            }
        }
    }

    fun updateOrderItemStatus(orderItemId: Int, newStatus: String) {
        viewModelScope.launch {
            try {
                orderRepository.updateOrderItemStatus(orderItemId, newStatus)
                // After updating, reload the order details to get the fresh data
                _selectedOrderDetails.value?.order?.orderId?.let {
                    loadOrderDetails(it)
                }
            } catch (e: Exception) {
                // Handle or log the error appropriately
            }
        }
    }
}

