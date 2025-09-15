package com.example.rasago.di

import android.content.Context
import androidx.room.Room
import com.example.rasago.data.dao.CustomerDao
import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.dao.StaffDao
import com.example.rasago.data.database.AppDatabase
import com.example.rasago.data.repository.ImageRepository
import com.example.rasago.data.repository.MenuRepository
import com.example.rasago.data.repository.OrderRepository
import com.example.rasago.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "rasago_database"
        ).build()
    }

    // --- DAO Providers ---
    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideOrderItemDao(db: AppDatabase): OrderItemDao = db.orderItemDao()

    @Provides
    fun provideMenuDao(db: AppDatabase): MenuItemDao = db.menuItemDao()

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Provides
    fun provideStaffDao(db: AppDatabase): StaffDao = db.staffDao()

    // --- Repository Providers ---
    @Provides
    @Singleton
    fun provideOrderRepository(
        orderDao: OrderDao,
        menuItemDao: MenuItemDao,
        orderItemDao: OrderItemDao
    ): OrderRepository {
        return OrderRepository(orderDao, menuItemDao, orderItemDao)
    }

    @Provides
    @Singleton
    fun provideMenuRepository(menuDao: MenuItemDao): MenuRepository {
        return MenuRepository(menuDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        customerDao: CustomerDao,
        staffDao: StaffDao
    ): UserRepository {
        return UserRepository(customerDao, staffDao)
    }

    // No need to explicitly provide ImageRepository if it has @Inject constructor
}

