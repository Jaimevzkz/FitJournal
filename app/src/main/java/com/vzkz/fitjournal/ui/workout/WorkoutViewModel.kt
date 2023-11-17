package com.vzkz.fitjournal.ui.workout

import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(): BaseViewModel<WorkoutState, WorkoutIntent>(WorkoutState.initial) {

    override fun reduce(state: WorkoutState, intent: WorkoutIntent): WorkoutState { //This function reduces each intent with a when
        TODO("Not yet implemented")
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(WorkoutIntent.Loading(true))
    }


}