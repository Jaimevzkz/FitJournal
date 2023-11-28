package com.vzkz.fitjournal.ui.workout.exlist

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.workout.WorkoutIntent


data class ExlistState(
    val user: UserModel?,
    val error: Error,
    val loading: Boolean,
    val start: Boolean
) : State {
    companion object {
        val initial: ExlistState = ExlistState(
            user = null,
            error = Error(isError = false, errorMsg = null),
            loading = false,
            start = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ExlistIntent: Intent {
    data class SetUserFromPersistence(val user: UserModel?): ExlistIntent()
    data class Error(val errorMsg: String): ExlistIntent()
    data object Loading: ExlistIntent()
}