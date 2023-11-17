package com.vzkz.fitjournal.ui.profile.editprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.usecases.GetUserDataStoreUseCase
import com.vzkz.fitjournal.domain.usecases.ModifyUserDataUseCase
import com.vzkz.fitjournal.domain.usecases.SaveUserDataStoreUseCase
import com.vzkz.fitjournal.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserDataStoreUseCase: GetUserDataStoreUseCase,
    private val modifyUserDataUseCase: ModifyUserDataUseCase,
    private val saveUserDataStoreUseCase: SaveUserDataStoreUseCase
) :
    BaseViewModel<EditProfileState, EditProfileIntent>(EditProfileState.initial) {

    override fun reduce(
        state: EditProfileState,
        intent: EditProfileIntent
    ): EditProfileState { //This function reduces each intent with a when
        return when (intent) {
            is EditProfileIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                success = false,
                loading = false
            )

            is EditProfileIntent.SetUserFromDS -> state.copy(
                user = intent.user,
                success = false,
                loading = false
            )

            EditProfileIntent.CloseError -> state.copy(
                error = Error(false, null),
                success = false,
                loading = false
            )

            EditProfileIntent.Success -> state.copy(
                error = Error(false, null),
                success = true,
                loading = false
            )

            is EditProfileIntent.Loading -> state.copy(
                success = false,
                loading = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        dispatch(EditProfileIntent.Loading)
        viewModelScope.launch {
            getUserDataStoreUseCase().collect { user ->
                if (user.uid == "") dispatch(EditProfileIntent.Error("Couldn't find user in DataStore"))
                else dispatch(EditProfileIntent.SetUserFromDS(user))
            }
        }
    }

    fun onModifyUserData(newUser: UserModel, oldUser: UserModel) {
        dispatch(EditProfileIntent.Loading)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    modifyUserDataUseCase(newUser = newUser, oldUser = oldUser)
                    saveUserDataStoreUseCase(newUser)
                }
                dispatch(EditProfileIntent.Success)

            } catch (e: Exception) {
                dispatch(EditProfileIntent.Error("${e.message}"))
            }
        }

    }

    fun onCloseDialog() = dispatch(EditProfileIntent.CloseError)


}