package com.vzkz.fitjournal.ui.workout.searchexercise

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.GetExerciseByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SearchExerciseViewModel @Inject constructor(private val getExerciseByNameUseCase: GetExerciseByNameUseCase) :
    BaseViewModel<SearchExerciseState, SearchExerciseIntent>(SearchExerciseState.initial) {

    override fun reduce(
        state: SearchExerciseState,
        intent: SearchExerciseIntent
    ): SearchExerciseState { //This function reduces each intent with a when
        return when (intent) {
            is SearchExerciseIntent.Error -> state.copy(
                error = Error(isError = true, errorMsg = intent.errorMsg),
                loading = false,
                exerciseList = null,
                noResults = false
            )

            is SearchExerciseIntent.Loading -> state.copy(
                loading = true,
                exerciseList = null,
                noResults = false
            )

            is SearchExerciseIntent.SubmitSearch -> state.copy(
                loading = false,
                error = Error(isError = false, errorMsg = null),
                exerciseList = intent.exList,
                noResults = false
            )

            SearchExerciseIntent.NoResults -> state.copy(
                loading = false,
                error = Error(isError = false, errorMsg = null),
                noResults = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI

    fun onSearchByName(name: String) {
        dispatch(SearchExerciseIntent.Loading)
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { getExerciseByNameUseCase(name) }
                if (result != null) {
                    if(result.isEmpty()) dispatch(SearchExerciseIntent.NoResults)
                    else dispatch(SearchExerciseIntent.SubmitSearch(result))
                } else {
                    Log.e("Jaime", "The code should never get here (Exception controlled)")
                    dispatch(SearchExerciseIntent.Error(""))
                }
            } catch (e: Exception) {
                Log.e("Jaime", e.message.orEmpty())
                dispatch(SearchExerciseIntent.Error(e.message.orEmpty()))
            }
        }

    }


}