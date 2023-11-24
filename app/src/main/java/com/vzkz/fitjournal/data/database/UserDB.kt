package com.vzkz.fitjournal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vzkz.fitjournal.data.database.dao.UserDao
import com.vzkz.fitjournal.data.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDB: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}