package com.example.rasago.ui.theme.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository // handles staff + customer DB
) : ViewModel() {

    private val _customer = MutableLiveData<CustomerEntity?>()
    val customer: LiveData<CustomerEntity?> = _customer

    private val _staff = MutableLiveData<StaffEntity?>()
    val staff: LiveData<StaffEntity?> = _staff

    fun loadCustomerById(customerId: Long) {
        viewModelScope.launch {
            _customer.value = userRepository.getCustomerById(customerId)
        }
    }

    fun loadStaffById(staffId: Long) {
        viewModelScope.launch {
            _staff.value = userRepository.getStaffById(staffId)
        }
    }

    fun updateCustomer(updated: CustomerEntity) {
        viewModelScope.launch {
            userRepository.updateCustomer(updated)
            _customer.value = updated
        }
    }

    fun updateStaff(updated: StaffEntity) {
        viewModelScope.launch {
            userRepository.updateStaff(updated)
            _staff.value = updated
        }
    }
}

