package com.vzkz.fitjournal.ui.profile.editprofile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import com.vzkz.fitjournal.domain.usecases.ModifyUserDataUseCase
import com.vzkz.fitjournal.domain.usecases.datapersistence.SaveUserPersistenceUseCase
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.profile.ProfileIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserPersistenceUseCase: GetUserPersistenceUseCase,
    private val modifyUserDataUseCase: ModifyUserDataUseCase,
    private val saveUserPersistenceUseCase: SaveUserPersistenceUseCase
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
                loading = false,
                start = false
            )

            is EditProfileIntent.SetUserFromPersistence -> state.copy(
                user = intent.user,
                success = false,
                loading = false,
                start = true
            )

            EditProfileIntent.CloseError -> state.copy(
                error = Error(false, null),
                success = false,
                loading = false,
                start = false
            )

            EditProfileIntent.Success -> state.copy(
                error = Error(false, null),
                success = true,
                loading = false,
                start = false
            )

            is EditProfileIntent.Loading -> state.copy(
                success = false,
                loading = true,
                start = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                viewModelScope.launch(Dispatchers.IO) {
                    val user = getUserPersistenceUseCase()
                    if (user.uid == "") dispatch(EditProfileIntent.Error("Couldn't find user in DataStore/room"))
                    else dispatch(EditProfileIntent.SetUserFromPersistence(user))
                }
            } catch(e: Exception){
                Log.e("Jaime", "Error when calling persistence from initEditPorfile, ${e.message}")
            }
        }
    }

    fun onModifyUserData(newUser: UserModel, oldUser: UserModel) {
        dispatch(EditProfileIntent.Loading)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    modifyUserDataUseCase(newUser = newUser, oldUser = oldUser)
                    saveUserPersistenceUseCase(newUser)
                }
                dispatch(EditProfileIntent.Success)

            } catch (e: Exception) {
                dispatch(EditProfileIntent.Error("${e.message}"))
            }
        }

    }

    fun onCloseDialog() = dispatch(EditProfileIntent.CloseError)


}