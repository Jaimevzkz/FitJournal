package com.vzkz.fitjournal.ui.login

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel


data class LoginState(
    val loading: Boolean,
    val user: UserModel?,
    val success: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            user = null,
            success = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class LoginIntent: Intent {
    data class Loading(val isLoading: Boolean): LoginIntent()
    data class Login(val user: UserModel): LoginIntent()
    data class Error(val errorMsg: String): LoginIntent()
    data object CloseError: LoginIntent()
}