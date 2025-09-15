package com.example.rasago.theme.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProfile(customer: CustomerEntity?, staff: StaffEntity?) {
        if (customer != null) {
            _uiState.update {
                it.copy(
                    name = customer.name,
                    email = customer.email,
                    phone = customer.phone
                )
            }
        } else if (staff != null) {
            _uiState.update {
                it.copy(
                    name = staff.name,
                    email = staff.email,
                    phone = staff.phone ?: ""
                )
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun saveProfile() {
        // In a real app, you would have a more complex update logic
        // For now, we'll just simulate a successful save.
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulate network/db delay
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
        }
    }

    fun resetSaveStatus() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}

