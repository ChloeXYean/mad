package com.example.rasago

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    @DrawableRes val profileImageRes: Int = R.drawable.default_profile_picture
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

//    init {
//        loadUserProfile()
//    }
//
//    private fun loadUserProfile() {
//        viewModelScope.launch {
//            val user = DummyData.customerProfile
//            _uiState.update {
//                it.copy(
//                    name = user.name,
//                    email = user.email,
//                    phone = user.phone,
//                    profileImageRes = user.profileImageRes
//                )
//            }
//        }
//    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            // TODO: Add logic to save the updated profile to the database.
            println("Profile saved: ${_uiState.value}")
        }
    }
}

