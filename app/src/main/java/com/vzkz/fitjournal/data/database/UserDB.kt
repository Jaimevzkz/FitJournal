package com.vzkz.fitjournal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vzkz.fitjournal.data.database.dao.UserDao
import com.vzkz.fitjournal.data.database.entities.Converters
import com.vzkz.fitjournal.data.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDB: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}