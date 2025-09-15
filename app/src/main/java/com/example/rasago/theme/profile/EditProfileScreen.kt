package com.example.rasago.theme.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rasago.R
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.theme.navigation.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    customer: CustomerEntity?,
    staff: StaffEntity?,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsState()

    // Load the profile
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(customer, staff)
    }

    // Navigate back when save is successful
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            profileViewModel.resetSaveStatus()
            onSaveSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full background image
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent, // Make Scaffold background transparent
            topBar = {
                AppTopBar(
                    title = "Edit Profile",
                    onBackClick = onBackClick
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp)) // Space to push below top bar

                // Profile Picture
                Image(
                    painter = painterResource(id = R.drawable.default_profile_picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Name Field
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { profileViewModel.updateName(it) },
                    label = { Text("Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name Icon") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Field
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { profileViewModel.updatePhone(it) },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field (read-only)
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { },
                    label = { Text("Email (cannot be changed)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = { profileViewModel.saveProfile() },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Save Changes")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


