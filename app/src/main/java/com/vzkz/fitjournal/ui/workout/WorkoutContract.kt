package com.vzkz.fitjournal.ui.workout

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class WorkoutState(
    val loading: Boolean,
//    val counter: Int,
) : State {
    companion object {
        val initial: WorkoutState = WorkoutState(
            loading = false,
//            counter = 0,
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class WorkoutIntent: Intent {
    data class Loading(val isLoading: Boolean): WorkoutIntent()
}