package com.example.rasago.theme.profile

enum class UserRole {
    CUSTOMER,
    MANAGER,
    KITCHEN,
    CASHIER,
    NONE
}

val UserRole.isStaff: Boolean
    get() = this != UserRole.CUSTOMER && this != UserRole.NONE