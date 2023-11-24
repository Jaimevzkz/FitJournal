package com.vzkz.fitjournal.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vzkz.fitjournal.domain.model.UserModel

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo("nickname") val nickname: String,
    @ColumnInfo("userData") val userData: UserModel
)