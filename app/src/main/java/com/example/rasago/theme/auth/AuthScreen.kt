package com.example.rasago.theme.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rasago.R
import com.example.rasago.ui.theme.RasagoApp

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (isStaff: Boolean) -> Unit = { _ -> }
) {
    var loginEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f,
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x90000000))
        )

        // Foreground content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo + caption
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Rasa Go",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Order Fast, Rasa Best!",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp, top = 8.dp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Login form
            Text(
                text = "Login to your account",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp),
                color = Color.White
            )

            AuthTextField(
                label = "Email Address",
                value = loginEmail,
                onValueChange = { loginEmail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            AuthPasswordField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = {
                    // TODO: 实际登录逻辑
                    // 默认登录为顾客
                    onLoginSuccess(false)
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Sign up prompt
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = "Sign Up",
                    fontSize = 16.sp,
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    onRegisterSuccess: () -> Unit = {}
) {
    var registerUsername by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPhoneNo by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var registerConfirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.login_background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                alpha = 0.5f,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x90000000))
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(40.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo + caption
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Rasa Go",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Order Fast, Rasa Best!",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = Color.White
                )

                AuthTextField(
                    label = "Username",
                    value = registerUsername,
                    onValueChange = { registerUsername = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                AuthTextField(
                    label = "Email Address",
                    value = registerEmail,
                    onValueChange = { registerEmail = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                AuthTextField(
                    label = "Phone Number",
                    value = registerPhoneNo,
                    onValueChange = { registerPhoneNo = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )

                AuthPasswordField(
                    label = "Password",
                    value = registerPassword,
                    onValueChange = { registerPassword = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                AuthPasswordField(
                    label = "Confirm Password",
                    value = registerConfirmPassword,
                    onValueChange = { registerConfirmPassword = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                Button(
                    onClick = {
                        // TODO: 实际注册逻辑
                        onRegisterSuccess()
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Register",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        color = Color(0xFF2196F3),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(44.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.White
        )
    )
}

@Composable
fun AuthPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = image,
                    contentDescription = description,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        shape = RoundedCornerShape(44.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RasagoApp {
        LoginScreen(
            navController = rememberNavController(),
            onLoginSuccess = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RasagoApp {
        RegisterScreen(
            navController = rememberNavController(),
            onRegisterSuccess = {}
        )
    }
}
