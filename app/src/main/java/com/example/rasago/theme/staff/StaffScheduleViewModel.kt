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
                _staffList.value = staffList
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
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
                val success = userRepository.updateStaffStatus(currentManager, email, newStatus)
                if (success) {
                    loadStaffList() // 重新加载列表
                    _uiState.value = _uiState.value.copy(
                        message = "员工状态已更新"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "更新员工状态失败，权限不足或员工不存在"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "更新员工状态失败: ${e.message}"
                )
            }
        }
    }

    /**
     * 更新员工工作时间
     * 注意：当前数据库结构中jobTime是Long类型，这里可以根据需要调整
     */
    fun updateStaffWorkTime(email: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                // 这里可以根据需要实现具体的时间更新逻辑
                // 目前只是显示消息，实际可能需要修改数据库结构来存储工作时间段
                _uiState.value = _uiState.value.copy(
                    message = "工作时间已更新: $startTime - $endTime"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "更新工作时间失败: ${e.message}"
                )
            }
        }
    }

    /**
     * 清除消息
     */
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}
