package com.vzkz.fitjournal.ui.profile.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.components.MyAlertDialog
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyConfirmDialog
import com.vzkz.fitjournal.ui.components.MyGenericTextField
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@Destination
@Composable
fun EditProfileScreen(
    navigator: DestinationsNavigator,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {
    editProfileViewModel.onInit()
    val state = editProfileViewModel.state
    if(state.start){
        if (state.success) {
            navigator.navigate(ProfileScreenDestination)
        } else if (state.loading) {
            MyCircularProgressbar()
        } else {
            val user = editProfileViewModel.state.user
            val isError = editProfileViewModel.state.error.isError
            val errorMsg = editProfileViewModel.state.error.errorMsg
            ScreenBody(
                user = user,
                isError = isError,
                errorMsg = errorMsg,
                onModifyUserData = { newUser, oldUser ->
                    editProfileViewModel.onModifyUserData(newUser = newUser, oldUser = oldUser)
                },
                onCloseDialog = { editProfileViewModel.onCloseDialog() },
                onBackClicked = { navigator.navigate(ProfileScreenDestination) }
            )
        }
    }
    else {
        MyCircularProgressbar()
    }
}

@Composable
private fun ScreenBody(
    user: UserModel?,
    isError: Boolean,
    errorMsg: String?,
    onModifyUserData: (UserModel, UserModel) -> Unit,
    onCloseDialog: () -> Unit,
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
        var firstname by remember { mutableStateOf("") }
        var readOnlyFirstname by remember { mutableStateOf(true) }
        var lastname by remember { mutableStateOf("") }
        var readOnlyLastname by remember { mutableStateOf(true) }
        var age by remember { mutableIntStateOf(-1) }
        var readOnlyAge by remember { mutableStateOf(true) }
        var gender by remember { mutableStateOf("") }
        var readOnlyGender by remember { mutableStateOf(true) }
        var goal by remember { mutableStateOf("") }
        var readOnlyGoal by remember { mutableStateOf(true) }
        var weight by remember { mutableIntStateOf(-1) }
        var readOnlyWeight by remember { mutableStateOf(true) }

        var firstTime by remember { mutableStateOf(true) }
        if (firstTime) {
            nickname = user?.nickname ?: ""
            firstname = user?.firstname ?: ""
            lastname = user?.lastname ?: ""
            age = user?.age ?: -1
            gender = user?.gender ?: ""
            goal = user?.goal ?: ""
            weight = user?.weight ?: -1
            firstTime = false
        }
        var showAlertDialog by remember { mutableStateOf(false) }
        showAlertDialog = isError
        var showConfirmDialog by remember { mutableStateOf(false) }

        //Top screen
        IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = {
            firstTime = true
            onBackClicked()
        }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Cancel")
        }

        //Main screen
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        ) {
            MyPrivateGenericTextfield(
                text = nickname,
                hint = stringResource(id = R.string.nickname),
                readOnlyText = readOnlyNickname,
                onTextChanged = { nickname = it },
                onReadOnlyChanged = { readOnlyNickname = !readOnlyNickname }
            )
            Spacer(modifier = Modifier.weight(1f))
            MyPrivateGenericTextfield(
                text = firstname,
                hint = stringResource(R.string.first_name),
                readOnlyText = readOnlyFirstname,
                onTextChanged = { firstname = it },
                onReadOnlyChanged = { readOnlyFirstname = !readOnlyFirstname }
            )
            Spacer(modifier = Modifier.weight(1f))
            MyPrivateGenericTextfield(
                text = lastname,
                hint = stringResource(R.string.last_name),
                readOnlyText = readOnlyLastname,
                onTextChanged = { lastname = it },
                onReadOnlyChanged = { readOnlyLastname = !readOnlyLastname }
            )
            Spacer(modifier = Modifier.weight(1f))
            MyPrivateGenericTextfield(
                text = age.toString(),
                hint = stringResource(id = R.string.age),
                readOnlyText = readOnlyAge,
                onTextChanged = { age = if(it != "") it.toInt() else -1 },
                onReadOnlyChanged = { readOnlyAge = !readOnlyAge }
            )
            Spacer(modifier = Modifier.weight(1f))
            MyPrivateGenericTextfield(
                text = weight.toString(),
                hint = stringResource(id = R.string.weight),
                readOnlyText = readOnlyWeight,
                onTextChanged = { weight = if(it != "") it.toInt() else -1 },
                onReadOnlyChanged = { readOnlyWeight = !readOnlyWeight }
            )
            Spacer(modifier = Modifier.weight(1f))
            MyPrivateGenericTextfield(
                text = gender,
                hint = stringResource(id = R.string.gender),
                readOnlyText = readOnlyGender,
                onTextChanged = { gender = it },
                onReadOnlyChanged = { readOnlyGender = !readOnlyGender }
            )
            Spacer(modifier = Modifier.weight(1f))
            MyPrivateGenericTextfield(
                text = goal,
                hint = stringResource(id = R.string.goal),
                readOnlyText = readOnlyGoal,
                onTextChanged = { goal = it },
                onReadOnlyChanged = { readOnlyGoal = !readOnlyGoal }
            )
            Spacer(modifier = Modifier.weight(3f))
        }

        //Bottom screen
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
            title = stringResource(R.string.are_you_sure),
            text = stringResource(R.string.profile_changes_like_username_may_not_be_undoable),
            onDismiss = {
                readOnlyNickname = true
                readOnlyFirstname = true
                readOnlyLastname = true
                readOnlyWeight = true
                readOnlyAge = true
                readOnlyGender = true
                readOnlyGoal = true

                showConfirmDialog = false
            },
            onConfirm = {
                readOnlyNickname = true
                readOnlyFirstname = true
                readOnlyLastname = true
                readOnlyWeight = true
                readOnlyAge = true
                readOnlyGender = true
                readOnlyGoal = true

                showConfirmDialog = false
                if (user != null &&
                    nickname != "" && firstname != "" &&
                    lastname != ""
                ) {
                    val newUser = user.copy(
                        nickname = nickname,
                        firstname = firstname,
                        lastname = lastname,
                        age = (if (age != -1) age else null),
                        weight = (if (weight != -1) weight else null),
                        gender = (if (gender != "") gender else null),
                        goal = (if (goal != "") goal else null)
                    )
                    onModifyUserData(newUser, user) //We know the user is not null
                }
            },
            showDialog = showConfirmDialog
        )

        MyAlertDialog( //Error Dialog
            title = stringResource(R.string.error_during_profile_modification),
            text = errorMsg ?: stringResource(R.string.username_already_in_use),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = showAlertDialog
        )
    }
}

@Composable
private fun MyPrivateGenericTextfield(
    text: String,
    hint: String,
    readOnlyText: Boolean,
    onTextChanged: (String) -> Unit,
    onReadOnlyChanged: () -> Unit
) {
    MyGenericTextField(modifier = Modifier,
        hint = hint,
        text = text,
        onTextChanged = { onTextChanged(it) },
        readOnly = readOnlyText,
        trailingIcon = {
            IconButton(onClick = { onReadOnlyChanged() }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Icon")
            }
        })
}

@Preview
@Composable
fun LightPreview() {
    FitJournalTheme {
        ScreenBody(
            user = UserModel(
                uid = "203fj4nfi4",
                nickname = "Jaime",
                email = "jaime@gmail.com",
                firstname = "Jaime",
                lastname = "Vázquez",
                weight = null,
                age = null,
                gender = null,
                goal = null,
                workouts = null
            ),
            isError = false,
            errorMsg = null,
            onModifyUserData = { s1, s2 -> },
            onCloseDialog = {}) {

        }
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}