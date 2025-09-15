package com.example.rasago.data.mapper

import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.model.MenuItem

fun MenuItemEntity.toMenuItem(): MenuItem {
    return MenuItem(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        category = this.category,
        photo = this.photo,
        isRecommended = this.isRecommended
    )
}

