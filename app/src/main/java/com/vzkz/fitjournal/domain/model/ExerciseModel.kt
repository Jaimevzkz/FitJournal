package com.vzkz.fitjournal.domain.model

import com.google.gson.annotations.SerializedName

data class ExerciseModel(
    val name: String,
    val muscle: String,
    val difficulty: String,
    val instructions: String
)