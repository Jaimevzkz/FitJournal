package com.vzkz.fitjournal.data.database.entities

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.vzkz.fitjournal.domain.model.UserModel


class Converters {
    @TypeConverter
    fun fromUserModel(userModel: UserModel): String {
        return Gson().toJson(userModel)
    }

    @TypeConverter
    fun toUserModel(userModelString: String): UserModel {
        val type = object : TypeToken<UserModel>() {}.type
        return Gson().fromJson(userModelString, type)
    }
}