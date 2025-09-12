package com.example.rasago.data.repository

import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.entity.MenuItem

//Repository = “bridge” between DAO (entities, database) and ViewModel (Ui, app logic)
class MenuRepository(private val menuDao: MenuItemDao){
    suspend fun insertMenuItem(item: MenuItem) =
        menuDao.insertMenuItem(item)

    suspend fun getAllMenuItems(): List<MenuItem> =
        menuDao.getAllMenuItems()
}