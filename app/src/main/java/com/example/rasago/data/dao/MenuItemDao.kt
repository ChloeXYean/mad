package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rasago.data.entity.MenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface   MenuItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: MenuItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MenuItemEntity>): List<Long>

    @Update
    suspend fun updateMenuItem(item: MenuItemEntity)

    @Delete
    suspend fun deleteMenuItem(item: MenuItemEntity)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun getCount(): Int

    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC") //ASC = Ascending, DES = Descending
    suspend fun getAllMenuItems(): List<MenuItemEntity> //Get data once

    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC") //ASC = Ascending, DES = Descending
    fun getAllMenuItemsFlow(): Flow<List<MenuItemEntity>> //Keep getting updated data

    @Query("SELECT * FROM menu_items WHERE id = :id")
    suspend fun getMenuItemById(id: Int): MenuItemEntity

}