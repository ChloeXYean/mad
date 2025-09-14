package com.example.rasago.ui.theme.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.Order
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FoodStatusUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Expose live list of orders
    val uiState: StateFlow<FoodStatusUiState> =
        orderRepository.getAllOrdersWithItems()
            .map { orderWithItemsList ->
                FoodStatusUiState(
                    orders = orderWithItemsList.map { it.toOrder() },
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FoodStatusUiState(isLoading = true)
            )

    // Extra mutable state (selection, errors, etc.)
    private val _extraState = MutableStateFlow(FoodStatusUiState())
    val extraState: StateFlow<FoodStatusUiState> = _extraState.asStateFlow()

    fun selectOrder(order: Order) {
        _extraState.update { it.copy(selectedOrder = order) }
    }

    fun updateOrderStatus(orderNo: String, newStatus: String) {
        viewModelScope.launch {
            try {
                orderRepository.updateOrderStatus(orderNo, newStatus)
                // Room Flow auto-updates uiState, so no manual refresh needed
                _extraState.update { current ->
                    val updatedSelected = current.selectedOrder?.let {
                        if (it.no == orderNo) it.copy(status = newStatus) else it
                    }
                    current.copy(selectedOrder = updatedSelected)
                }
            } catch (e: Exception) {
                _extraState.update {
                    it.copy(errorMessage = "Failed to update status: ${e.message}")
                }
            }
        }
    }
}

//package com.example.rasago.ui.theme.order


//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.rasago.data.mapper.toOrder
//import com.example.rasago.data.model.Order
//import com.example.rasago.data.repository.OrderRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//data class FoodStatusUiState(
//    val orders: List<Order> = emptyList(),
//    val selectedOrder: Order? = null,
//    val isLoading: Boolean = true,
//    val errorMessage: String? = null
//)
//
//@HiltViewModel
//class OrderViewModel @Inject constructor(
//    private val orderRepository: OrderRepository
//) : ViewModel() {
//
//    // expose reactive UI state
//    val uiState: StateFlow<FoodStatusUiState> =
//        orderRepository.getAllOrdersWithItems()
//            .map { orderWithItemsList ->
//                FoodStatusUiState(
//                    orders = orderWithItemsList.map { it.toOrder() },
//                    isLoading = false
//                )
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = FoodStatusUiState(isLoading = true)
//            )
//
//    // hold additional "mutable" UI state (for selection, errors, etc.)
//    private val _extraState = MutableStateFlow(FoodStatusUiState())
//    val extraState: StateFlow<FoodStatusUiState> = _extraState
//
//    fun selectOrder(order: Order) {
//        _extraState.update { it.copy(selectedOrder = order) }
//    }
//
//    fun updateOrderStatus(orderNo: String, newStatus: String) {
//        viewModelScope.launch {
//            try {
//                orderRepository.updateOrderStatus(orderNo, newStatus)
//                // No need to manually refresh — Room flow auto-updates!
//                _extraState.update { current ->
//                    val updatedSelected = current.selectedOrder?.let {
//                        if (it.no == orderNo) it.copy(status = newStatus) else it
//                    }
//                    current.copy(selectedOrder = updatedSelected)
//                }
//            } catch (e: Exception) {
//                _extraState.update {
//                    it.copy(errorMessage = "Failed to update status: ${e.message}")
//                }
//            }
//        }
//    }
//}
