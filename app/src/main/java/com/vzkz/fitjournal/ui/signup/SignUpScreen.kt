package com.vzkz.fitjournal.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.destinations.LoginScreenDestination
import com.vzkz.fitjournal.ui.components.MyAlertDialog
import com.vzkz.fitjournal.ui.components.MyAuthHeader
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyEmailTextField
import com.vzkz.fitjournal.ui.components.MyGenericTextField
import com.vzkz.fitjournal.ui.components.MyImageLogo
import com.vzkz.fitjournal.ui.components.MyPasswordTextField
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.validateEmail
import com.vzkz.fitjournal.ui.components.validatePassword

@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val state = signUpViewModel.state
    if (state.success) {
        navigator.navigate(HomeScreenDestination)
    } else if (state.loading) {
        MyCircularProgressbar(backGroundColor = MaterialTheme.colorScheme.background)
    } else {
        ScreenBody(onSignInClicked = {
            navigator.navigate(LoginScreenDestination)
        }, signUpViewModel, state)
    }
}

@Composable
private fun ScreenBody(
    onSignInClicked: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    state: SignUpState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        var email by remember { mutableStateOf("jaimevzkz1+2@gmail.com") }
        var password by remember { mutableStateOf("1234Qwerty") }
        var repeatPassword by remember { mutableStateOf("1234Qwerty") }
        var nickname by remember { mutableStateOf("jaimee") }
        //validation
        var isEmailValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isSamePassword by remember { mutableStateOf(true) }
        var showDialog by remember { mutableStateOf(false) }
        showDialog = state.error.isError

        MyAuthHeader(Modifier.align(Alignment.TopEnd))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-70).dp)
        ) {
            MyImageLogo()
            MySpacer(16)
            MyEmailTextField(modifier = Modifier, text = email, onTextChanged = {
                isEmailValid = validateEmail(it)
                email = it
            })
            if (!isEmailValid) {
                Text(
                    text = stringResource(R.string.email_must_have_a_valid_format),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyPasswordTextField(
                modifier = Modifier,
                text = password,
                hint = stringResource(R.string.password),
                onTextChanged = {
                    isPasswordValid = validatePassword(it)
                    password = it
                })
            if (!isPasswordValid) {
                Text(
                    text = stringResource(R.string.invalid_password),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyPasswordTextField(
                modifier = Modifier,
                text = repeatPassword,
                hint = stringResource(R.string.repeat_password),
                onTextChanged = {
                    repeatPassword = it
                    isSamePassword = repeatPassword == password
                })
            if (!isSamePassword) {
                Text(
                    text = "Passwords must coincide.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyGenericTextField(
                modifier = Modifier,
                text = nickname,
                hint = stringResource(R.string.user_name),
                onTextChanged = { nickname = it })
            MySpacer(16)
            Text(
                text = stringResource(R.string.login),
                Modifier
                    .clickable { onSignInClicked() },
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = {
                if (isEmailValid && isPasswordValid && isSamePassword) {
                    signUpViewModel.onSignUp(email, password, nickname)
                }
            },
            Modifier
                .fillMaxWidth()
                .padding(48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.signup))
        }

        MyAlertDialog(
            title = stringResource(R.string.error_during_sign_up),
            text = state.error.errorMsg ?: stringResource(R.string.account_already_exists),
            onDismiss = { signUpViewModel.onCloseDialog() },
            onConfirm = { signUpViewModel.onCloseDialog() },
            showDialog = showDialog
        )
    }
}

@Preview
@Composable
fun SignUpPreview() {
    ScreenBody(onSignInClicked = {}, state = SignUpState.initial)
}