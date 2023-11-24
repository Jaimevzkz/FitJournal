package com.vzkz.fitjournal.ui.workout.searchexercise

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.ExerciseModel


data class SearchExerciseState(
    val loading: Boolean,
    val exerciseList: List<ExerciseModel>?,
    val error: Error,
    val noResults: Boolean

) : State {
    companion object {
        val initial: SearchExerciseState = SearchExerciseState(
            loading = false,
            exerciseList = null,
            error = Error(false, null),
            noResults = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SearchExerciseIntent : Intent {
    data object Loading : SearchExerciseIntent()
    data class SubmitSearch(val exList: List<ExerciseModel>) : SearchExerciseIntent()
    data object NoResults: SearchExerciseIntent()
    data class Error(val errorMsg: String): SearchExerciseIntent()
}