package com.example.rasago.data.repository

import com.example.rasago.data.dao.CustomerDao
import com.example.rasago.data.dao.StaffDao
import com.example.rasago.data.entity.CustomerEntity
import com.example.rasago.data.entity.StaffEntity
import com.example.rasago.theme.utils.RoleDetector
import javax.inject.Inject
import javax.inject.Singleton

// 登录结果数据类
data class LoginResult(
    val isSuccess: Boolean,
    val isStaff: Boolean,
    val customer: CustomerEntity? = null,
    val staff: StaffEntity? = null,
    val message: String = ""
)

@Singleton
class UserRepository @Inject constructor(
    private val customerDao: CustomerDao,
    private val staffDao: StaffDao
) {

    /**
     * @param email
     * @param password
     * @return
     */
    suspend fun smartLogin(email: String, password: String): LoginResult {
        // 首先检测邮箱是否为员工邮箱
        val isStaffEmail = RoleDetector.isStaffEmail(email)

        if (isStaffEmail) {
            // 尝试员工登录
            val staff = staffDao.login(email, password)
            if (staff != null) {
                return LoginResult(
                    isSuccess = true,
                    isStaff = true,
                    staff = staff,
                    message = "Staff Login Successful"
                )
            }

            // 员工登录失败
            val existingStaff = staffDao.getByEmail(email)
            if (existingStaff == null) {
                return LoginResult(
                    isSuccess = false,
                    isStaff = true,
                    message = "Staff doesn't exitst, please contact manager"
                )
            } else {
                return LoginResult(
                    isSuccess = false,
                    isStaff = true,
                    message = "Wrong password"
                )
            }
        } else {
            // 尝试顾客登录
            val customer = customerDao.login(email, password)
            if (customer != null) {
                return LoginResult(
                    isSuccess = true,
                    isStaff = false,
                    customer = customer,
                    message = "Login Successful"
                )
            }

            // 顾客登录失败，检查是否需要自动创建顾客账户
            val existingCustomer = customerDao.getByEmail(email)
            if (existingCustomer == null) {
                // 自动创建顾客账户（演示用途）
                val newCustomer = CustomerEntity(
                    name = email.substringBefore("@"),
                    phone = "",
                    email = email,
                    password = password,
                    gender = "N/A"
                )

                try {
                    val customerId = customerDao.insert(newCustomer)
                    return LoginResult(
                        isSuccess = true,
                        isStaff = false,
                        customer = newCustomer.copy(customerId = customerId.toInt()),
                        message = "Account created and already login"
                    )
                } catch (e: Exception) {
                    return LoginResult(
                        isSuccess = false,
                        isStaff = false,
                        message = "Fail to create account"
                    )
                }
            } else {
                return LoginResult(
                    isSuccess = false,
                    isStaff = false,
                    message = "Wrong Password"
                )
            }
        }
    }

    /**
     * 顾客注册
     */
    suspend fun registerCustomer(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        gender: String = "N/A"
    ): Long? {
        // 检查邮箱是否已存在
        if (customerDao.isEmailExists(email)) {
            return null // 邮箱已存在
        }

        val newCustomer = CustomerEntity(
            name = name,
            phone = phoneNumber,
            email = email,
            password = password,
            gender = gender
        )

        return try {
            customerDao.insert(newCustomer)
        } catch (e: Exception) {
            null
        }
    }


    /**
     * 根据邮箱获取顾客
     */
    suspend fun getCustomerByEmail(email: String): CustomerEntity? {
        return customerDao.getByEmail(email)
    }

    /**
     * 根据邮箱获取员工
     */
    suspend fun getStaffByEmail(email: String): StaffEntity? {
        return staffDao.getByEmail(email)
    }

    /**
     * 获取所有活跃顾客
     */
    suspend fun getAllActiveCustomers(): List<CustomerEntity> {
        return customerDao.getAllActive()
    }

    /**
     * 获取所有活跃员工
     */
    suspend fun getAllActiveStaff(): List<StaffEntity> {
        return staffDao.getAllActive()
    }

    /**
     * 更新员工状态（仅管理员可操作）
     */
    suspend fun updateStaffStatus(
        adminStaff: StaffEntity,
        targetEmail: String,
        newStatus: String
    ): Boolean {
        // 检查执行者是否有管理权限
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