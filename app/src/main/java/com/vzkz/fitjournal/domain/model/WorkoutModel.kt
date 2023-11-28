package com.vzkz.fitjournal.domain.model

import com.google.firebase.firestore.PropertyName
import com.vzkz.fitjournal.domain.model.Constants.DURATION
import com.vzkz.fitjournal.domain.model.Constants.EXCOUNT
import com.vzkz.fitjournal.domain.model.Constants.EXERCISEWEIGHT
import com.vzkz.fitjournal.domain.model.Constants.EXNUM
import com.vzkz.fitjournal.domain.model.Constants.EXORDER
import com.vzkz.fitjournal.domain.model.Constants.REPS
import com.vzkz.fitjournal.domain.model.Constants.REST
import com.vzkz.fitjournal.domain.model.Constants.SETNUM
import com.vzkz.fitjournal.domain.model.Constants.WOTNAME
import com.vzkz.fitjournal.domain.model.Constants.WOTORDER

data class WorkoutModel(
    val wotName: String,
    val duration: Int,
    val exCount: Int,
    val wotOrder: Int,
    val exercises: List<Exercises>
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            WOTNAME to wotName,
            DURATION to duration,
            EXCOUNT to exCount,
            WOTORDER to wotOrder
        )
    }
}


data class Exercises(
    val rest: Int,
    val exData: ExerciseModel,
    val setNum: Int,
    val exOrder: Int,
    val setXrepXweight: List<SetXrepXweight>
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            REST to rest,
            SETNUM to setNum,
            EXORDER to exOrder
        )
    }
}

data class SetXrepXweight(
    @PropertyName(EXNUM) val exNum: String,
    @PropertyName(REPS) val reps: Int = 8,
    @PropertyName(EXERCISEWEIGHT) val weight: Int = 10){
    fun toMap(): Map<String, Any> {
        return mapOf(
            EXNUM to exNum,
            REPS to reps,
            EXERCISEWEIGHT to weight
        )
    }
}