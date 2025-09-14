//package com.example.rasago.ui.theme.menu
//
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.rasago.data.entity.MenuItemEntity
//import com.example.rasago.data.model.MenuItem
//import com.example.rasago.data.repository.MenuRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MenuViewModel @Inject constructor(private val menuRepository: MenuRepository) : ViewModel() {
//
//    private val _menuItems = mutableStateListOf<MenuItem>()
//
//    val menuItems: List<MenuItem> get() = _menuItems
//
//    init {
//        loadMenuItems()
//    }
//
//    fun loadMenuItems() {
//        viewModelScope.launch {
//            val list = menuRepository.getAllMenuItems()
//            _menuItems.clear()
//            _menuItems.addAll(list)
//        }
//    }
//
//    fun addMenuItem(item: MenuItemEntity) {
//        viewModelScope.launch {
//            menuRepository.insertMenuItem(item)
//            loadMenuItems() // refresh list
//        }
//    }
//
//    suspend fun prepopulateMenu() {
//        menuRepository.prepopulateMenu()
//        loadMenuItems()
//    }
//}
package com.example.rasago.ui.theme.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val menuRepository: MenuRepository) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()

    private val _selectedMenuItem = MutableStateFlow<MenuItem?>(null)
    val selectedMenuItem: StateFlow<MenuItem?> = _selectedMenuItem.asStateFlow()

    init {
        viewModelScope.launch {
            menuRepository.getMenuItems().collect {
                _menuItems.value = it
            }
        }
    }


    /**
     * Fetches a menu item by its ID from the repository and sets it as the
     * currently selected item in the UI state.
     */
    fun selectMenuItem(id: Long) {
        viewModelScope.launch {
            menuRepository.getMenuItemById(id).collect {
                _selectedMenuItem.value = it
            }
        }
    }
}
