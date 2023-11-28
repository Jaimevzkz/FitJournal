package com.vzkz.fitjournal.ui.workout.exlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import com.vzkz.fitjournal.ui.profile.Error
import com.vzkz.fitjournal.ui.workout.WorkoutIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExlistViewModel @Inject constructor(
    private val getUserPersistenceUseCase: GetUserPersistenceUseCase
) : BaseViewModel<ExlistState, ExlistIntent>(ExlistState.initial) {

    override fun reduce(
        state: ExlistState,
        intent: ExlistIntent
    ): ExlistState { //This function reduces each intent with a when
        return when(intent){
            is ExlistIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                start = false
            )
            ExlistIntent.Loading -> state.copy(
                error = Error(false, null),
                loading = true,
                start = false
            )
            is ExlistIntent.SetUserFromPersistence -> state.copy(
                user = intent.user,
                error = Error(false, null),
                loading = false,
                start = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInitWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUserPersistenceUseCase()
                if (user.uid == "") dispatch(ExlistIntent.Error("Couldn't find user in DataStore/room"))
                else dispatch(ExlistIntent.SetUserFromPersistence(user))
            } catch (e: Exception) {
                Log.e("Jaime", "Error when calling persistence from onInitWorkouts, workoutScreen, ${e.message}")
            }
        }
    }


}