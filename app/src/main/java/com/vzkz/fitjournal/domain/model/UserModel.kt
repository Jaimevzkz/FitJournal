package com.vzkz.fitjournal.domain.model

import com.vzkz.fitjournal.data.database.entities.UserEntity
import com.vzkz.fitjournal.domain.model.Constants.AGE
import com.vzkz.fitjournal.domain.model.Constants.EMAIL
import com.vzkz.fitjournal.domain.model.Constants.FIRSTNAME
import com.vzkz.fitjournal.domain.model.Constants.GENDER
import com.vzkz.fitjournal.domain.model.Constants.GOAL
import com.vzkz.fitjournal.domain.model.Constants.LASTNAME
import com.vzkz.fitjournal.domain.model.Constants.NICKNAME
import com.vzkz.fitjournal.domain.model.Constants.UID
import com.vzkz.fitjournal.domain.model.Constants.WEIGHT

data class UserModel(
    val uid: String,
    val nickname: String,
    val email: String?,
    val firstname: String,
    val lastname: String,
    val weight: Int? = null,
    val age: Int? = null,
    val gender: String? = null,
    val goal: String? = null,
    var workouts: List<WorkoutModel>? = null
) {
    fun toRoomEntity(): UserEntity {
        return UserEntity(
            nickname = this.nickname,
            userData = this
        )
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            UID to uid,
            NICKNAME to nickname,
            EMAIL to email,
            FIRSTNAME to firstname,
            LASTNAME to lastname,
            WEIGHT to weight,
            AGE to age,
            GENDER to gender,
            GOAL to goal
        )
    }

    // No argument constructor
    constructor() : this("", "", "", "", "", null, null, null, null, null)

}