package com.vzkz.fitjournal.ui.workout

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.profile.ProfileIntent


data class WorkoutState(
    val user: UserModel?,
    val error: Error,
    val loading: Boolean,
    val start: Boolean
) : State {
    companion object {
        val initial: WorkoutState = WorkoutState(
            user = null,
            error = Error(isError = false, errorMsg = null),
            loading = false,
            start = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class WorkoutIntent: Intent {
    data class SetUserFromPersistence(val user: UserModel?): WorkoutIntent()
    data class Error(val errorMsg: String): WorkoutIntent()
    data object Loading: WorkoutIntent()
    data class DeleteWorkout(val updatedUser: UserModel?): WorkoutIntent()
}