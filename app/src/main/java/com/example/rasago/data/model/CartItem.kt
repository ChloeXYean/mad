package com.example.rasago.data.model

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int,
    val selectedAddOns: List<AddOn> = emptyList(),
    val totalPrice: Float = 0f
) {
    // 计算总价格（包括配菜）
    // 逻辑：(1个商品 + 配菜) x 数量
    fun calculateTotalPrice(): Float {
        val singleItemPrice = menuItem.price.toFloat()
        val addOnPrice = selectedAddOns.fold(0f) { sum, addOn -> sum + (addOn.price * addOn.quantity) }
        val totalPriceForOne = singleItemPrice + addOnPrice
        return totalPriceForOne * quantity
    }
}