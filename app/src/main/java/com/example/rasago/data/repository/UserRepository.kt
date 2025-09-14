package com.example.rasago.data.repository

import com.example.rasago.DummyData
import com.example.rasago.data.dao.CustomerDao
import com.example.rasago.data.dao.StaffDao
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.mapper.toEntity
import com.example.rasago.data.model.CustomerProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val customerDao: CustomerDao,
    private val staffDao: StaffDao
) {
    // A simple way to get the first customer profile for the UI
    suspend fun getCustomerProfile(): CustomerProfile? {
        // In a real app, you'd have a way to select the current user
        return DummyData.customerProfile
    }

    suspend fun updateCustomerProfile(profile: CustomerProfile) {
        // In a real app, you'd update the specific user entity in the DB
        println("Updated profile: $profile")
    }


    suspend fun getCustomerById(id: Long): CustomerEntity? = customerDao.getById(id)
    suspend fun updateCustomer(customer: CustomerEntity) = customerDao.update(customer)

    suspend fun getStaffById(id: Long): StaffEntity? = staffDao.getById(id)
    suspend fun updateStaff(staff: StaffEntity) = staffDao.update(staff)

    suspend fun prepopulateUsers() {
        if (customerDao.getCount() == 0) {
            customerDao.insert(DummyData.customerProfile.toEntity(2)) //TODO: TEMP
        }
        if (staffDao.getCount() == 0) {
            staffDao.insertAll(DummyData.staffProfiles.map { it.toEntity() })
        }
    }
}
