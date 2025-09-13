package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rasago.data.entity.MenuItem

@Dao
interface MenuItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: MenuItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: List<MenuItem>): List<Long>

    @Update
    suspend fun updateMenuItem(item: MenuItem)

    @Delete
    suspend fun deleteMenuItem(item: MenuItem)

    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC") //ASC = Ascending, DES = Descending
    suspend fun getAllMenuItems(): List<MenuItem>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    suspend fun getMenuItemById(id: Int): MenuItem

}