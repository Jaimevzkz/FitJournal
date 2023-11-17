package com.vzkz.fitjournal.ui.profile

import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.GetUserDataStoreUseCase
import com.vzkz.fitjournal.domain.usecases.LogoutUseCase
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.profile.ProfileIntent
import com.vzkz.fitjournal.ui.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(private val getUserDataStoreUseCase: GetUserDataStoreUseCase, private val logoutUseCase: LogoutUseCase): BaseViewModel<ProfileState, ProfileIntent>(
    ProfileState.initial) {
    override fun reduce(state: ProfileState, intent: ProfileIntent): ProfileState { //This function reduces each intent with a when
        return when(intent){
            ProfileIntent.Logout -> state.copy(logout = true)
            is ProfileIntent.Error -> state.copy(
                logout = false,
                error = Error(true, intent.errorMsg)
            )
            is ProfileIntent.SetUserFromDS -> state.copy(
                logout = false,
                user = intent.user,
                error = Error(false, null)
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInitProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                getUserDataStoreUseCase().collect{user ->
                    if(user.uid == "") dispatch(ProfileIntent.Error("Couldn't find user in DataStore"))
                    else dispatch(ProfileIntent.SetUserFromDS(user))
                }
            }
        }
    }
    fun onLogout(){
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
        }
        dispatch(ProfileIntent.Logout)
    }


}