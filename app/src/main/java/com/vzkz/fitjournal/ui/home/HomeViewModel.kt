package com.vzkz.fitjournal.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.profile.ProfileIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserPersistenceUseCase: GetUserPersistenceUseCase,
) : BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(
        state: HomeState,
        intent: HomeIntent
    ): HomeState { //This function reduces each intent with a when
        return when(intent){
            is HomeIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                start = false
            )
            is HomeIntent.Loading -> state.copy(
                error = Error(false, null),
                loading = true,
                start = false
            )
            is HomeIntent.SetUserFromPersistence -> state.copy(
                user = intent.user,
                error = Error(false, null),
                loading = false,
                start = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI

    fun onInitProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUserPersistenceUseCase()
                if (user.uid == "") dispatch(HomeIntent.Error("Couldn't find user in DataStore/room"))
                else dispatch(HomeIntent.SetUserFromPersistence(user))
            } catch (e: Exception) {
                Log.e("Jaime", "Error when calling persistence from Home, ${e.message}")
            }
        }
    }


}