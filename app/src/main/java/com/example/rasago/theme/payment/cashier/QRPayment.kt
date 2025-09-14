package com.example.rasago.theme.payment.cashier

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.R

@Composable
fun QRPaymentScreen() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { /* TODO: Implement back navigation */ }
                )
                Text(
                    text = "QR Payment",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Order Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Order No :", style = MaterialTheme.typography.bodyLarge)
                Text(text = "T01", style = MaterialTheme.typography.bodyLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total Payment :", style = MaterialTheme.typography.bodyLarge)
                Text(text = "RM 20.95", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // QR Code (Placeholder)
            Image(
                painter = painterResource(R.drawable.ic_menu_help), // Replace with your QR code image
                contentDescription = "QR Code",
                modifier = Modifier.size(200.dp).clickable {
                    /*TODO: Implement click action*/
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Click anywhere to proceed",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRPaymentPreview() {
    MaterialTheme {
        QRPaymentScreen()
    }
}
