package com.example.rasago.theme.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rasago.data.model.MenuItem
import com.example.rasago.theme.navigation.AppTopBar
import com.example.rasago.ui.theme.menu.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    menuViewModel: MenuViewModel,
    onBackClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    val menuItem by menuViewModel.selectedMenuItem.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = menuItem?.name ?: "Details",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        menuItem?.let { item ->
            FoodDetailContent(
                menuItem = item,
                modifier = Modifier.padding(innerPadding),
                onAddToCart = onAddToCart
            )
        }
    }
}

@Composable
fun FoodDetailContent(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            if (menuItem.photo.isNotBlank()) {
                AsyncImage(
                    model = menuItem.photo,
                    contentDescription = menuItem.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = "No Image Available",
                    modifier = Modifier.size(100.dp),
                    tint = Color.Gray
                )
            }
        }
        Text(
            text = menuItem.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "RM ${String.format("%.2f", menuItem.price)}",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = menuItem.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))

        // This button is now shown for everyone, including staff
        Button(
            onClick = onAddToCart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }
    }
}

