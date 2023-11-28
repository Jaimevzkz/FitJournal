package com.vzkz.fitjournal.domain.model

import com.google.firebase.firestore.PropertyName
import com.vzkz.fitjournal.domain.model.Constants.DIFFICULTY
import com.vzkz.fitjournal.domain.model.Constants.EXNAME
import com.vzkz.fitjournal.domain.model.Constants.INSTRUCTIONS
import com.vzkz.fitjournal.domain.model.Constants.MUSCLE


data class ExerciseModel(
    @PropertyName(EXNAME) val exName: String,
    @PropertyName(MUSCLE) val muscle: String,
    @PropertyName(DIFFICULTY) val difficulty: String,
    @PropertyName(INSTRUCTIONS) val instructions: String
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            EXNAME to exName,
            MUSCLE to muscle,
            DIFFICULTY to difficulty,
            INSTRUCTIONS to instructions
        )
    }
}