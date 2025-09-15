package com.example.rasago.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rasago.data.dao.CustomerDao
import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.dao.StaffDao
import com.example.rasago.data.database.AppDatabase
import com.example.rasago.getPredefinedCustomers
import com.example.rasago.getPredefinedMenuItems
import com.example.rasago.getPredefinedStaff
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        prepopulateCallback: PrepopulateCallback
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rasago_db"
        )
            .addCallback(prepopulateCallback)
            .fallbackToDestructiveMigration() // Add this line
            .build()
    }

    @Provides
    @Singleton
    fun providePrepopulateCallback(
        dbProvider: Provider<AppDatabase>,
        coroutineScope: CoroutineScope
    ): PrepopulateCallback {
        return PrepopulateCallback(dbProvider, coroutineScope)
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    // DAO Providers
    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Provides
    fun provideStaffDao(db: AppDatabase): StaffDao = db.staffDao()

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideOrderItemDao(db: AppDatabase): OrderItemDao = db.orderItemDao()

    @Provides
    fun provideMenuItemDao(db: AppDatabase): MenuItemDao = db.menuItemDao()
}

class PrepopulateCallback(
    private val dbProvider: Provider<AppDatabase>,
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            // Get DAOs from the database provider
            val database = dbProvider.get()
            val menuItemDao = database.menuItemDao()
            val customerDao = database.customerDao()
            val staffDao = database.staffDao()

            // Pre-populate menu items if the table is empty
            if (menuItemDao.getCount() == 0) {
                menuItemDao.insertAll(getPredefinedMenuItems())
            }

            // Pre-populate customers if the table is empty
            if (customerDao.getCount() == 0) {
                customerDao.insert(getPredefinedCustomers().first())
            }

            // Pre-populate staff if the table is empty
            if (staffDao.getCount() == 0) {
                staffDao.insert(getPredefinedStaff().first())
            }
        }
    }
}

