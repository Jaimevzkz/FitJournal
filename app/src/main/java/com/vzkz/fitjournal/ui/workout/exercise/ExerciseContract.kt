package com.vzkz.fitjournal.ui.workout.exercise

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.workout.exlist.ExlistIntent
import com.vzkz.fitjournal.ui.workout.exlist.ExlistState


data class ExerciseState(
    val user: UserModel?,
    val error: Error,
    val loading: Boolean,
    val start: Boolean
) : State {
    companion object {
        val initial: ExerciseState = ExerciseState(
            user = null,
            error = Error(isError = false, errorMsg = null),
            loading = false,
            start = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ExerciseIntent: Intent {
    data class SetUserFromPersistence(val user: UserModel?): ExerciseIntent()
    data class Error(val errorMsg: String): ExerciseIntent()
    data object Loading: ExerciseIntent()
}