package com.example.rasago.data.mapper

import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.model.CustomerProfile
import com.example.rasago.data.model.StaffProfile

fun CustomerProfile.toEntity(id: Long): CustomerEntity {
    return CustomerEntity(
        customerId = id,
        name = this.name,
        email = this.email,
        phone = this.phone,
        gender = this.gender,
        profileImageRes = this.profileImageRes,
        password = this.password
    )
}

fun StaffProfile.toEntity(): StaffEntity {
    return StaffEntity(
        staffId = this.id,
        name = this.name,
        gender = this.gender,
        email = this.email,
        phone = this.phone,
        role = this.role,
        status = this.status,
        jobTime = this.jobTime
        )
}
