package com.vzkz.fitjournal.ui.workout.exlist

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class tState(
    val loading: Boolean,
//    val counter: Int,
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
//            counter = 0,
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class tIntent: Intent {
    data class Loading(val isLoading: Boolean): tIntent()
}