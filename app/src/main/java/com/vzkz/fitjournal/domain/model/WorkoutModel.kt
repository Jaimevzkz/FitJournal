package com.vzkz.fitjournal.domain.model

import com.vzkz.fitjournal.domain.model.Constants.DURATION
import com.vzkz.fitjournal.domain.model.Constants.EXCOUNT
import com.vzkz.fitjournal.domain.model.Constants.EXERCISEWEIGHT
import com.vzkz.fitjournal.domain.model.Constants.EXID
import com.vzkz.fitjournal.domain.model.Constants.EXNUM
import com.vzkz.fitjournal.domain.model.Constants.EXORDER
import com.vzkz.fitjournal.domain.model.Constants.REPS
import com.vzkz.fitjournal.domain.model.Constants.REST
import com.vzkz.fitjournal.domain.model.Constants.SETNUM
import com.vzkz.fitjournal.domain.model.Constants.WID
import com.vzkz.fitjournal.domain.model.Constants.WOTNAME
import com.vzkz.fitjournal.domain.model.Constants.WOTORDER

data class WorkoutModel(
    var wid: String,
    val wotName: String,
    val duration: Int,
    val exCount: Int,
    val wotOrder: Int,
    val exercises: List<Exercises>
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            WID to wid,
            WOTNAME to wotName,
            DURATION to duration,
            EXCOUNT to exCount,
            WOTORDER to wotOrder
        )
    }
}


data class Exercises(
    var exid: String,
    val rest: Int,
    val exData: ExerciseModel,
    val setNum: Int,
    val exOrder: Int,
    val setXrepXweight: List<SetXrepXweight>
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            EXID to exid,
            REST to rest,
            SETNUM to setNum,
            EXORDER to exOrder
        )
    }
}

data class SetXrepXweight(
    val exNum: String,
    var reps: Int = 8,
    var weight: Int = 10
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            EXNUM to exNum,
            REPS to reps,
            EXERCISEWEIGHT to weight
        )
    }
}