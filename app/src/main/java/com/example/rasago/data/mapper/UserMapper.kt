package com.example.rasago.data.mapper

import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.model.CustomerProfile
import com.example.rasago.data.model.StaffProfile

fun CustomerProfile.toEntity(id: Int): CustomerEntity {
    return CustomerEntity(
        customerId = id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        gender = this.gender,
        isActive = true,
        createdAt = System.currentTimeMillis()
    )
}

fun StaffProfile.toEntity(): StaffEntity {
    return StaffEntity(
        staffId = this.id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        role = this.role,
        status = this.status,
        jobTime = this.jobTime
        )
}
