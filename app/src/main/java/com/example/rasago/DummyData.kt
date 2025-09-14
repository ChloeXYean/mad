package com.example.rasago

import com.example.rasago.data.model.CustomerProfile
import com.example.rasago.data.model.MenuItem
import com.example.rasago.data.model.Order
import com.example.rasago.data.model.OrderItem

object DummyData {
    val orders = listOf(
        Order(
            no = "T01",
            type = "Dine-In",
            time = "2025-09-13 12:30",
            status = "Preparing",
            orderItems = listOf(
                OrderItem(1,"Nasi Lemak", 8.5, 2),
                OrderItem(2,"Teh Tarik", 2.0, 1)
            )
        ),
        Order(
            no = "T02",
            type = "Takeaway",
            time = "2025-09-13 13:10",
            status = "Done",
            orderItems = listOf(
                OrderItem(1,"Mee Goreng", 7.0, 1),
                OrderItem(2,"Sirap Bandung", 2.5, 2)
            )
        )
    )

    val customerProfile = CustomerProfile(
        name = "John Doe",
        email = "john.c.calhoun@examplepetstore.com",
        phone = "123-456-7890",
        profileImageRes = R.drawable.default_profile_picture
    )

    val menuItems = listOf(
        MenuItem(
            id = 1,
            name = "Nasi Lemak",
            description = " ",
            price = 8.5,
            category = "Main Course",
            imageRes = R.drawable.rice_nasilemak
        ),
        MenuItem(
            id = 2,
            name = "Teh Tarik",
            description = " ",
            price = 2.0,
            category = "Drinks",
            imageRes = R.drawable.drink_tehtarik
        )
    )
}

