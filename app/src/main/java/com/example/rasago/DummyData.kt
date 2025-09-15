package com.example.rasago

import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.theme.utils.RoleDetector

/**
 * Provides a pre-defined list of menu items to populate the database on its first creation.
 */
fun getPredefinedMenuItems(): List<MenuItemEntity> {
    return listOf(
        MenuItemEntity(
            name = "Nasi Lemak",
            description = "A traditional Malaysian fragrant rice dish cooked in coconut milk and pandan leaf.",
            price = 8.50,
            category = "Rice",
            photo = R.drawable.rice_nasilemak.toString(),
            isRecommended = true
        ),
        MenuItemEntity(
            name = "Asam Laksa",
            description = "A spicy and sour fish-based noodle soup.",
            price = 10.00,
            category = "Noodles",
            photo = R.drawable.noodle_asamlaksa.toString(),
            isRecommended = true
        ),
        // ... other menu items
    )
}

/**
 * Provides a pre-defined list of customers for database seeding.
 */
fun getPredefinedCustomers(): List<CustomerEntity> {
    return listOf(
        CustomerEntity(
            name = "John Doe",
            phone = "0123456789",
            email = "customer@rasago.com",
            password = "password123",
            gender = "Male"
        )
    )
}

/**
 * Provides a pre-defined list of staff members for database seeding, covering all roles.
 */
fun getPredefinedStaff(): List<StaffEntity> {
    return listOf(
        StaffEntity(
            name = "Manager Sarah",
            email = "mgr_sarah@rasago.com",
            password = "password123",
            phone = "0111111111",
            role = RoleDetector.ROLE_MANAGER,
            status = "active",
            jobTime = System.currentTimeMillis()
        ),
        StaffEntity(
            name = "Cashier Ali",
            email = "cas_ali@rasago.com",
            password = "password123",
            phone = "0122222222",
            role = RoleDetector.ROLE_CASHIER,
            status = "active",
            jobTime = System.currentTimeMillis()
        ),
        StaffEntity(
            name = "Kitchen David",
            email = "kit_david@rasago.com",
            password = "password123",
            phone = "0133333333",
            role = RoleDetector.ROLE_KITCHEN,
            status = "active",
            jobTime = System.currentTimeMillis()
        )
    )
}

