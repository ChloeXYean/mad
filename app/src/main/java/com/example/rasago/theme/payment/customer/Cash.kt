package com.example.rasago.theme.payment.customer

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.order.OrderViewModel
import com.example.rasago.theme.navigation.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashPaymentScreen(
    orderViewModel: OrderViewModel,
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    val orderState by orderViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Cash Payment", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Order Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Order No :", style = MaterialTheme.typography.bodyLarge)
                Text(text = "T${System.currentTimeMillis() % 1000}", style = MaterialTheme.typography.bodyLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total Payment :", style = MaterialTheme.typography.bodyLarge)
                Text(text = "RM ${String.format("%.2f", orderState.total)}", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Pay at counter Area
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .border(2.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                    .clickable { onPaymentSuccess() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pay at counter",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Click the box above to proceed",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
