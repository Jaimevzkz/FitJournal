package com.vzkz.fitjournal.ui.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.SaveUserDataStoreUseCase
import com.vzkz.fitjournal.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase, private val saveUserDataStore: SaveUserDataStoreUseCase): BaseViewModel<SignUpState, SignUpIntent>(
    SignUpState.initial) {

    override fun reduce(state: SignUpState, intent: SignUpIntent): SignUpState { //This function reduces each intent with a when
        return when (intent) {
            is SignUpIntent.Loading -> state.copy(
                loading = intent.isLoading
            )

            is SignUpIntent.SignUp -> state.copy(
                error = Error(false, null),
                user = intent.user,
                loading = false,
                success = true
            )

            is SignUpIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                success = false
            )

            SignUpIntent.CloseError -> state.copy(
                error = Error(false, null),
                loading = false,
                success = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onSignUp(email: String, password: String, nickname: String, firstname: String, lastname: String) {
        dispatch(SignUpIntent.Loading(isLoading = true))
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { signUpUseCase(email, password, nickname, firstname, lastname) }
                if (result != null) {
                    withContext(Dispatchers.IO) { saveUserDataStore(result) }
                    dispatch(SignUpIntent.SignUp(result))
                } else {
                    Log.e("Jaime", "nickname already exists")
                    dispatch(SignUpIntent.Error(""))
                }
            } catch (e: Exception) {
                Log.e("Jaime", e.message.orEmpty())
                dispatch(SignUpIntent.Error(e.message.orEmpty()))
            }
        }

    }

    fun onCloseDialog() = dispatch(SignUpIntent.CloseError)

}