package com.vzkz.fitjournal.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import com.vzkz.fitjournal.domain.usecases.LoginUseCase
import com.vzkz.fitjournal.domain.usecases.datapersistence.SaveUserPersistenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase, private val saveUserPersistence: SaveUserPersistenceUseCase) :
    BaseViewModel<LoginState, LoginIntent>(LoginState.initial) {

    override fun reduce(
        state: LoginState,
        intent: LoginIntent
    ): LoginState { //This function reduces each intent with a when
        return when (intent) {
            is LoginIntent.Loading -> state.copy(
                loading = intent.isLoading
            )

            is LoginIntent.Login -> state.copy(
                error = Error(false, null),
                user = intent.user,
                loading = false,
                success = true
            )

            is LoginIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                success = false
            )

            LoginIntent.CloseError -> state.copy(
                error = Error(false, null),
                loading = false,
                success = false
            )

        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onLogin(email: String, password: String) {
        dispatch(LoginIntent.Loading(isLoading = true))
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { loginUseCase(email, password) }
                if (result != null) {
                    withContext(Dispatchers.IO) { saveUserPersistence(result) }
                    dispatch(LoginIntent.Login(result))
                } else {
                    Log.e("Jaime", "The code should never get here (Exception controlled)")
                    dispatch(LoginIntent.Error(""))
                }
            } catch (e: Exception) {
                Log.e("Jaime", e.message.orEmpty())
                dispatch(LoginIntent.Error(e.message.orEmpty()))
            }
        }

    }

    fun onCloseDialog() = dispatch(LoginIntent.CloseError)


}