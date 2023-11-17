package com.vzkz.fitjournal.ui.settings

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State


data class SettingsState(
    val darkTheme: Boolean,
) : State {
    companion object {
        val initial: SettingsState = SettingsState(
            darkTheme = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SettingsIntent: Intent {
    data object Theme: SettingsIntent()
    data class Init(val theme: Boolean): SettingsIntent()
}