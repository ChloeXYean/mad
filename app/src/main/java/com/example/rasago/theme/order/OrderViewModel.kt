package com.example.rasago.ui.theme.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.mapper.toOrder
import com.example.rasago.data.model.Order
import com.example.rasago.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class OrderViewModel @Inject constructor(private val orderRepository: OrderRepository) : ViewModel(){
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    fun loadOrders(){
        viewModelScope.launch {
            val dbOrders = orderRepository.getAllOrdersWithItems() // List <OrderWithItem>
            _orders.value = dbOrders.map { it.toOrder() } // Convert to List<Order>
        }
    }
    fun addOrder(order: Order){
        viewModelScope.launch{
            orderRepository.insertOrder(order)
            loadOrders() //Refresh
        }
    }
}
