package com.example.rasago.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        clearOrder()
    }

    fun clearOrder() {
        _uiState.value = OrderUiState()
    }

    // Adds one instance of the item to the cart
    fun addItemToOrder(itemToAdd: MenuItem) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.toMutableList()
            currentItems.add(itemToAdd)
            recalculateTotals(currentState.copy(orderItems = currentItems))
        }
    }

    // Alias for addItemToOrder, as increasing quantity now means adding another instance
    fun increaseItemQuantity(item: MenuItem) {
        addItemToOrder(item)
    }

    // Removes one instance of the item from the cart
    fun decreaseItemQuantity(item: MenuItem) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.toMutableList()
            // Find the first occurrence of this item to remove
            val itemToRemove = currentItems.find { it.id == item.id }
            if (itemToRemove != null) {
                currentItems.remove(itemToRemove)
            }
            recalculateTotals(currentState.copy(orderItems = currentItems))
        }
    }

    // Removes all instances of the item from the cart
    fun removeItemFromOrder(item: MenuItem) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.filter { it.id != item.id }
            recalculateTotals(currentState.copy(orderItems = currentItems))
        }
    }

    fun saveOrder(orderType: String, paymentMethod: String, customerId: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.orderItems.isNotEmpty()) {
                // The repository now expects a grouped list, so we group it here
                val groupedItems = currentState.orderItems
                    .groupBy { it.id }
                    .map { (id, items) ->
                        items.first().copy(quantity = items.size)
                    }

                orderRepository.saveOrder(
                    items = groupedItems,
                    subtotal = currentState.subtotal,
                    serviceCharge = currentState.serviceCharge,
                    tax = currentState.tax,
                    total = currentState.total,
                    orderType = orderType,
                    paymentMethod = paymentMethod,
                    customerId = customerId
                )
            }
        }
    }

    private fun recalculateTotals(state: OrderUiState): OrderUiState {
        // The list now contains individual items, so we just sum them up
        val subtotal = state.orderItems.sumOf { it.price }
        val serviceCharge = 2.00
        val tax = (subtotal + serviceCharge) * 0.06
        val total = subtotal + serviceCharge + tax

        return state.copy(
            subtotal = subtotal,
            serviceCharge = serviceCharge,
            tax = tax,
            total = total
        )
    }
}

