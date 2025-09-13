package com.example.rasago.ui.theme.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.DummyData
import com.example.rasago.data.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FoodStatusUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FoodStatusUiState())
    val uiState: StateFlow<FoodStatusUiState> = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Using DummyData for now
                val orders = DummyData.orders
                _uiState.update {
                    it.copy(orders = orders, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message, isLoading = false)
                }
            }
        }
    }

    fun selectOrder(order: Order) {
        _uiState.update { it.copy(selectedOrder = order) }
    }

    fun updateOrderStatus(orderNo: String, newStatus: String) {
        _uiState.update { currentState ->
            // Create a new list with the updated order status
            val updatedOrders = currentState.orders.map { order ->
                if (order.no == orderNo) {
                    order.copy(status = newStatus)
                } else {
                    order
                }
            }
            val updatedSelectedOrder = if (currentState.selectedOrder?.no == orderNo) {
                currentState.selectedOrder.copy(status = newStatus)
            } else {
                currentState.selectedOrder
            }
            // Return
            currentState.copy(
                orders = updatedOrders,
                selectedOrder = updatedSelectedOrder
            )
        }
    }
}