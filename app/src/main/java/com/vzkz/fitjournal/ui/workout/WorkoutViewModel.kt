package com.vzkz.fitjournal.ui.workout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.DeleteWorkoutUseCase
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import com.vzkz.fitjournal.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getUserPersistenceUseCase: GetUserPersistenceUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase
) : BaseViewModel<WorkoutState, WorkoutIntent>(WorkoutState.initial) {

    override fun reduce(
        state: WorkoutState,
        intent: WorkoutIntent
    ): WorkoutState { //This function reduces each intent with a when
        return when(intent){
            is WorkoutIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                start = false
            )
            WorkoutIntent.Loading -> state.copy(
                error = Error(false, null),
                loading = true,
                start = false
            )

            is WorkoutIntent.SetUserFromPersistence -> state.copy(
                user = intent.user,
                error = Error(false, null),
                loading = false,
                start = true
            )

            is WorkoutIntent.DeleteWorkout -> state.copy(
                user = intent.updatedUser,
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
                if (user.uid == "") dispatch(WorkoutIntent.Error("Couldn't find user in DataStore/room"))
                else dispatch(WorkoutIntent.SetUserFromPersistence(user))
            } catch (e: Exception) {
                Log.e(
                    "Jaime",
                    "Error when calling persistence from onInitWorkouts, workoutScreen, ${e.message}"
                )
            }
        }
    }

    fun onDeleteWorkout(wid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedUser = deleteWorkoutUseCase(wid)
            dispatch(WorkoutIntent.DeleteWorkout(updatedUser))
        }
    }


}