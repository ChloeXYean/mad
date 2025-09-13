package com.example.rasago.data.mapper

import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.model.MenuItem

fun MenuItem.toEntity(): MenuItemEntity {
    return MenuItemEntity(
        id = this.id,
        name = this.name,
        price = this.price,
        category = this.category,
        description = this.description,
        imageRes = this.imageRes
    )
}
