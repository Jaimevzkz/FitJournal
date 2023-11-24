package com.vzkz.fitjournal.data.network.response

import com.google.gson.annotations.SerializedName
import com.vzkz.fitjournal.domain.model.ExerciseModel

data class ExerciseResponse(
    @SerializedName("name") val name: String,
    @SerializedName("muscle") val muscle: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("instructions") val instructions: String
) {
    fun toDomain(): ExerciseModel = ExerciseModel(name = name, muscle = muscle, difficulty = difficulty, instructions = instructions)
}