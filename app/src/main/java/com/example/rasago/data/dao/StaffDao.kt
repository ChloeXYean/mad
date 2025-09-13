package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rasago.data.entity.StaffEntity

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff WHERE staffId = :id LIMIT 1")
    suspend fun getById(id: Int): StaffEntity?

    @Update
    suspend fun update(staff: StaffEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staff: StaffEntity): Long

    @Delete
    suspend fun delete(staff: StaffEntity)
}