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
    @Query("SELECT * FROM staff WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): StaffEntity?

    @Query("SELECT * FROM staff WHERE staffId = :id LIMIT 1")
    suspend fun getById(id: Int): StaffEntity

    @Update
    suspend fun update(staff: StaffEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staff: StaffEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(staff: List<StaffEntity>)

    @Delete
    suspend fun delete(staff: StaffEntity)

    @Query("SELECT COUNT(*) FROM staff")
    suspend fun getCount(): Int

    // 登录相关方法
    @Query("SELECT * FROM staff WHERE email = :email AND password = :password AND status = 'active' LIMIT 1")
    suspend fun login(email: String, password: String): StaffEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM staff WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Query("SELECT * FROM staff WHERE status = 'active'")
    suspend fun getAllActive(): List<StaffEntity>

    @Query("SELECT * FROM staff")
    suspend fun getAll(): List<StaffEntity>

    @Query("UPDATE staff SET status = :status WHERE email = :email")
    suspend fun updateStatus(email: String, status: String)
}