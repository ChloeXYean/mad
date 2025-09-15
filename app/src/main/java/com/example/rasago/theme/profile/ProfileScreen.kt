package com.example.rasago.theme.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rasago.R
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.ui.theme.Baloo2
import com.example.rasago.ui.theme.DarkGreen
import com.example.rasago.ui.theme.LightGreen

enum class AccountStatus(val label: String, val color: Color) {
    Active("Active", Color(0xFF4CAF50)),
    Inactive("Inactive", Color(0xFF9E9E9E)),
}

@Composable
fun ProfileScreen(
    customer: CustomerEntity?,
    staff: StaffEntity?,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onManageMenuClicked: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onLogout: () -> Unit
) {
    val isStaff = staff != null
    val name = staff?.name ?: customer?.name ?: "User"
    val email = staff?.email ?: customer?.email ?: "No email"
    val phone = staff?.phone ?: customer?.phone ?: "N/A"
    val gender = customer?.gender
    val status = if (isStaff) {
        if (staff?.status.equals("active", ignoreCase = true)) AccountStatus.Active else AccountStatus.Inactive
    } else {
        if (customer?.isActive == true) AccountStatus.Active else AccountStatus.Inactive
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Background image with fixed height
            Image(
                painter = painterResource(R.drawable.background_image),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Profile Picture
            Image(
                painter = painterResource(id = R.drawable.default_profile_picture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp) // slightly bigger
                    .clip(CircleShape)
                    .align(Alignment.BottomCenter) // aligns at the bottom of the Box
                    .border(3.dp, Color.White, CircleShape), // optional border
                contentScale = ContentScale.Crop
            )

            // Top Bar (Back & Edit)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Text(
                    text = if (isStaff) "Staff Profile" else "My Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                TextButton(onClick = onEditProfileClick) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.Black, modifier = Modifier.size(16.dp))
                        Text("Edit", color = Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        // User Name and Email
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(16.dp))

        // Personal Information Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(LightGreen)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.personal_information),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    fontFamily = Baloo2
                )
            }
        }
        ProfileItem(label = R.string.name, value = name)
        ProfileStatus(label = R.string.status, status = status)
        ProfileItem(label = R.string.mobile_number, value = phone)
        ProfileItem(label = R.string.email, value = email)
        if (gender != null && !isStaff) {
            ProfileItem(label = R.string.gender, value = gender)
        }

        // Action buttons based on role
        if (isStaff) {
            ProfileActionButton(
                text = "Manage Menu",
                icon = Icons.Default.MenuBook,
                onClick = onManageMenuClicked
            )
            ProfileActionButton(
                text = "Orders History",
                icon = Icons.Default.ReceiptLong,
                onClick = onNavigateToOrders
            )
        }

        // Log Out Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onLogout),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Log Out", tint = MaterialTheme.colorScheme.onErrorContainer)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", color = MaterialTheme.colorScheme.onErrorContainer, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileItem(@StringRes label: Int, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(label),
            color = DarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            fontFamily = Baloo2
        )
        Text(
            text = value, fontWeight = FontWeight.Medium, fontSize = 18.sp,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            fontFamily = Baloo2
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun ProfileStatus(@StringRes label: Int, status: AccountStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            color = DarkGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Baloo2
        )
        AccountStatusBadge(status)
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@Composable
fun AccountStatusBadge(status: AccountStatus) {
    Box(
        modifier = Modifier
            .background(color = status.color, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = status.label,
            color = Color.White
        )
    }
}

@Composable
private fun ProfileActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = icon, contentDescription = text)
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }
    }
}

