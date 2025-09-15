package com.example.rasago

import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.entity.StaffEntity

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
        MenuItemEntity(
            name = "Curry Mee",
            description = "A rich and spicy curry noodle soup with coconut milk.",
            price = 9.50,
            category = "Noodles",
            photo = R.drawable.noodle_currymee.toString()
        ),
        MenuItemEntity(
            name = "Otak-Otak",
            description = "Grilled fish cake made of ground fish meat mixed with tapioca starch and spices.",
            price = 7.00,
            category = "Side Dishes",
            photo = R.drawable.side_otakotak.toString()
        ),
        MenuItemEntity(
            name = "Chicken Rice",
            description = "Poached chicken and seasoned rice, served with chili sauce and cucumber garnishes.",
            price = 9.00,
            category = "Rice",
            photo = R.drawable.rice_chickenrice.toString()
        ),
        MenuItemEntity(
            name = "Char Kuey Teow",
            description = "Stir-fried rice noodles with shrimp, cockles, bean sprouts, and chives in a soy sauce mixture.",
            price = 11.00,
            category = "Noodles",
            photo = R.drawable.noodle_charkueyteow.toString()
        ),
        MenuItemEntity(
            name = "Chicken Satay",
            description = "Grilled marinated chicken skewers served with a peanut sauce.",
            price = 12.00,
            category = "Side Dishes",
            photo = R.drawable.side_chickensatay.toString()
        ),
        MenuItemEntity(
            name = "Cendol",
            description = "An iced sweet dessert that contains droplets of green rice flour jelly, coconut milk and palm sugar syrup.",
            price = 5.00,
            category = "Desserts",
            photo = R.drawable.dessert_cendol.toString()
        ),
        MenuItemEntity(
            name = "Teh Tarik",
            description = "A popular hot milk tea beverage most commonly found in restaurants, outdoor stalls and kopitiams.",
            price = 3.50,
            category = "Drinks",
            photo = R.drawable.drink_tehtarik.toString()
        )
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
 * Provides a pre-defined list of staff members for database seeding.
 */
fun getPredefinedStaff(): List<StaffEntity> {
    return listOf(
        StaffEntity(
            name = "Cashier Ali",
            email = "cas_ali@rasago.com",
            password = "password123",
            phone = "0198765432",
            role = "cashier",
            status = "active",
            jobTime = System.currentTimeMillis()
        ),
        StaffEntity(
            name = "Kitchen David",
            email = "kit_david@rasago.com",
            password = "password123",
            phone = "0123456789",
            role = "kitchen",
            status = "active",
            jobTime = System.currentTimeMillis()
        ),
        StaffEntity(
            name = "Manager Sarah",
            email = "mgr_sarah@rasago.com",
            password = "password123",
            phone = "0111222333",
            role = "manager",
            status = "active",
            jobTime = System.currentTimeMillis()
        )
    )
}

