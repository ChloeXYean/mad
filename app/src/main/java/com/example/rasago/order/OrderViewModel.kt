package com.example.rasago.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.model.AddOn
import com.example.rasago.data.model.CartItem
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun clearOrder() {
        _uiState.value = OrderUiState()
    }

    fun addItem(menuItem: MenuItem, addOns: List<AddOn>, quantity: Int) {
        val newItem = CartItem(
            menuItem = menuItem,
            quantity = quantity,
            selectedAddOns = addOns.filter { it.quantity > 0 }
        )
        _uiState.update { currentState ->
            val updatedItems = currentState.orderItems + newItem
            recalculateTotals(currentState.copy(orderItems = updatedItems))
        }
    }

    fun editItem(itemIndex: Int, newAddOns: List<AddOn>, newQuantity: Int) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.toMutableList()
            if (itemIndex >= 0 && itemIndex < currentItems.size) {
                val itemToEdit = currentItems[itemIndex]
                val updatedItem = itemToEdit.copy(
                    quantity = newQuantity,
                    selectedAddOns = newAddOns.filter { it.quantity > 0 }
                )
                currentItems[itemIndex] = updatedItem
            }
            recalculateTotals(currentState.copy(orderItems = currentItems))
        }
    }

    fun increaseItemQuantity(cartItem: CartItem) {
        _uiState.update { currentState ->
            val updatedItems = currentState.orderItems.map {
                if (it == cartItem) {
                    it.copy(quantity = it.quantity + 1)
                } else {
                    it
                }
            }
            recalculateTotals(currentState.copy(orderItems = updatedItems))
        }
    }

    fun decreaseItemQuantity(cartItem: CartItem) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.toMutableList()
            val itemToUpdate = currentItems.find { it == cartItem }

            if (itemToUpdate != null) {
                if (itemToUpdate.quantity > 1) {
                    val updatedItem = itemToUpdate.copy(quantity = itemToUpdate.quantity - 1)
                    val index = currentItems.indexOf(itemToUpdate)
                    currentItems[index] = updatedItem
                } else {
                    currentItems.remove(itemToUpdate)
                }
            }
            recalculateTotals(currentState.copy(orderItems = currentItems))
        }
    }

    fun setOrderType(orderType: String) {
        _uiState.update { currentState ->
            recalculateTotals(currentState.copy(orderType = orderType))
        }
    }

    fun setPaymentMethod(methodIndex: Int) {
        val paymentMethod = when (methodIndex) {
            0 -> "QR Scan"
            1 -> "Cash"
            2 -> "Card"
            else -> "QR Scan"
        }
        _uiState.update { it.copy(paymentMethod = paymentMethod) }
    }

    private fun recalculateTotals(state: OrderUiState): OrderUiState {
        val subtotal = state.orderItems.sumOf { it.calculateTotalPrice().toDouble() }
        val serviceCharge = subtotal * 0.1
        val takeAwayCharge = if (state.orderType == "Take-away") 0.50 else 0.0
        val tax = (subtotal + serviceCharge + takeAwayCharge) * 0.06
        val total = subtotal + serviceCharge + tax + takeAwayCharge

        return state.copy(
            subtotal = subtotal,
            serviceCharge = serviceCharge,
            tax = tax,
            takeAwayCharge = takeAwayCharge,
            total = total
        )
    }

    fun saveOrder(customerId: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.orderItems.isNotEmpty()) {
                val orderEntity = OrderEntity(
                    orderNo = "T${System.currentTimeMillis()}",
                    orderTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
                    subtotal = currentState.subtotal,
                    serviceCharge = currentState.serviceCharge,
                    sst = currentState.tax,
                    totalPayment = currentState.total,
                    paymentMethod = currentState.paymentMethod,
                    remarks = "Take-away charge: RM ${currentState.takeAwayCharge}",
                    orderType = currentState.orderType,
                    foodStatus = "Preparing",
                    customerId = customerId
                )

                val orderItemEntities = currentState.orderItems.flatMap { cartItem ->
                    List(cartItem.quantity) {
                        OrderItemEntity(
                            orderId = 0, // Will be updated by repository
                            menuItemId = cartItem.menuItem.id,
                            price = cartItem.menuItem.price,
                            status = "Preparing"
                        )
                    }
                }

                orderRepository.saveOrderWithItems(orderEntity, orderItemEntities)
            }
        }
    }
}

