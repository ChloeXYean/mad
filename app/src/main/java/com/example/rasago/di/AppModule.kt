package com.example.rasago.di

import OrderRepository
import android.content.Context
import androidx.room.Room
import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.database.AppDatabase
import com.example.rasago.data.repository.MenuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, //Use Database
            "my_database"
        ).build()

    @Provides
    fun provideMenuDao(db: AppDatabase): MenuItemDao = db.menuItemDao()

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideOrderItemDao (db: AppDatabase): OrderItemDao = db.orderItemDao()

    @Provides
    fun provideMenuRepository(menuDao: MenuItemDao) = MenuRepository(menuDao)

    @Provides
    fun provideOrderRepository(orderDao: OrderDao, orderItemDao: OrderItemDao) = OrderRepository(orderDao, orderItemDao)
}