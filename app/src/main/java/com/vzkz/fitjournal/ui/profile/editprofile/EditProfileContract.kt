package com.vzkz.fitjournal.ui.profile.editprofile

import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.profile.Error


data class EditProfileState(
    val user: UserModel?,
    val error: Error,
    val success: Boolean,
    val loading: Boolean,
    val start: Boolean
) : State {
    companion object {
        val initial: EditProfileState = EditProfileState(
            user = null,
            error = Error(false, null),
            success = false,
            loading = false,
            start = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class EditProfileIntent: Intent {
    data class SetUserFromPersistence(val user: UserModel): EditProfileIntent()
    data class Error(val errorMsg: String): EditProfileIntent()
    data object CloseError: EditProfileIntent()
    data object  Success: EditProfileIntent()
    data object Loading: EditProfileIntent()
    data class SetImg(val updatedUser: UserModel): EditProfileIntent()
}