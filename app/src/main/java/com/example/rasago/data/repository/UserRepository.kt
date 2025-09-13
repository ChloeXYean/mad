package com.example.rasago.data.repository

import com.example.rasago.data.dao.CustomerDao
import com.example.rasago.data.dao.StaffDao
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import jakarta.inject.Inject


class UserRepository @Inject constructor(
    private val customerDao: CustomerDao,
    private val staffDao: StaffDao
) {
    suspend fun getCustomerById(id: Int): CustomerEntity? = customerDao.getById(id)
    suspend fun updateCustomer(customer: CustomerEntity) = customerDao.update(customer)

    suspend fun getStaffById(id: Int): StaffEntity? = staffDao.getById(id)
    suspend fun updateStaff(staff: StaffEntity) = staffDao.update(staff)
}