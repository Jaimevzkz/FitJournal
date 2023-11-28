package com.vzkz.fitjournal.domain.model

import com.google.firebase.firestore.PropertyName
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
import com.vzkz.fitjournal.domain.model.Constants.WORKOUTS

data class UserModel(
    @PropertyName(UID) val uid: String,
    @PropertyName(NICKNAME) val nickname: String,
    @PropertyName(EMAIL) val email: String?,
    @PropertyName(FIRSTNAME) val firstname: String,
    @PropertyName(LASTNAME) val lastname: String,
    @PropertyName(WEIGHT) val weight: Int? = null,
    @PropertyName(AGE) val age: Int? = null,
    @PropertyName(GENDER) val gender: String? = null,
    @PropertyName(GOAL) val goal: String? = null,
    @PropertyName(WORKOUTS) var workouts: List<WorkoutModel>? = null
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