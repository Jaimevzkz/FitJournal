package com.vzkz.fitjournal.domain.model

import com.google.firebase.firestore.PropertyName
import com.vzkz.fitjournal.domain.model.Constants.DIFFICULTY
import com.vzkz.fitjournal.domain.model.Constants.ERRORSTR
import com.vzkz.fitjournal.domain.model.Constants.EXNAME
import com.vzkz.fitjournal.domain.model.Constants.INSTRUCTIONS
import com.vzkz.fitjournal.domain.model.Constants.MUSCLE


data class ExerciseModel(
    val exName: String,
    val muscle: String,
    val difficulty: String,
    val instructions: String
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            EXNAME to exName,
            MUSCLE to muscle,
            DIFFICULTY to difficulty,
            INSTRUCTIONS to instructions
        )
    }

    //No argument constructor
    constructor() : this(exName = ERRORSTR, muscle = ERRORSTR, difficulty = ERRORSTR, instructions = ERRORSTR)
}