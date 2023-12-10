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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Devices
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
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

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
        val isError = signUpViewModel.state.error.isError
        val errorMsg = signUpViewModel.state.error.errorMsg
        ScreenBody(
            onSignUp = { email, password, nickname, firstname, lastname ->
                signUpViewModel.onSignUp(email = email, password = password, nickname = nickname, firstname = firstname, lastname = lastname)
            },
            onCloseDialog = {signUpViewModel.onCloseDialog()},
            isError = isError,
            errorMsg = errorMsg,
            onSignInClicked = {
                navigator.navigate(LoginScreenDestination)
            })
    }
}

@Composable
private fun ScreenBody(
    onSignInClicked: () -> Unit,
    onSignUp: (String, String, String, String, String) -> Unit,
    onCloseDialog: () -> Unit,
    isError: Boolean,
    errorMsg: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        //validation
        var isEmailValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isSamePassword by remember { mutableStateOf(true) }
        var showDialog by remember { mutableStateOf(false) }
        var isNicknameValid by remember { mutableStateOf(false) }
        var isFirstNameValid by remember { mutableStateOf(false) }
        var isLastNameValid by remember { mutableStateOf(false) }
        showDialog = isError

        MyAuthHeader(Modifier.align(Alignment.TopEnd))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 72.dp)
                .verticalScroll(rememberScrollState())
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
                    text = stringResource(R.string.passwords_must_coincide),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyGenericTextField(
                modifier = Modifier,
                text = nickname,
                hint = stringResource(R.string.user_name),
                onTextChanged = {
                    nickname = it
                    isNicknameValid = (nickname != "")
                }
            )
            if (!isNicknameValid) {
                Text(
                    text = "Nickname shouldn't be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyGenericTextField(
                modifier = Modifier,
                text = firstname,
                hint = "First name",
                onTextChanged = {
                    firstname = it
                    isFirstNameValid = (firstname != "")
                }
            )
            if (!isFirstNameValid) {
                Text(
                    text = "First name shouldn't be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            MySpacer(size = 8)
            MyGenericTextField(
                modifier = Modifier,
                text = lastname,
                hint = "Last name",
                onTextChanged = {
                    lastname = it
                    isLastNameValid = lastname != ""
                }
            )
            if (!isLastNameValid) {
                Text(
                    text = "Last name shouldn't be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

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
                if (isEmailValid && isPasswordValid && isSamePassword && isNicknameValid && isFirstNameValid && isLastNameValid) {
                    onSignUp(email, password, nickname, firstname, lastname)
                }
            },
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .padding(horizontal = 48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.signup))
        }

        MyAlertDialog(
            title = stringResource(R.string.error_during_sign_up),
            text = errorMsg ?: stringResource(R.string.account_already_exists),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = showDialog
        )
    }
}

@Preview(device = Devices.NEXUS_5X)
@Preview()
@Composable
fun SignUpPreview() {
    FitJournalTheme {
        ScreenBody(
            onSignInClicked = {  },
            onSignUp = {_, _, _, _, _ ->},
            onCloseDialog = {  },
            isError = false,
            errorMsg = ""
        )
    }
}