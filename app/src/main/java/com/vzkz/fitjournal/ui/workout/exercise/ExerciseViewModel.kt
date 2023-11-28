package com.vzkz.fitjournal.ui.workout.exercise

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import com.vzkz.fitjournal.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val getUserPersistenceUseCase: GetUserPersistenceUseCase
): BaseViewModel<ExerciseState, ExerciseIntent>(ExerciseState.initial) {

    override fun reduce(state: ExerciseState, intent: ExerciseIntent): ExerciseState { //This function reduces each intent with a when
        return when(intent){
            is ExerciseIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                start = false
            )
            ExerciseIntent.Loading -> state.copy(
                error = Error(false, null),
                loading = true,
                start = false
            )
            is ExerciseIntent.SetUserFromPersistence -> state.copy(
                user = intent.user,
                error = Error(false, null),
                loading = false,
                start = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInitExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUserPersistenceUseCase()
                if (user.uid == "") dispatch(ExerciseIntent.Error("Couldn't find user in DataStore/room"))
                else dispatch(ExerciseIntent.SetUserFromPersistence(user))
            } catch (e: Exception) {
                Log.e("Jaime", "Error when calling persistence from onInitWorkouts, workoutScreen, ${e.message}")
            }
        }
    }


}