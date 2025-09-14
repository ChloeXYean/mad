//package com.example.rasago.data.repository
//
//import com.example.rasago.DummyData
//import com.example.rasago.data.dao.MenuItemDao
//import com.example.rasago.data.entity.MenuItemEntity
//import com.example.rasago.data.mapper.toEntityList
//import com.example.rasago.data.model.MenuItem
//import kotlinx.coroutines.flow.Flow
//
////Repository = “bridge” between DAO (entities, database) and ViewModel (Ui, app logic)
//class MenuRepository(private val menuItemDao: MenuItemDao){
//    suspend fun insertMenuItem(item: MenuItemEntity) =
//        menuItemDao.insertMenuItem(item)
//
//    suspend fun getAllMenuItems(): List<MenuItem> {
//        return menuItemDao.getAllMenuItems()
//    }
//
//
//    suspend fun prepopulateMenu(){
//        if (menuItemDao.getCount() == 0){
//            val menuItemEntities = DummyData.menuItems.toEntityList()
//            menuItemDao.insertAll(menuItemEntities)
//        }
//    }
//}

package com.example.rasago.data.repository

import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.mapper.toMenuItem
import com.example.rasago.data.model.MenuItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuRepository @Inject constructor(private val menuItemDao: MenuItemDao) {
    fun getMenuItems(): Flow<List<MenuItem>> {
        return menuItemDao.getAllMenuItems().map { entities ->
            entities.map { it.toMenuItem() }
        }
    }

    fun getMenuItemById(id: Long): Flow<MenuItem?> {
        return menuItemDao.getMenuItemById(id).map { it?.toMenuItem() }
    }

    /**
     * Inserts a new menu item into the database.
     */
    suspend fun addMenuItem(
        name: String,
        description: String,
        price: Double,
        category: String,
        photoUri: String,
        isRecommended: Boolean
    ) {
        val newMenuItem = MenuItemEntity(
            name = name,
            description = description,
            price = price,
            category = category,
            photo = photoUri,
            isRecommended = isRecommended
        )
        menuItemDao.insertMenuItem(newMenuItem)
    }

    suspend fun updateMenuItem(menuItem: MenuItem) {
        val menuItemEntity = MenuItemEntity(
            id = menuItem.id,
            name = menuItem.name,
            description = menuItem.description,
            price = menuItem.price,
            category = menuItem.category,
            photo = menuItem.photo,
            isRecommended = menuItem.isRecommended
        )
        menuItemDao.updateMenuItem(menuItemEntity)
    }

    suspend fun deleteMenuItem(menuItem: MenuItem) {
        val menuItemEntity = MenuItemEntity(id = menuItem.id, name = menuItem.name, description = menuItem.description, price = menuItem.price, category = menuItem.category, photo = menuItem.photo, isRecommended = menuItem.isRecommended)
        menuItemDao.deleteMenuItem(menuItemEntity)
    }
}

