package com.example.rasago.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rasago.data.dao.MenuItemDao
import com.example.rasago.data.dao.OrderDao
import com.example.rasago.data.dao.OrderItemDao
import com.example.rasago.data.entity.MenuItemEntity
import com.example.rasago.data.entity.OrderEntity
import com.example.rasago.data.entity.OrderItemEntity

@Database(
    entities = [OrderEntity::class, OrderItemEntity::class, MenuItemEntity::class], //Entity class for kotlin
    version = 1, //Increment when change schema (add/remove columns or table)
    exportSchema = false //true to export for tracking history, vice versa
)
//Room use those class definitions to create tables
//Room = a library from Android to make working with SQL lite database more easier, if without -> write raw SQL queries
abstract class AppDatabase : RoomDatabase(){
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun menuItemDao(): MenuItemDao

    //Singleton = shared one database
    companion object{
        @Volatile //With this worker(threads) able to see the up-to-date value
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){ //If instance = not null -> return, else lock synchronized so only 1 thread creates the database instance
                //Thread = worker in app, if block thread or multiple thread -> crash
                //So only main thread and background thread
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}