package com.example.rasago.ui.theme.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.DummyData
import com.example.rasago.data.model.Order
import com.example.rasago.data.repository.OrderRepository
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
class OrderViewModel @Inject constructor(private val orderRepository: OrderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodStatusUiState())
    val uiState: StateFlow<FoodStatusUiState> = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val orders = orderRepository.getAllOrders()
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
        viewModelScope.launch {
            try {
                orderRepository.updateOrderStatus(orderNo, newStatus)
                fetchOrders()
                _uiState.update { currentState ->
                    val updatedSelectedOrder = currentState.selectedOrder?.let {
                        if (it.no == orderNo) {
                            it.copy(status = newStatus)
                        } else {
                            it
                        }
                    }
                    currentState.copy(selectedOrder = updatedSelectedOrder)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to update status: ${e.message}") }
            }
        }
    }
}