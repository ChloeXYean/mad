package com.example.rasago.theme.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        menuItem?.let {
            FoodDetailContent(
                menuItem = it,
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
        Image(
            painter = painterResource(id = menuItem.photo),
            contentDescription = menuItem.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = menuItem.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "RM ${String.format("%.2f", menuItem.price)}",
            style = MaterialTheme.typography.titleLarge
        )
        // Display the description
        Text(
            text = menuItem.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onAddToCart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }
    }
}

