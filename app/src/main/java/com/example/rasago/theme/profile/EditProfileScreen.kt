package com.example.rasago.theme.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rasago.theme.navigation.AppTopBar
import com.example.rasago.DummyData.customerProfile
import com.example.rasago.ProfileViewModel
import com.example.rasago.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    // Collect the customer profile state from the ViewModel.
    val uiState by profileViewModel.uiState.collectAsState()

    //Temp
    var editableName by remember { mutableStateOf("") }
    var editablePhoneNumber by remember { mutableStateOf("") }
    var editableAddress by remember { mutableStateOf("") }

    LaunchedEffect(customerProfile) {
        customerProfile.let {
            editableName = it.name
            editablePhoneNumber = it.phone
            editableAddress = it.email
        }
    }
    //initializes your editable form fields whenever the customerProfile changes.

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTopBar(
                title = "Edit Profile",
                onBackClick = onBackClick,
                actions = {
                    Button(
                        onClick = {
                            profileViewModel.saveProfile()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC48639))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(100.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_profile_picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )

                // Name field
                Spacer(modifier = Modifier.height(30.dp))
                OutlinedTextField(
                    value = editableName,
                    onValueChange = { editableName = it },
                    label = { Text("Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name Icon") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFFE4AD62),
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Phone number field
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = editablePhoneNumber,
                    onValueChange = { editablePhoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFFE4AD62),
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Address field
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = editableAddress,
                    onValueChange = { editableAddress = it },
                    label = { Text("Address") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFFE4AD62),
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
}
