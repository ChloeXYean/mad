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
    val gender: String = "",
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

    private var currentCustomer: CustomerEntity? = null
    private var currentStaff: StaffEntity? = null

    fun loadProfile(customer: CustomerEntity?, staff: StaffEntity?) {
        currentCustomer = customer
        currentStaff = staff
        
        if (customer != null) {
            _uiState.update {
                it.copy(
                    name = customer.name,
                    email = customer.email,
                    phone = customer.phone,
                    gender = customer.gender
                )
            }
        } else if (staff != null) {
            _uiState.update {
                it.copy(
                    name = staff.name,
                    email = staff.email,
                    phone = staff.phone ?: "",
                    gender = "" // Staff don't have gender field
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

    fun updateGender(gender: String) {
        _uiState.update { it.copy(gender = gender) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val success = if (currentCustomer != null) {
                    // Update customer profile
                    val updatedCustomer = currentCustomer!!.copy(
                        name = _uiState.value.name,
                        phone = _uiState.value.phone,
                        gender = _uiState.value.gender
                    )
                    userRepository.updateCustomerProfile(updatedCustomer)
                } else if (currentStaff != null) {
                    // Update staff profile
                    val updatedStaff = currentStaff!!.copy(
                        name = _uiState.value.name,
                        phone = _uiState.value.phone
                    )
                    userRepository.updateStaffProfile(updatedStaff)
                } else {
                    false
                }
                
                if (success) {
                    _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = "Failed to update profile. Please try again."
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "An error occurred: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun resetSaveStatus() {
        _uiState.update { it.copy(saveSuccess = false, error = null) }
    }
}

