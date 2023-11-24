package com.vzkz.fitjournal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vzkz.fitjournal.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE nickname = :nickname" )
    suspend fun getUser(nickname: String): UserEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM user_table" )
    suspend fun clearUsers()

}