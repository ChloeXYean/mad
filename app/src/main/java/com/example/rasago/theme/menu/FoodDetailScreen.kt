package com.example.rasago.theme.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rasago.R
import com.example.rasago.data.model.AddOn
import com.example.rasago.data.model.CartItem
import com.example.rasago.order.OrderViewModel
import com.example.rasago.ui.theme.menu.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    menuViewModel: MenuViewModel,
    orderViewModel: OrderViewModel,
    onBackClick: () -> Unit,
    editMode: Boolean,
    existingCartItem: CartItem?,
    cartItemIndex: Int
) {
    val menuItem by menuViewModel.selectedMenuItem.collectAsState()

    // This check ensures that we don't proceed if the menu item data isn't loaded yet.
    menuItem?.let { currentMenuItem ->
        // State for quantity and add-ons, initialized based on whether we are in "edit" mode.
        var quantity by remember {
            mutableStateOf(if (editMode && existingCartItem != null) existingCartItem.quantity else 1)
        }
        val availableAddOns = remember {
            listOf(
                AddOn("Sambal", 1.5f, R.drawable.side_otakotak),
                AddOn("Fried Egg", 2.0f, R.drawable.addon_egg),
                AddOn("Extra Rice", 3.0f, R.drawable.addon_rice),
                AddOn("Fried Chicken", 4.5f, R.drawable.addon_friedchicken)
            )
        }
        var currentAddOns by remember {
            mutableStateOf(
                if (editMode && existingCartItem != null) {
                    // In edit mode, we merge the existing add-ons with the full available list
                    // to ensure all options are displayed.
                    availableAddOns.map { available ->
                        existingCartItem.selectedAddOns.find { it.name == available.name } ?: available
                    }
                } else {
                    availableAddOns.map { it.copy() }
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (editMode) "Edit Item" else "Food Details") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                // Food Image
                AsyncImage(
                    model = currentMenuItem.photo,
                    contentDescription = currentMenuItem.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Food Name, Description, and Price
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(currentMenuItem.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(currentMenuItem.description, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                    }
                    Text("RM ${"%.2f".format(currentMenuItem.price)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Quantity Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Quantity:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-", fontSize = 18.sp)
                        }
                        Text(quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = { quantity++ },
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+", fontSize = 18.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Add-Ons Section
                Text("Add Ons:", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(currentAddOns.size) { index ->
                        val addOn = currentAddOns[index]
                        AddOnItemRow(
                            addOn = addOn,
                            onQuantityChange = { newQuantity ->
                                val updatedList = currentAddOns.toMutableList()
                                updatedList[index] = addOn.copy(quantity = newQuantity)
                                currentAddOns = updatedList
                            }
                        )
                    }
                }

                // Total Price Calculation
                val addOnPrice = currentAddOns.sumOf { it.price.toDouble() * it.quantity }
                val totalPrice = (currentMenuItem.price + addOnPrice) * quantity

                // Add to Cart / Update Button
                Button(
                    onClick = {
                        val selectedAddOns = currentAddOns.filter { it.quantity > 0 }
                        if (editMode) {
                            orderViewModel.editItem(cartItemIndex, selectedAddOns, quantity)
                        } else {
                            orderViewModel.addItem(currentMenuItem, selectedAddOns, quantity)
                        }
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (editMode) "Update Item - RM ${"%.2f".format(totalPrice)}" else "Add to Cart - RM ${"%.2f".format(totalPrice)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AddOnItemRow(
    addOn: AddOn,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = addOn.imageRes), contentDescription = addOn.name, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(addOn.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("RM ${"%.2f".format(addOn.price)}", fontSize = 14.sp, color = Color.Gray)
            }
            // Add-On Quantity Control
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { if (addOn.quantity > 0) onQuantityChange(addOn.quantity - 1) },
                    modifier = Modifier.size(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-", fontSize = 14.sp)
                }
                Text(addOn.quantity.toString(), modifier = Modifier.padding(horizontal = 12.dp), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Button(
                    onClick = { onQuantityChange(addOn.quantity + 1) },
                    modifier = Modifier.size(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", fontSize = 14.sp)
                }
            }
        }
    }
}

