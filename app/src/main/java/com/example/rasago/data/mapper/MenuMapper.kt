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
        photo = this.photo,
        isRecommended = this.isRecommended
    )
}

fun List<MenuItem>.toEntityList(): List<MenuItemEntity> {
    return this.map { it.toEntity() }
}

/**
 * Converts a MenuItemEntity from the database into a MenuItem model for the app's UI.
 */
fun MenuItemEntity.toMenuItem(): MenuItem {
    return MenuItem(
        id = this.id,
        name = this.name,
        description = this.description, // Map the description
        price = this.price,
        category = this.category,
        photo = this.photo,
        isRecommended = this.isRecommended
    )
}


fun List<MenuItemEntity>.toDomainList(): List<MenuItem> {
    return this.map { it.toMenuItem() }
}
