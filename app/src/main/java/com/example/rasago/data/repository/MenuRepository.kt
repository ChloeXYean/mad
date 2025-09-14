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
}

