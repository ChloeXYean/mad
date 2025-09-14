//package com.example.rasago
//
//import com.example.rasago.data.model.CustomerProfile
//import com.example.rasago.data.model.MenuItem
//import com.example.rasago.data.model.Order
//import com.example.rasago.data.model.OrderItem
//import com.example.rasago.data.model.StaffProfile
//
//object DummyData {
//    /**
//     * Provides a list of `MenuItem` UI models for database pre-population.
//     * The repository will convert these into `MenuItemEntity` objects.
//     */
//    val menuItems: List<MenuItem> = listOf(
//        MenuItem(
//            id = 1,
//            name = "Nasi Lemak",
//            description = "A fragrant rice dish cooked in coconut milk and pandan leaf, served with a spicy sambal, fried anchovies, peanuts, and a hard-boiled egg.",
//            price = 8.5,
//            category = "Rice",
//            photo = R.drawable.rice_nasilemak,
//            isRecommended = true
//        ),
//        MenuItem(
//            id = 2,
//            name = "Asam Laksa",
//            description = "A sour and spicy fish-based noodle soup. Its unique flavour comes from tamarind (asam), chili, and shredded fish.",
//            price = 10.0,
//            category = "Noodles",
//            photo = R.drawable.noodle_asamlaksa,
//            isRecommended = true
//        ),
//        MenuItem(
//            id = 3,
//            name = "Curry Mee",
//            description = "A rich and flavorful noodle soup with a coconut-based curry broth, served with tofu puffs, chicken, and bean sprouts.",
//            price = 9.5,
//            category = "Noodles",
//            photo = R.drawable.noodle_currymee
//        ),
//        MenuItem(id = 4, name = "Teh Tarik", description = "A hot milk tea beverage.", price = 3.5, category = "Drinks", photo = R.drawable.drink_tehtarik),
//        MenuItem(id = 5, name = "Milo Ais", description = "A cold chocolate malt drink.", price = 4.0, category = "Drinks", photo = R.drawable.drink_miloais),
//        MenuItem(id = 6, name = "Chicken Rice", description = "Poached chicken and seasoned rice.", price = 9.0, category = "Rice", photo = R.drawable.rice_chickenrice),
//        MenuItem(id = 7, name = "Satay", description = "Seasoned, skewered and grilled meat, served with a delicious peanut sauce.", price = 12.0, category = "Side Dishes", photo = R.drawable.side_chickensatay),
//        MenuItem(id = 8, name = "Keropok Lekor", description = "A traditional Malay fish cracker snack.", price = 4.0, category = "Side Dishes", photo = R.drawable.side_lekor),
//        MenuItem(id = 9, name = "Cendol", description = "An iced sweet dessert with green rice flour jelly, coconut milk and palm sugar syrup.", price = 5.0, category = "Desserts", photo = R.drawable.dessert_cendol),
//        MenuItem(id = 10, name = "Ais Kacang (ABC)", description = "A Malaysian dessert of shaved ice, red beans, sweet corn, and grass jelly.", price = 5.0, category = "Desserts", photo = R.drawable.dessert_abc)
//    )
//
//    /**
//     * Provides a list of `Order` UI models for database pre-population.
//     * The repository will use mappers to convert these into `OrderEntity` and `OrderItemEntity` objects.
//     */
//    val orders: List<Order> = listOf(
//        Order(
//            id = 1,
//            no = "T01",
//            type = "Dine-In",
//            time = "2025-09-15 09:30",
//            status = "Preparing",
//            customerId = "1",
//            orderItems = listOf(
//                OrderItem(id = 1, name = "Nasi Lemak", price = 8.5, quantity = 2),
//                OrderItem(id = 4, name = "Teh Tarik", price = 3.5, quantity = 1)
//            )
//        ),
//        Order(
//            id = 2,
//            no = "T02",
//            type = "Takeaway",
//            time = "2025-09-15 10:15",
//            status = "Done",
//            customerId = "1",
//            orderItems = listOf(
//                OrderItem(id = 3, name = "Curry Mee", price = 9.5, quantity = 1),
//                OrderItem(id = 5, name = "Milo Ais", price = 4.0, quantity = 2)
//            )
//        ),
//        Order(
//            id = 3,
//            no = "T03",
//            type = "Dine-In",
//            time = "2025-09-15 11:05",
//            status = "Cancelled",
//            customerId = "1",
//            orderItems = listOf(
//                OrderItem(id = 7, name = "Satay", price = 12.0, quantity = 1),
//                OrderItem(id = 9, name = "Cendol", price = 5.0, quantity = 2)
//            )
//        )
//    )
//
//    val customerProfile = CustomerProfile(
//        id = 1,
//        name = "John Doe",
//        email = "john.doe@example.com",
//        phone = "0123456789",
//        gender = "Male",
//        profileImageRes = R.drawable.default_profile_picture,
//        password = "password"
//    )
//
//    val staffProfiles = listOf(
//        StaffProfile(
//            id = 1,
//            name = "Jane Smith (Manager)",
//            gender = "Female",
//            email = "jane.smith@rasago.com",
//            phone = "0198765432",
//            role = "MANAGER",
//            status = "Active",
//            jobTime = System.currentTimeMillis()
//        ),
//        StaffProfile(
//            id = 2,
//            name = "David Chen (Waiter)",
//            gender = "Male",
//            email = "david.chen@rasago.com",
//            phone = "0161234567",
//            role = "WAITER",
//            status = "Active",
//            jobTime = System.currentTimeMillis()
//        )
//    )
//}
//
