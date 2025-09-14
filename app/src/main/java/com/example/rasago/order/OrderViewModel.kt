package com.example.rasago.order

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderItem
import com.example.rasago.data.repository.OrderRepository
import com.example.rasago.order.OrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState

    private val _orders = mutableStateListOf<Order>()
    val orders: List<Order> = _orders

    private val _cartItems = mutableStateListOf<OrderItem>()
    val cartItems: List<OrderItem> = _cartItems

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collectLatest { list ->
                _orders.clear()
                _orders.addAll(list)
                _uiState.value = _uiState.value.copy(orders = list)
            }
        }
    }

    fun addOrder(order: Order, orderItems: List<OrderItemEntity>) {
        viewModelScope.launch {
            orderRepository.insertOrder(order, orderItems)
            loadOrders() // refresh list
        }
    }

    fun addToCart(item: OrderItem) {
        val existingIndex = _cartItems.indexOfFirst { it.id == item.id }
        if (existingIndex >= 0) {
            val existing = _cartItems[existingIndex]
            _cartItems[existingIndex] = existing.copy(quantity = existing.quantity + item.quantity)
        } else {
            _cartItems.add(item)
        }
        _uiState.value = _uiState.value.copy(cartItems = _cartItems.toList())
    }

    fun removeFromCart(itemId: Long) {
        _cartItems.removeAll { it.id == itemId }
        _uiState.value = _uiState.value.copy(cartItems = _cartItems.toList())
    }

    fun clearCart() {
        _cartItems.clear()
        _uiState.value = _uiState.value.copy(cartItems = emptyList())
    }

    fun selectOrder(order: Order) {
        _uiState.value = _uiState.value.copy(selectedOrder = order)
    }

    fun updateOrderStatus(orderNo: String, newStatus: String) {
        val updatedOrders = _uiState.value.orders.map { order ->
            if (order.no == orderNo) order.copy(status = newStatus) else order
        }
        _uiState.value = _uiState.value.copy(
            orders = updatedOrders,
            selectedOrder = _uiState.value.selectedOrder?.let {
                if (it.no == orderNo) it.copy(status = newStatus) else it
            }
        )
    }
}
