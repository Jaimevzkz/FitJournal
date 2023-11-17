package com.vzkz.fitjournal.core.boilerplate.template

import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(): BaseViewModel<tState, tIntent>(tState.initial) {

    override fun reduce(state: tState, intent: tIntent): tState { //This function reduces each intent with a when
        TODO("Not yet implemented")
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(tIntent.Loading(true))
    }


}