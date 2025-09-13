package com.example.rasago.ui.theme.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class MenuUiState(
    val menuItems: List<MenuItem> = emptyList(),
    val cart: Map<MenuItem, Int> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val orderPlaced: Boolean = false,
    val placedOrderId: Long? = null
)

@HiltViewModel
class MenuViewModel @Inject constructor(private val menuRepository: MenuRepository): ViewModel(){
    private val _menuItems = MutableLiveData<List<MenuItemEntity>>()
    val menuItems: LiveData<List<MenuItemEntity>> = _menuItems

    init{
        loadMenu()
    }

    fun loadMenu(){
        viewModelScope.launch {
            val items = menuRepository.getAllMenuItems()
            _menuItems.value = items
        }
    }

    suspend fun addMenuItem(item: MenuItemEntity){
        menuRepository.insertMenuItem(item)
        loadMenu()
    }
}