package com.example.rasago.theme.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.theme.navigation.CustomerBottomNavigationBar
import com.example.rasago.theme.navigation.StaffBottomNavigationBar
import com.example.rasago.theme.menu.NavItem

// 订单数据类
data class Order(val orderNumber: String, val time: String, val type: String)

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // 浅绿背景
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Order Number: ${order.orderNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Time: ${order.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(20.dp)
                    .background(
                        color = when (order.type) {
                            "Dine In" -> Color(0xFF81C784) // 深绿
                            "Take Away" -> Color(0xFFFFD54F) // 浅橙
                            else -> Color.Gray
                        },
                        shape = RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center // 使 Box 内的 Text 水平垂直居中
            ) {
                Text(
                    text = order.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
            TextButton(
                onClick = { /*TODO: Handle view receipt*/ },
                modifier = Modifier.padding(top = 4.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
            ) {
                Text("View receipt", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun OrderList(orders: List<Order>) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(orders) { order ->
            OrderItem(order = order)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    isStaff: Boolean = true,
    cartItemCount: Int = 0,
    selectedNavItem: String = "Orders",
    onNavItemSelect: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO: Handle back click*/ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            // 根据用户角色显示不同的底部导航栏
            if (isStaff) {
                StaffBottomNavigationBar(
                    selectedNavItem = selectedNavItem,
                    onNavItemSelect = onNavItemSelect
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {

        // 搜索栏
            OutlinedTextField(

                value = "",
                onValueChange = {},
                label = {
                    Text(
                        "Search by Order ID",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                )
            )


// 筛选栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                ) {
                    OutlinedTextField(
                        value = "Today",
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(
                                "Date",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                        },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(15.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                }
                Spacer(modifier = Modifier.padding(15.dp))
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = "All",
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(
                                "Status",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                        },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                }
            }


            // 订单列表
            val orders = listOf(
                Order("#T01", "9:03 AM", "Dine In"),
                Order("#T02", "9:15 AM", "Take Away"),
                Order("#T03", "9:20 AM", "Dine In"),
                Order("#T04", "9:30 AM", "Take Away")
            )
            OrderList(orders = orders)
        }
    }
}


@Preview(showBackground = true, name = "Staff Order History")
@Composable
fun StaffOrderHistoryPreview() {
    MaterialTheme {
        OrderHistoryScreen(
            isStaff = true,
            selectedNavItem = "Staff"
        )
    }
}
