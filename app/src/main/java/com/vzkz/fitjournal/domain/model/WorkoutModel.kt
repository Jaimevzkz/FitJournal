package com.vzkz.fitjournal.domain.model

data class WorkoutModel(
    val wid: String,
    val wotName: String,
    val duration: Int,
    val exCount: Int,
    val exercises: List<Exercises>
)


data class Exercises(
    val eXid: String,
    val rest: Int,
    val exData: ExerciseModel,
    val setNum: Int = 4,
    val setXrepXweight: List<SetXrepXweight>
)

data class SetXrepXweight(val exNum: String, val reps: Int = 8, val weight: Int = 10)