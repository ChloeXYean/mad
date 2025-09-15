package com.example.rasago.theme.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.StaffEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    staff = staff,
                    // Use staff's current status from DB as initial status
                    status = staff?.status?.replaceFirstChar { char -> char.uppercase() } ?: "Working"
                )
            }
        }
    }

    fun updateStatus(newStatus: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = newStatus) }
            // Here you would typically also update the status in your repository/database
        }
    }

//    fun loadStaff(staffId: Int) {
//        viewModelScope.launch {
//            val staff = getStaffFromDatabase(staffId)
//            setStaff(staff)
//        }
//    }
}
