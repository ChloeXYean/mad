package com.example.rasago.theme.utils

object RoleDetector {

    // 角色常量
    const val ROLE_CUSTOMER = "customer"
    const val ROLE_CASHIER = "cashier"
    const val ROLE_KITCHEN = "kitchen"
    const val ROLE_MANAGER = "manager"   // 经理拥有最高权限

    /**
     * 根据邮箱前缀自动检测用户角色
     * @param email 用户邮箱
     * @return 检测到的用户角色字符串
     */
    fun detectRoleFromEmail(email: String): String {
        val emailPrefix = email.substringBefore("@").lowercase()

        return when {
            // 收银员 - cas_ 开头
            emailPrefix.startsWith("cas_") -> ROLE_CASHIER

            // 厨房 - kit_ 开头
            emailPrefix.startsWith("kit_") -> ROLE_KITCHEN

            // 经理 - mgr_ 开头 (拥有最高权限)
            emailPrefix.startsWith("mgr_") -> ROLE_MANAGER

            // 默认为顾客
            else -> ROLE_CUSTOMER
        }
    }

    /**
     * 检查邮箱是否为员工邮箱
     * @param email 用户邮箱
     * @return true如果是员工邮箱
     */
    fun isStaffEmail(email: String): Boolean {
        val role = detectRoleFromEmail(email)
        return role != ROLE_CUSTOMER
    }

    /**
     * 获取角色的显示名称
     * @param role 用户角色字符串
     * @return 角色的中文显示名称
     */
    fun getRoleDisplayName(role: String): String {
        return when (role) {
            ROLE_CUSTOMER -> "顾客"
            ROLE_CASHIER -> "收银员"
            ROLE_KITCHEN -> "厨房员工"
            ROLE_MANAGER -> "经理"
            else -> "未知角色"
        }
    }

    /**
     * 获取角色建议的邮箱格式示例
     * @param role 用户角色字符串
     * @return 邮箱格式示例
     */
    fun getEmailFormatExample(role: String): String {
        return when (role) {
            ROLE_CUSTOMER -> "customer@example.com"
            ROLE_CASHIER -> "cas_mary@rasago.com"
            ROLE_KITCHEN -> "kit_david@rasago.com"
            ROLE_MANAGER -> "mgr_sarah@rasago.com"
            else -> "user@example.com"
        }
    }

    /**
     * 检查用户是否有管理权限（管理现有员工）
     * @param role 用户角色字符串
     * @return true如果有管理权限
     */
    fun hasManagementPermission(role: String): Boolean {
        return role == ROLE_MANAGER  // 经理有管理权限
    }

    /**
     * 检查用户是否可以处理订单
     * @param role 用户角色字符串
     * @return true如果可以处理订单
     */
    fun canHandleOrders(role: String): Boolean {
        return role in listOf(ROLE_CASHIER, ROLE_MANAGER)
    }

    /**
     * 检查用户是否可以处理厨房任务
     * @param role 用户角色字符串
     * @return true如果可以处理厨房任务
     */
    fun canHandleKitchen(role: String): Boolean {
        return role in listOf(ROLE_KITCHEN, ROLE_MANAGER)
    }

    /**
     * 检查用户是否可以查看所有数据和报表
     * @param role 用户角色字符串
     * @return true如果有查看权限
     */
    fun canViewAllData(role: String): Boolean {
        return role == ROLE_MANAGER
    }
}
