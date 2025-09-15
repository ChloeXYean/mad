package com.example.rasago.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rasago.data.entity.CustomerEntity

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers WHERE customerId = :id LIMIT 1")
    suspend fun getById(id: Int): CustomerEntity?

    @Update
    suspend fun update(customer: CustomerEntity)

    @Query("SELECT * FROM customers")
    suspend fun getAll(): List<CustomerEntity>

    @Query("SELECT * FROM customers WHERE phone = :phone LIMIT 1")
    suspend fun getByPhone(phone: String): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity): Long

    @Query("SELECT COUNT(*) FROM customers")
    suspend fun getCount(): Int

    @Query("SELECT * FROM customers WHERE email = :email AND password = :password AND isActive = 1 LIMIT 1")
    suspend fun login(email: String, password: String): CustomerEntity?

    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): CustomerEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM customers WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM customers WHERE name = :name)")
    suspend fun isNameExists(name: String): Boolean

    @Query("SELECT * FROM customers WHERE isActive = 1")
    suspend fun getAllActive(): List<CustomerEntity>
}