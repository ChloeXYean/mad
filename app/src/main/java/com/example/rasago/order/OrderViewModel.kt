//package com.example.rasago.order
//
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.*
//import com.example.rasago.data.entity.OrderItemEntity
//import com.example.rasago.data.model.Order
//import com.example.rasago.data.model.OrderItem
//import com.example.rasago.data.repository.OrderRepository
//import com.example.rasago.order.OrderUiState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class OrderViewModel @Inject constructor(
//    private val orderRepository: OrderRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(OrderUiState())
//    val uiState: StateFlow<OrderUiState> = _uiState
//
//    private val _orders = mutableStateListOf<Order>()
//    val orders: List<Order> = _orders
//
//    private val _cartItems = mutableStateListOf<OrderItem>()
//    val cartItems: List<OrderItem> = _cartItems
//
//    init {
//        loadOrders()
//    }
//
//    fun loadOrders() {
//        viewModelScope.launch {
//            orderRepository.getAllOrders().collectLatest { list ->
//                _orders.clear()
//                _orders.addAll(list)
//                _uiState.value = _uiState.value.copy(orders = list)
//            }
//        }
//    }
//
//    fun addOrder(order: Order, orderItems: List<OrderItemEntity>) {
//        viewModelScope.launch {
//            orderRepository.insertOrder(order, orderItems)
//            loadOrders() // refresh list
//        }
//    }
//
//    fun addToCart(item: OrderItem) {
//        val existingIndex = _cartItems.indexOfFirst { it.id == item.id }
//        if (existingIndex >= 0) {
//            val existing = _cartItems[existingIndex]
//            _cartItems[existingIndex] = existing.copy(quantity = existing.quantity + item.quantity)
//        } else {
//            _cartItems.add(item)
//        }
//        _uiState.value = _uiState.value.copy(cartItems = _cartItems.toList())
//    }
//
//    fun removeFromCart(itemId: Long) {
//        _cartItems.removeAll { it.id == itemId }
//        _uiState.value = _uiState.value.copy(cartItems = _cartItems.toList())
//    }
//
//    fun clearCart() {
//        _cartItems.clear()
//        _uiState.value = _uiState.value.copy(cartItems = emptyList())
//    }
//
//    fun selectOrder(order: Order) {
//        _uiState.value = _uiState.value.copy(selectedOrder = order)
//    }
//
//    fun updateOrderStatus(orderNo: String, newStatus: String) {
//        val updatedOrders = _uiState.value.orders.map { order ->
//            if (order.no == orderNo) order.copy(status = newStatus) else order
//        }
//        _uiState.value = _uiState.value.copy(
//            orders = updatedOrders,
//            selectedOrder = _uiState.value.selectedOrder?.let {
//                if (it.no == orderNo) it.copy(status = newStatus) else it
//            }
//        )
//    }
//}

package com.example.rasago.order

import androidx.lifecycle.ViewModel
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        // When the ViewModel is created, we start with a fresh, empty order.
        // This resolves the crash that occurred when trying to load a non-existent order.
        clearOrder()
    }

    /**
     * Clears the current order and resets all totals to zero.
     */
    fun clearOrder() {
        _uiState.value = OrderUiState()
    }

    /**
     * Adds a selected menu item to the current order.
     * If the item already exists, its quantity is incremented.
     * After adding, totals are recalculated.
     */
    fun addItemToOrder(itemToAdd: MenuItem) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.toMutableList()
            val existingItem = currentItems.find { it.id == itemToAdd.id }

            if (existingItem != null) {
                // Item exists, so we update its quantity
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                val itemIndex = currentItems.indexOf(existingItem)
                currentItems[itemIndex] = updatedItem
            } else {
                // This is a new item, so we add it to the list with a quantity of 1
                currentItems.add(itemToAdd.copy(quantity = 1))
            }
            // Recalculate totals and return the new state
            recalculateTotals(currentState.copy(orderItems = currentItems))
        }
    }

    /**
     * A private helper function to recalculate all totals based on the current list of items.
     * This ensures calculations are consistent and centralized.
     */
    private fun recalculateTotals(state: OrderUiState): OrderUiState {
        val items = state.orderItems
        val subtotal = items.sumOf { it.price * it.quantity }
        val serviceCharge = 2.00 // A fixed service charge
        val tax = (subtotal + serviceCharge) * 0.06 // 6% SST
        val total = subtotal + serviceCharge + tax

        return state.copy(
            subtotal = subtotal,
            serviceCharge = serviceCharge,
            tax = tax,
            total = total
        )
    }
}
