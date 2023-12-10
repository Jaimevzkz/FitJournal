package com.vzkz.fitjournal.ui.login

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
import com.vzkz.fitjournal.destinations.SignUpScreenDestination
import com.vzkz.fitjournal.ui.components.MyAlertDialog
import com.vzkz.fitjournal.ui.components.MyAuthHeader
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyEmailTextField
import com.vzkz.fitjournal.ui.components.MyImageLogo
import com.vzkz.fitjournal.ui.components.MyPasswordTextField
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.validateEmail

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val state = loginViewModel.state
    if (state.success) {
        navigator.navigate(HomeScreenDestination)
    } else if (state.loading) {
        MyCircularProgressbar()
    } else {
        val isError = loginViewModel.state.error.isError
        val errorMsg = loginViewModel.state.error.errorMsg
        ScreenBody(
            onSignUpClicked = {
                navigator.navigate(SignUpScreenDestination)
            },
            onLogin = { email, password ->
                loginViewModel.onLogin(email, password)
            },
            onCloseDialog = { loginViewModel.onCloseDialog() },
            isError  = isError,
            errorMsg = errorMsg
        )
    }
}

@Composable
private fun ScreenBody(
    onSignUpClicked: () -> Unit,
    onLogin: (String, String) -> Unit,
    onCloseDialog: () -> Unit,
    isError : Boolean,
    errorMsg: String?
//    state: LoginState
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
        var isValid by remember { mutableStateOf(true) }
        var showDialog by remember { mutableStateOf(false) }
        showDialog = isError

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
                email = it
                isValid = validateEmail(email)
            })
            if (!isValid) {
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
                onTextChanged = { password = it })
            MySpacer(16)
            Text(
                text = stringResource(R.string.signup),
                Modifier
                    .clickable { onSignUpClicked() },
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = {
                if (isValid) {
                    onLogin(email, password)
                }
            },
            Modifier
                .fillMaxWidth()
                .padding(48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        MyAlertDialog(
            title = stringResource(R.string.error_during_login),
            text = errorMsg ?: stringResource(R.string.invalid_password),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = showDialog
        )
    }
}

@Preview
@Composable
fun LoginPreview() {
    ScreenBody(
        onSignUpClicked = {},
        onLogin = { _, _ -> },
        onCloseDialog = {},
        isError = false,
        errorMsg = null
    )
}