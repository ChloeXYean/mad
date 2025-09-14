import androidx.lifecycle.*
import com.example.rasago.data.entity.OrderItemEntity
import com.example.rasago.data.model.Order
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collectLatest { list ->
                _orders.value = list
            }
        }
    }

    fun addOrder(order: Order, orderItems: List<OrderItemEntity>) {
        viewModelScope.launch {
            orderRepository.insertOrder(order, orderItems)
            loadOrders() // refresh LiveData
        }
    }
}
