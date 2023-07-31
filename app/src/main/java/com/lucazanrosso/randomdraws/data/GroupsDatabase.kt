package com.lucazanrosso.randomdraws.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}

//@Database(entities = [Item::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun itemDao(): ItemDao
//
//    companion object {
//        @Volatile
//        private var Instance: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            // if the Instance is not null, return it, otherwise create a new database instance.
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
//                    .build().also { Instance = it }
//            }
//        }
//    }
//}