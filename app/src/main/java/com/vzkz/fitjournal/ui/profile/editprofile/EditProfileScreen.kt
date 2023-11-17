package com.vzkz.fitjournal.ui.profile.editprofile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.ui.components.MyAlertDialog
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyConfirmDialog
import com.vzkz.fitjournal.ui.components.MyGenericTextField

@Destination
@Composable
fun EditProfileScreen(
    navigator: DestinationsNavigator,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {
    editProfileViewModel.onInit()
    val state = editProfileViewModel.state
    if (state.success) {
        navigator.navigate(ProfileScreenDestination)
    } else if (state.loading) {
        MyCircularProgressbar()
    } else {
        ScreenBody(editProfileViewModel){
            navigator.navigate(ProfileScreenDestination)
        }
    }
}

@Composable
private fun ScreenBody(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        var nickname by remember { mutableStateOf("") }
        var readOnlyNickname by remember { mutableStateOf(true) }
        var firstTime by remember { mutableStateOf(true) }
        var showAlertDialog by remember { mutableStateOf(false) }
        showAlertDialog = editProfileViewModel.state.error.isError
        var showConfirmDialog by remember { mutableStateOf(false) }

        if (firstTime) {
            nickname = editProfileViewModel.state.user?.nickname ?: ""
            firstTime = false
        }
        IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = {
            onBackClicked()
        }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Cancel")
        }

        Column(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 48.dp)) {
            MyGenericTextField(
                modifier = Modifier,
                hint = stringResource(R.string.nickname),
                text = nickname,
                onTextChanged = { nickname = it },
                readOnly = readOnlyNickname,
                trailingIcon = {
                    IconButton(onClick = { readOnlyNickname = !readOnlyNickname }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Icon")
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                showConfirmDialog = true
            }) {
                Text(text = stringResource(R.string.save))
            }
        }

        MyConfirmDialog(
            title = "Are you sure?",
            text = "Profile changes like username may not be undoable",
            onDismiss = {
                readOnlyNickname = true
                showConfirmDialog = false
            },
            onConfirm = {
                readOnlyNickname = true
                showConfirmDialog = false
                if (editProfileViewModel.state.user != null) {
                    val newUser = editProfileViewModel.state.user!!.copy(nickname = nickname)
                    editProfileViewModel.onModifyUserData(
                        newUser = newUser,
                        oldUser = editProfileViewModel.state.user!!
                    ) //We know the user is not null
                }
            },
            showDialog = showConfirmDialog
        )

        MyAlertDialog( //Error Dialog
            title = stringResource(R.string.error_during_profile_modification),
            text = editProfileViewModel.state.error.errorMsg
                ?: stringResource(R.string.username_already_in_use),
            onDismiss = {
                editProfileViewModel.onCloseDialog()
            },
            onConfirm = {
                editProfileViewModel.onCloseDialog()
            },
            showDialog = showAlertDialog
        )
    }
}

@Preview
@Composable
fun LightPreview() {
    ScreenBody(){}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {

}