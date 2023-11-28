package com.vzkz.fitjournal.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.LogoutUseCase
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserPersistenceUseCase: GetUserPersistenceUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<ProfileState, ProfileIntent>(
    ProfileState.initial
) {
    override fun reduce(
        state: ProfileState,
        intent: ProfileIntent
    ): ProfileState { //This function reduces each intent with a when
        return when (intent) {
            ProfileIntent.Logout -> state.copy(logout = true, loading = false)
            is ProfileIntent.Error -> state.copy(
                logout = false,
                error = Error(true, intent.errorMsg),
                loading = false,
                start = false
            )

            is ProfileIntent.SetUserFromPersistence -> state.copy(
                logout = false,
                user = intent.user,
                error = Error(false, null),
                loading = false,
                start = true
            )

            ProfileIntent.Loading -> state.copy(
                logout = false,
                error = Error(false, null),
                loading = true,
                start = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInitProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val user = getUserPersistenceUseCase()
                if (user.uid == "") dispatch(ProfileIntent.Error("Couldn't find user in DataStore/room"))
                else dispatch(ProfileIntent.SetUserFromPersistence(user))
            } catch(e: Exception){
                Log.e("Jaime", "Error when calling persistence from initPorfile, ${e.message}")
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