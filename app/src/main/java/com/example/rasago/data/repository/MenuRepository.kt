package com.example.rasago.data.repository

import com.example.rasago.DummyData
import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

//Repository = “bridge” between DAO (entities, database) and ViewModel (Ui, app logic)
class MenuRepository(private val menuItemDao: MenuItemDao){
    suspend fun insertMenuItem(item: MenuItemEntity) =
        menuItemDao.insertMenuItem(item)

    suspend fun getAllMenuItems(): List<MenuItem> {
        return menuItemDao.getAllMenuItems()
    }

    suspend fun prepopulateMenu(){
        if (menuItemDao.getCount() == 0){
            menuItemDao.insertAll(DummyData.menuItems)
        }
    }
}