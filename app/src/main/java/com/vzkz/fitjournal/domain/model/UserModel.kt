package com.vzkz.fitjournal.domain.model

import android.net.Uri
import com.vzkz.fitjournal.data.database.entities.UserEntity
import com.vzkz.fitjournal.domain.model.Constants.AGE
import com.vzkz.fitjournal.domain.model.Constants.EMAIL
import com.vzkz.fitjournal.domain.model.Constants.ERRORSTR
import com.vzkz.fitjournal.domain.model.Constants.FIRSTNAME
import com.vzkz.fitjournal.domain.model.Constants.GENDER
import com.vzkz.fitjournal.domain.model.Constants.GOAL
import com.vzkz.fitjournal.domain.model.Constants.LASTNAME
import com.vzkz.fitjournal.domain.model.Constants.NICKNAME
import com.vzkz.fitjournal.domain.model.Constants.UID
import com.vzkz.fitjournal.domain.model.Constants.WEIGHT
import java.time.LocalDate

data class UserModel(
    var uid: String,
    var nickname: String,
    var email: String?,
    var firstname: String,
    var lastname: String,
    var weight: Int? = null,
    var age: Int? = null,
    var gender: String? = null,
    var goal: String? = null,
    var workouts: List<WorkoutModel>? = null,
    var wotDates: List<Pair<LocalDate, String>> = emptyList(),
    var progressPhotos: List<Uri> = emptyList(),
    var profilePhoto: Uri? = null
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

    fun datesToMap(): Map<String,String> {
        val mapRet = mutableMapOf<String, String>()
        for(date in wotDates){
            val dateToStr = "${date.first.dayOfMonth}/${date.first.monthValue}/${date.first.year}"
            mapRet[dateToStr] = date.second
        }
        return mapRet.toMap()
    }

    fun mapToDates(strMap: Map<String, String>){
        val ret = mutableListOf<Pair<LocalDate, String>>()
        for(mapEntry in strMap){
            val numbersList = mapEntry.key.split("/").map { it.toInt() }

            val date = LocalDate.of(numbersList[2], numbersList[1], numbersList[0])
            ret.add(Pair(date, mapEntry.value))
        }
        wotDates = ret.toList()
    }

    // No argument constructor
    constructor() : this(ERRORSTR, ERRORSTR, ERRORSTR, ERRORSTR, ERRORSTR, null, null, null, null, null)

}