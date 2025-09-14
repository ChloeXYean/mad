package com.example.rasago.theme.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    isStaff: Boolean,
    onBackClick: () -> Unit,
    onAddToCart: () -> Unit,
    onEditClick: (MenuItem) -> Unit,
    onDeleteClick: (MenuItem) -> Unit
) {
    val menuItem by menuViewModel.selectedMenuItem.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                isStaff = isStaff,
                onAddToCart = onAddToCart,
                onEditClick = { onEditClick(item) },
                onDeleteRequest = { showDeleteDialog = true } // Show the dialog
            )
        }

        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    menuItem?.let { onDeleteClick(it) }
                    showDeleteDialog = false
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

@Composable
fun FoodDetailContent(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    isStaff: Boolean,
    onAddToCart: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = menuItem.photo,
            contentDescription = menuItem.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop,
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
        Text(
            text = menuItem.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))

        if (isStaff) {
            // Show Edit and Delete buttons for staff
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = onEditClick, modifier = Modifier.weight(1f)) {
                    Text("Edit")
                }
                Button(
                    onClick = onDeleteRequest,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            }
        } else {
            // Show Add to Cart button for customers
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Deletion") },
        text = { Text("Are you sure you want to delete this menu item? This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

