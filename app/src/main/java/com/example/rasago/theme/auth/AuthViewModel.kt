package com.example.rasago.theme.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val isLoginSuccess: Boolean = false,
    val isStaff: Boolean = false,
    val customer: CustomerEntity? = null,
    val staff: StaffEntity? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun loginUser() {
        // 输入验证
        when {
            email.isBlank() && password.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your email and password") }
                return
            }
            email.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your email") }
                return
            }
            password.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your password") }
                return
            }
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.smartLogin(email, password)
            if (result.isSuccess) {
                _loginState.update {
                    it.copy(
                        isLoginSuccess = true,
                        isStaff = result.isStaff,
                        customer = result.customer,
                        staff = result.staff,
                        isLoading = false
                    )
                }
            } else {
                _loginState.update {
                    it.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun logout() {
        _loginState.value = LoginState()
    }

    fun clearError() {
        _loginState.update { it.copy(error = null) }
    }

    fun registerCustomer(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        confirmPassword: String,
        gender: String = "N/A"
    ) {
        when {
            name.isBlank() && email.isBlank() && phoneNumber.isBlank() && password.isBlank() && confirmPassword.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your details") }
                return
            }
            name.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your username") }
                return
            }
            email.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your email") }
                return
            }
            phoneNumber.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your phone number") }
                return
            }
            password.isBlank() -> {
                _loginState.update { it.copy(error = "Please enter your password") }
                return
            }
            confirmPassword.isBlank() -> {
                _loginState.update { it.copy(error = "Please confirm your password") }
                return
            }
            password != confirmPassword -> {
                _loginState.update { it.copy(error = "Passwords do not match") }
                return
            }
            password.length < 6 -> {
                _loginState.update { it.copy(error = "Password must be at least 6 characters") }
                return
            }
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }
            val result = userRepository.registerCustomer(name, email, password, phoneNumber, gender)
            if (result.isSuccess && result.customerId != null) {
                _loginState.update {
                    it.copy(
                        isLoginSuccess = true,
                        isStaff = false,
                        customer = com.example.rasago.data.entity.CustomerEntity(
                            customerId = result.customerId.toInt(),
                            name = name,
                            email = email,
                            password = password,
                            phone = phoneNumber,
                            gender = gender
                        ),
                        isLoading = false
                    )
                }
            } else {
                _loginState.update {
                    it.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}

