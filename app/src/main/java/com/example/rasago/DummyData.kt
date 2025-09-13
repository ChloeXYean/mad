package com.example.rasago

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
                OrderItem("Nasi Lemak", 8.5, 2),
                OrderItem("Teh Tarik", 2.0, 1)
            )
        ),
        Order(
            no = "T02",
            type = "Takeaway",
            time = "2025-09-13 13:10",
            status = "Done",
            orderItems = listOf(
                OrderItem("Mee Goreng", 7.0, 1),
                OrderItem("Sirap Bandung", 2.5, 2)
            )
        )
    )
}

