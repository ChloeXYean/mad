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

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.R
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.repository.MenuRepository
import com.example.rasago.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

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
    fun selectMenuItem(id: Int) {
        viewModelScope.launch {
            menuRepository.getMenuItemById(id).collect {
                _selectedMenuItem.value = it
            }
        }
    }

    fun addMenuItem(name: String, description: String, price: String, category: String, imageUri: Uri?) {
        viewModelScope.launch {
            val priceDouble = price.toDoubleOrNull() ?: 0.0

            // Copy the image to internal storage and get the permanent path
            val photoPath = imageUri?.let {
                imageRepository.copyImageToInternalStorage(it)
            } ?: "" // Use an empty string if no image was selected

            menuRepository.addMenuItem(name, description, priceDouble, category, photoPath, false)
        }
    }

    fun updateMenuItem(id: Int, name: String, description: String, price: String, category: String, imageUri: String) {
        viewModelScope.launch {
            val priceDouble = price.toDoubleOrNull() ?: 0.0

            // For updates, the imageUri is already a string. If it's a new temporary URI,
            // we would need to copy it. For simplicity, we assume the string is either
            // the old permanent path or a new permanent path.
            // A more complex implementation would differentiate between old and new URIs.
            val photoPath = Uri.parse(imageUri)?.let {
                if (it.scheme == "content") {
                    // This is a new image from the gallery, so we need to copy it
                    imageRepository.copyImageToInternalStorage(it) ?: ""
                } else {
                    // This is an existing file path, so we can use it directly
                    imageUri
                }
            } ?: ""

            val updatedMenuItem = MenuItem(
                id = id,
                name = name,
                description = description,
                price = priceDouble,
                category = category,
                photo = photoPath,
                isRecommended = _selectedMenuItem.value?.isRecommended ?: false
            )
            menuRepository.updateMenuItem(updatedMenuItem)
        }
    }

    fun deleteMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            menuRepository.deleteMenuItem(menuItem)
        }
    }
}
