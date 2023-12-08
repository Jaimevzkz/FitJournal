package com.vzkz.fitjournal.ui.profile

import android.net.Uri
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel


data class ProfileState(
    val logout: Boolean,
    val user: UserModel?,
    val error: Error,
    val loading: Boolean,
    val start: Boolean
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            logout = false,
            user = null,
            error = Error(isError = false, errorMsg = null),
            loading = false,
            start = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ProfileIntent: Intent {
    data object Logout: ProfileIntent()
    data class SetUserFromPersistence(val user: UserModel?): ProfileIntent()
    data class Error(val errorMsg: String): ProfileIntent()
    data object Loading: ProfileIntent()
    data class setImg(val updatedUser: UserModel): ProfileIntent()
}