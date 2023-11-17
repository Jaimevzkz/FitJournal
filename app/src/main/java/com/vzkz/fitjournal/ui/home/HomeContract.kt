package com.vzkz.fitjournal.ui.home

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class HomeState(
    val loading: Boolean,
//    val counter: Int,
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = false,
//            counter = 0,
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class HomeIntent: Intent {
    data class Loading(val isLoading: Boolean): HomeIntent()
}