package com.example.rasago.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderDetails
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _allOrders = MutableStateFlow<List<Order>>(emptyList())
    val allOrders: StateFlow<List<Order>> = _allOrders.asStateFlow()

    private val _selectedOrderDetails = MutableStateFlow<OrderDetails?>(null)
    val selectedOrderDetails: StateFlow<OrderDetails?> = _selectedOrderDetails.asStateFlow()

    init {
        loadAllOrders()
    }

    private fun loadAllOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect { orders ->
                _allOrders.value = orders
            }
        }
    }

    fun loadOrderDetails(orderId: Int) {
        viewModelScope.launch {
            // Clear previous selection while loading
            _selectedOrderDetails.value = null
            orderRepository.getOrderDetails(orderId).collect {
                _selectedOrderDetails.value = it
            }
        }
    }
}
