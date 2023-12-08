package com.vzkz.fitjournal.ui.home

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.profile.ProfileIntent


data class HomeState(
    val user: UserModel?,
    val error: Error,
    val loading: Boolean,
    val start: Boolean,
    val workout: WorkoutModel
) : State {
    companion object {
        val initial: HomeState = HomeState(
            user = null,
            error = Error(isError = false, errorMsg = null),
            loading = false,
            start = false,
            workout = WorkoutModel()
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class HomeIntent: Intent {
    data class SetUserFromPersistence(val user: UserModel?): HomeIntent()
    data class Error(val errorMsg: String): HomeIntent()
    data object Loading: HomeIntent()
    data class UpdateWotSelected(val workout: WorkoutModel): HomeIntent()
}