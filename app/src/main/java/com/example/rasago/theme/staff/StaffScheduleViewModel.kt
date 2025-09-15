package com.example.rasago.theme.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StaffScheduleUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

@HiltViewModel
class StaffScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _staffList = MutableStateFlow<List<StaffEntity>>(emptyList())
    val staffList: StateFlow<List<StaffEntity>> = _staffList.asStateFlow()

    private val _uiState = MutableStateFlow(StaffScheduleUiState())
    val uiState: StateFlow<StaffScheduleUiState> = _uiState.asStateFlow()

    // 假设当前登录的是经理
    private val currentManager = StaffEntity(
        name = "当前经理",
        email = "mgr_current@rasago.com",
        password = "password",
        role = "manager",
        status = "active",
        jobTime = System.currentTimeMillis()
    )

    init {
        loadStaffList()
    }

    /**
     * 加载员工列表
     */
    private fun loadStaffList() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val staffList = userRepository.getAllActiveStaff()
                println("DEBUG: Loaded ${staffList.size} staff members")
                staffList.forEach { staff: StaffEntity ->
                    println("DEBUG: Staff - Name: ${staff.name}, Role: ${staff.role}, Status: ${staff.status}")
                }
                _staffList.value = staffList
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Loaded ${staffList.size} staff members"
                )
            } catch (e: Exception) {
                println("DEBUG: Error loading staff list: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "加载员工列表失败: ${e.message}"
                )
            }
        }
    }

    /**
     * 更新员工工作状态
     */
    fun updateStaffWorkStatus(email: String, newStatus: String) {
        viewModelScope.launch {
            try {
                userRepository.updateStaffStatusDirect(email, newStatus)
                loadStaffList() 
                _uiState.value = _uiState.value.copy(
                    message = "Staff status updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Fail to update status: ${e.message}"
                )
            }
        }
    }

    fun updateStaffWorkTime(email: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    message = "Job Time Updated: $startTime - $endTime"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Fail to update job time: ${e.message}"
                )
            }
        }
    }
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    fun reloadStaffList() {
        loadStaffList()
    }


    fun checkDatabaseData() {
        viewModelScope.launch {
            try {
                val activeStaff = userRepository.getAllActiveStaff()
                println("DEBUG: Database check - Active staff: ${activeStaff.size}")
                activeStaff.forEach { staff: StaffEntity ->
                    println("DEBUG: Active staff - Name: ${staff.name}, Role: ${staff.role}, Status: ${staff.status}")
                }
                _uiState.value = _uiState.value.copy(
                    message = "Database check: ${activeStaff.size} active staff"
                )
            } catch (e: Exception) {
                println("DEBUG: Error checking database: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    error = "Database check failed: ${e.message}"
                )
            }
        }
    }
}
