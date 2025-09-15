package com.example.rasago.theme.staff

import androidx.lifecycle.ViewModel
import com.example.rasago.data.entity.StaffEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class StaffMainUiState(
    val staff: StaffEntity? = null,
    val status: String = "Working"
)

@HiltViewModel
class StaffMainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StaffMainUiState())
    val uiState = _uiState.asStateFlow()

    fun setStaff(staff: StaffEntity?) {
        _uiState.update { it.copy(staff = staff, status = staff?.status ?: "Working") }
    }

    fun updateStatus(newStatus: String) {
        _uiState.update { it.copy(status = newStatus) }
        // In a real app, you would call the repository to save the new status to the database.
    }
}

