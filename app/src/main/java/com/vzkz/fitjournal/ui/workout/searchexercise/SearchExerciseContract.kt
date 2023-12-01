package com.vzkz.fitjournal.ui.workout.searchexercise

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.Exercises


data class SearchExerciseState(
    val loading: Boolean,
    val exerciseList: List<ExerciseModel>?,
    val newWorkoutExerciseList: MutableList<Exercises>,
    val error: Error,
    val noResults: Boolean,
    val exerciseModelToAdd: ExerciseModel?

) : State {
    companion object {
        val initial: SearchExerciseState = SearchExerciseState(
            loading = false,
            exerciseList = null,
            error = Error(false, null),
            noResults = false,
            newWorkoutExerciseList = mutableListOf(),
            exerciseModelToAdd = null
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SearchExerciseIntent : Intent {
    data object Loading : SearchExerciseIntent()
    data class SubmitSearch(val exList: List<ExerciseModel>) : SearchExerciseIntent()
    data object NoResults: SearchExerciseIntent()
    data class Error(val errorMsg: String): SearchExerciseIntent()
    data class AddExerciseToWorkout(val exercise: Exercises): SearchExerciseIntent()
    data class SetExerciseModelToAdd(val exerciseModelToAdd: ExerciseModel): SearchExerciseIntent()
    data object ClearAddedExercises: SearchExerciseIntent()
}