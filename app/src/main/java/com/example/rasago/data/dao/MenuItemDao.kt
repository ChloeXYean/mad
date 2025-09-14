//package com.example.rasago.data.dao
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.rasago.data.entity.MenuItemEntity
//import com.example.rasago.data.model.MenuItem
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface   MenuItemDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMenuItem(item: MenuItemEntity): Long
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(menuItems: List<MenuItemEntity>)
//    @Update
//    suspend fun updateMenuItem(item: MenuItemEntity)
//
//    @Delete
//    suspend fun deleteMenuItem(item: MenuItemEntity)
//
//    @Query("SELECT * FROM menu_items")
//    fun getMenuItems(): Flow<List<MenuItemEntity>>
//
//    @Query("SELECT COUNT(*) FROM menu_items")
//    suspend fun getCount(): Int
//
//    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC") //ASC = Ascending, DES = Descending
//    suspend fun getAllMenuItems(): List<MenuItem> //Get data once
//
//    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC") //ASC = Ascending, DES = Descending
//    fun getAllMenuItemsFlow(): Flow<List<MenuItem>> //Keep getting updated data
//
//    @Query("SELECT * FROM menu_items WHERE id = :id")
//    suspend fun getMenuItemById(id: Int): Flow<MenuItemEntity?>
//
//}

package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rasago.data.entity.MenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    fun getMenuItemById(id: Long): Flow<MenuItemEntity?>

    @Query("SELECT * FROM menu_items WHERE id IN (:ids)")
    fun getMenuItemsByIds(ids: List<Long?>): Flow<List<MenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(menuItems: List<MenuItemEntity>)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun getMenuItemCount(): Int
}

