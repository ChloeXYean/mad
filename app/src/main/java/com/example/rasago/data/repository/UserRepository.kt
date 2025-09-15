package com.example.rasago.data.repository

import com.example.rasago.data.dao.CustomerDao
import com.example.rasago.data.dao.StaffDao
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.theme.utils.RoleDetector
import javax.inject.Inject
import javax.inject.Singleton

data class RegistrationResult(
    val isSuccess: Boolean,
    val message: String,
    val customerId: Long?
)

data class UpdateProfileResult(
    val isSuccess: Boolean,
    val message: String,
    val updatedCustomer: CustomerEntity? = null
)

@Singleton
class UserRepository @Inject constructor(
    private val customerDao: CustomerDao,
    private val staffDao: StaffDao
) {

    /**
     * Attempts to log in a customer by checking their credentials against the database.
     * @param email The customer's email.
     * @param password The customer's password.
     * @return A CustomerEntity if successful, otherwise null.
     */
    suspend fun loginCustomer(email: String, password: String): CustomerEntity? {
        return customerDao.login(email, password)
    }

    /**
     * Attempts to log in a staff member by checking their credentials against the database.
     * @param email The staff member's email.
     * @param password The staff member's password.
     * @return A StaffEntity if successful, otherwise null.
     */
    suspend fun loginStaff(email: String, password: String): StaffEntity? {
        return staffDao.login(email, password)
    }

    /**
     * @param email
     * @param password
     * @return
     */
    suspend fun smartLogin(email: String, password: String): LoginResult {
        // First, detect if the email belongs to a staff member
        val isStaffEmail = RoleDetector.isStaffEmail(email)

        if (isStaffEmail) {
            // Attempt staff login
            val staff = staffDao.login(email, password)
            if (staff != null) {
                return LoginResult(
                    isSuccess = true,
                    isStaff = true,
                    staff = staff,
                    message = "Staff Login Successful"
                )
            }

            // Staff login failed
            val existingStaff = staffDao.getByEmail(email)
            return if (existingStaff == null) {
                LoginResult(
                    isSuccess = false,
                    isStaff = true,
                    message = "Staff doesn't exist, please contact manager"
                )
            } else {
                LoginResult(
                    isSuccess = false,
                    isStaff = true,
                    message = "Wrong password"
                )
            }
        } else {
            // Attempt customer login
            val customer = customerDao.login(email, password)
            if (customer != null) {
                return LoginResult(
                    isSuccess = true,
                    isStaff = false,
                    customer = customer,
                    message = "Login Successful"
                )

            }

            // Customer login failed
            val existingCustomer = customerDao.getByEmail(email)
            return if (existingCustomer == null) {
                LoginResult(
                    isSuccess = false,
                    isStaff = false,
                    message = "Account not found. Please register first."
                )
            } else {
                LoginResult(
                    isSuccess = false,
                    isStaff = false,
                    message = "Wrong Password"
                )
            }
        }
    }

    /**
     * Customer Registration
     */
    suspend fun registerCustomer(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        gender: String = "N/A"
    ): RegistrationResult {
        // Check if username already exists
        if (customerDao.isNameExists(name)) {
            return RegistrationResult(
                isSuccess = false,
                message = "Username already taken. Please choose a different one.",
                customerId = null
            )
        }
        
        // Check if email already exists
        if (customerDao.isEmailExists(email)) {
            return RegistrationResult(
                isSuccess = false,
                message = "Email already exists. Please use a different email.",
                customerId = null
            )
        }

        val newCustomer = CustomerEntity(
            name = name,
            phone = phoneNumber,
            email = email,
            password = password,
            gender = gender
        )

        return try {
            val customerId = customerDao.insert(newCustomer)
            RegistrationResult(
                isSuccess = true,
                message = "Registration successful!",
                customerId = customerId
            )
        } catch (e: Exception) {
            RegistrationResult(
                isSuccess = false,
                message = "Registration failed. Please try again.",
                customerId = null
            )
        }
    }

    /**
     * Get customer by email
     */
    suspend fun getCustomerByEmail(email: String): CustomerEntity? {
        return customerDao.getByEmail(email)
    }

    /**
     * Get staff by email
     */
    suspend fun getStaffByEmail(email: String): StaffEntity? {
        return staffDao.getByEmail(email)
    }

    /**
     * Get all active customers
     */
    suspend fun getAllActiveCustomers(): List<CustomerEntity> {
        return customerDao.getAllActive()
    }

    /**
     * Get all active staff
     */
    suspend fun getAllActiveStaff(): List<StaffEntity> {
        return staffDao.getAllActive()
    }

    /**
     * Update customer profile information
     */
    suspend fun updateCustomerProfile(
        customerId: Int,
        newName: String,
        newPhone: String,
        newGender: String
    ): UpdateProfileResult {
        return try {
            // Get current customer
            val currentCustomer = customerDao.getById(customerId)
            if (currentCustomer == null) {
                return UpdateProfileResult(
                    isSuccess = false,
                    message = "Customer not found"
                )
            }

            // Check if new name is different and if it already exists
            if (newName != currentCustomer.name && customerDao.isNameExists(newName)) {
                return UpdateProfileResult(
                    isSuccess = false,
                    message = "Name already taken. Please choose a different one."
                )
            }

            // Update customer
            val updatedCustomer = currentCustomer.copy(
                name = newName,
                phone = newPhone,
                gender = newGender
            )
            
            customerDao.update(updatedCustomer)
            
            UpdateProfileResult(
                isSuccess = true,
                message = "Profile updated successfully!",
                updatedCustomer = updatedCustomer
            )
        } catch (e: Exception) {
            UpdateProfileResult(
                isSuccess = false,
                message = "Failed to update profile. Please try again."
            )
        }
    }

    /**
     * Update staff status (Manager only)
     */
    suspend fun updateStaffStatus(
        adminStaff: StaffEntity,
        targetEmail: String,
        newStatus: String
    ): Boolean {
        // Check if the executor has management permission
        if (!RoleDetector.hasManagementPermission(adminStaff.role)) {
            return false
        }

        return try {
            val targetStaff = staffDao.getByEmail(targetEmail)
            if (targetStaff != null) {
                staffDao.updateStatus(targetEmail, newStatus)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

// Data class for login result, moved to UserRepository for better organization
data class LoginResult(
    val isSuccess: Boolean,
    val isStaff: Boolean,
    val customer: CustomerEntity? = null,
    val staff: StaffEntity? = null,
    val message: String = ""
)
