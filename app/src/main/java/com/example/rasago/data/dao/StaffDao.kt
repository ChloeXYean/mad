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
    suspend fun getById(id: Long): StaffEntity

    @Update
    suspend fun update(staff: StaffEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staff: StaffEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(staff: List<StaffEntity>)

    @Delete
    suspend fun delete(staff: StaffEntity)

    @Query("SELECT COUNT(*) FROM staff")
    suspend fun getCount(): Int
}