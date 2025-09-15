package com.example.rasago.theme.utils

object RoleDetector {

    // Role constants
    const val ROLE_CUSTOMER = "customer"
    const val ROLE_CASHIER = "cashier"
    const val ROLE_KITCHEN = "kitchen"
    const val ROLE_MANAGER = "manager"   // Manager has the highest permission

    /**
     * Automatically detects user role based on email prefix
     * @param email User's email
     * @return Detected user role string
     */
    fun detectRoleFromEmail(email: String): String {
        val emailPrefix = email.substringBefore("@").lowercase()

        return when {
            // Cashier - starts with cas_
            emailPrefix.startsWith("cas_") -> ROLE_CASHIER

            // Kitchen - starts with kit_
            emailPrefix.startsWith("kit_") -> ROLE_KITCHEN

            // Manager - starts with mgr_ (highest permission)
            emailPrefix.startsWith("mgr_") -> ROLE_MANAGER

            // Default to customer
            else -> ROLE_CUSTOMER
        }
    }

    /**
     * Checks if the email is a staff email
     * @param email User's email
     * @return true if it is a staff email
     */
    fun isStaffEmail(email: String): Boolean {
        val role = detectRoleFromEmail(email)
        return role != ROLE_CUSTOMER
    }

    /**
     * Gets the display name for a role
     * @param role User role string
     * @return Chinese display name for the role
     */
    fun getRoleDisplayName(role: String): String {
        return when (role) {
            ROLE_CUSTOMER -> "Customer"
            ROLE_CASHIER -> "Cashier"
            ROLE_KITCHEN -> "Kitchen Staff"
            ROLE_MANAGER -> "Manager"
            else -> "Unknown Role"
        }
    }

    /**
     * Checks if the user has permission to manage staff
     * @param role User role string
     * @return true if they have management permission
     */
    fun hasManagementPermission(role: String): Boolean {
        return role == ROLE_MANAGER  // Only managers have staff management permission
    }

    /**
     * Checks if the user has permission to manage the menu
     * @param role User role string
     * @return true if they have menu management permission
     */
    fun canManageMenu(role: String): Boolean {
        return role == ROLE_MANAGER // Only managers can manage the menu
    }

    /**
     * Checks if the user can handle orders
     * @param role User role string
     * @return true if they can handle orders
     */
    fun canHandleOrders(role: String): Boolean {
        return role in listOf(ROLE_CASHIER, ROLE_MANAGER)
    }

    /**
     * Checks if the user can handle kitchen tasks
     * @param role User role string
     * @return true if they can handle kitchen tasks
     */
    fun canHandleKitchen(role: String): Boolean {
        return role in listOf(ROLE_KITCHEN, ROLE_MANAGER)
    }
}

