package com.example.rasago.ui.theme.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.MenuItem
import com.example.rasago.data.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel @Inject constructor(private val menuRepository: MenuRepository): ViewModel(){
    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    fun loadMenu(){
        viewModelScope.launch {
            val items = menuRepository.getAllMenuItems()
            _menuItems.value = items
        }
    }

    suspend fun addMenuItem(item: MenuItem){
        menuRepository.insertMenuItem(item)
        loadMenu()
    }
}