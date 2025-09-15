package com.example.rasago.theme.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            // Use the smartLogin function which handles both staff and customer logic
            val result = userRepository.smartLogin(email, password)
            if (result.isSuccess) {
                _loginState.update {
                    it.copy(
                        isLoginSuccess = true,
                        isStaff = result.isStaff,
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

    fun clearError() {
        _loginState.update { it.copy(error = null) }
    }
}

