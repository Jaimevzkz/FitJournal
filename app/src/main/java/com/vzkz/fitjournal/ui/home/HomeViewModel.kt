package com.vzkz.fitjournal.ui.home

import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.ui.home.HomeIntent
import com.vzkz.fitjournal.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(state: HomeState, intent: HomeIntent): HomeState { //This function reduces each intent with a when
        TODO("Not yet implemented")
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(HomeIntent.Loading(true))
    }


}