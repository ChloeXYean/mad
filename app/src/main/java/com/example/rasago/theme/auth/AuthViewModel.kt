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

    /**
     * Refresh user information after profile update
     */
    fun refreshUserInfo() {
        viewModelScope.launch {
            val currentState = _loginState.value
            if (currentState.isStaff && currentState.staff != null) {
                // Refresh staff info
                val refreshedStaff = userRepository.loginStaff(currentState.staff!!.email, currentState.staff!!.password ?: "")
                if (refreshedStaff != null) {
                    _loginState.update { 
                        it.copy(staff = refreshedStaff) 
                    }
                }
            } else if (!currentState.isStaff && currentState.customer != null) {
                // Refresh customer info
                val refreshedCustomer = userRepository.loginCustomer(currentState.customer!!.email, currentState.customer!!.password ?: "")
                if (refreshedCustomer != null) {
                    _loginState.update { 
                        it.copy(customer = refreshedCustomer) 
                    }
                }
            }
        }
    }
}

