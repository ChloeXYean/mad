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

    /**
     * Edits an existing item in the cart.
     * @param itemIndex The index of the item to edit in the orderItems list.
     * @param newAddOns The updated list of selected add-ons.
     * @param newQuantity The updated quantity for the item.
     */
    fun editItem(itemIndex: Int, newAddOns: List<AddOn>, newQuantity: Int) {
        _uiState.update { currentState ->
            val currentItems = currentState.orderItems.toMutableList()
            // Ensure the index is valid before attempting to update.
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

    fun removeItemFromOrder(cartItem: CartItem) {
        _uiState.update { currentState ->
            val updatedItems = currentState.orderItems.filter { it != cartItem }
            recalculateTotals(currentState.copy(orderItems = updatedItems))
        }
    }

    private fun recalculateTotals(state: OrderUiState): OrderUiState {
        val subtotal = state.orderItems.sumOf { it.calculateTotalPrice().toDouble() }
        val serviceCharge = subtotal * 0.1
        val tax = (subtotal + serviceCharge) * 0.06
        val total = subtotal + serviceCharge + tax

        return state.copy(
            subtotal = subtotal,
            serviceCharge = serviceCharge,
            tax = tax,
            total = total
        )
    }

    /**
     * Saves the final order to the database.
     * Note: This version saves each item quantity as a separate row and cannot save add-ons.
     */
    fun saveOrder(customerId: Int, orderType: String = "Dine-In", paymentMethod: String = "Cash") {
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
                    paymentMethod = paymentMethod,
                    remarks = "Thank you for your order!",
                    orderType = orderType,
                    foodStatus = "Preparing",
                    customerId = customerId
                )

                // This logic correctly saves main items but cannot save add-ons due to DB structure.
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

