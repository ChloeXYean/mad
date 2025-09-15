package com.example.rasago.theme.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represents the result of a login attempt.
 * @param success True if the login was successful.
 * @param isStaff True if the logged-in user is a staff member.
 * @param error An optional error message if the login fails.
 */
data class LoginResult(
    val success: Boolean,
    val isStaff: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult = _loginResult.asStateFlow()

    /**
     * Attempts to log a user in by checking credentials against the database.
     * It first checks the staff table, then the customer table.
     */
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                // First, try to log in as a staff member.
                val staff = userRepository.loginStaff(email, password)
                if (staff != null) {
                    _loginResult.value = LoginResult(success = true, isStaff = true)
                    return@launch
                }

                // If not a staff member, try to log in as a customer.
                val customer = userRepository.loginCustomer(email, password)
                if (customer != null) {
                    _loginResult.value = LoginResult(success = true, isStaff = false)
                    return@launch
                }

                // If neither login is successful, post a failure result.
                _loginResult.value = LoginResult(success = false, error = "Invalid email or password.")

            } catch (e: Exception) {
            // Handle potential database errors.
            _loginResult.value = LoginResult(success = false, error = "An error occurred during login.")
        }
        }
    }

    /**
     * Resets the login result to prevent re-navigation on configuration changes.
     */
    fun resetLoginResult() {
        _loginResult.value = null
    }
}

