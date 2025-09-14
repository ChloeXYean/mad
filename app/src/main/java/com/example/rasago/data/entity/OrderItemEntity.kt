package com.example.rasago.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single item within an order.
 *
 * This table links an Order with a MenuItem and stores the quantity and price at the time of purchase.
 *
 * @param id The unique identifier for this specific order item entry.
 * @param orderId The ID of the order this item belongs to. This is a foreign key to the 'orders' table.
 * @param menuItemId The ID of the menu item that was ordered. This is a foreign key to the 'menu_items' table.
 * @param quantity The number of this item that was ordered.
 * @param price The price of a single unit of this item at the time the order was placed.
 */
@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"], // The PrimaryKey column in the OrderEntity
            childColumns = ["orderId"], // The column in this entity that references the parent
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuItemEntity::class,
            parentColumns = ["id"], // The PrimaryKey column in the MenuItemEntity
            childColumns = ["menuItemId"],// The column in this entity that references the menu item
            onDelete = ForeignKey.SET_NULL // If a menu item is deleted, don't delete the order history
        )
    ],
    // Add indices for foreign key columns to improve query performance
    indices = [
        Index(value = ["orderId"]),
        Index(value = ["menuItemId"]) // This index resolves the KSP warning
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: Long,
    val menuItemId: Long, // Link back to the original menu item
    val quantity: Int,
    val price: Double // Price at the time of order
)
