package com.vzkz.fitjournal.ui.home

import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import com.vzkz.fitjournal.domain.usecases.datapersistence.GetUserPersistenceUseCase
import com.vzkz.fitjournal.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
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

            is HomeIntent.UpdateWotSelected -> state.copy(workout = intent.workout)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI

    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = getUserPersistenceUseCase()
                if (user.uid == "") dispatch(HomeIntent.Error("Couldn't find user in DataStore/room"))
                else{
                    dispatch(HomeIntent.SetUserFromPersistence(user = user))
                }
            } catch (e: Exception) {
                Log.e("Jaime", "Error when calling persistence from Home, ${e.message}")
            }
        }
    }

    fun onSelectedDate(selectedDate: LocalDate?, wotDates: List<Pair<LocalDate, String>>, wotList: List<WorkoutModel>?){
        val wid = workoutLogsForDay(selectedDate ?: LocalDate.now(), wotDates)
        val wot = wotForWid(wid, wotList)
        dispatch(HomeIntent.UpdateWotSelected(wot ?: WorkoutModel()))
    }

    private fun workoutLogsForDay(date: LocalDate, wotDates: List<Pair<LocalDate, String>>): String?{
        for (wotDate in wotDates)
            if (date == wotDate.first) return wotDate.second

        return null
    }

    private fun wotForWid(wid: String?, wotList: List<WorkoutModel>?): WorkoutModel?{
        if(wid != null && wotList != null){
            for (wot in wotList){
                if(wid == wot.wid)
                    return wot
            }
        }
        return null
    }


}