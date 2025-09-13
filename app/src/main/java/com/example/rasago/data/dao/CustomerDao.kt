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

    @Query("SELECT * FROM customers WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getByPhone(phone: String): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity): Long


}