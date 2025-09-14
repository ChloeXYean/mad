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

fun List<MenuItem>.toEntityList(): List<MenuItemEntity> {
    return this.map { it.toEntity() }
}

// You'll also likely need the reverse for when you fetch data:
fun MenuItemEntity.toDomain(): MenuItem {
    return MenuItem(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        category = this.category,
        imageRes = this.imageRes
    )
}


fun List<MenuItemEntity>.toDomainList(): List<MenuItem> {
    return this.map { it.toDomain() }
}
