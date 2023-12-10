package com.vzkz.fitjournal.ui.profile.editprofile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.components.MyAlertDialog
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyConfirmDialog
import com.vzkz.fitjournal.ui.components.MyGenericTextField
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.profile.UploadPhotoDialog
import com.vzkz.fitjournal.ui.profile.cameraIntent
import com.vzkz.fitjournal.ui.profile.galleryIntent
import com.vzkz.fitjournal.ui.profile.generateUri
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
            var user: UserModel? by remember { mutableStateOf(null) }
            user = editProfileViewModel.state.user
            val isError = editProfileViewModel.state.error.isError
            val errorMsg = editProfileViewModel.state.error.errorMsg
            var profilePhoto: Uri? by remember { mutableStateOf(null) }
            profilePhoto = user?.profilePhoto
            ScreenBody(
                user = user,
                isError = isError,
                errorMsg = errorMsg,
                profilePhoto = profilePhoto,
                onUploadImage = {uriToUpload ->
                    editProfileViewModel.onUploadPhoto(
                        uriToUpload,
                        editProfileViewModel.state.user ?: UserModel()
                    )
                },
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
    profilePhoto: Uri?,
    onUploadImage: (Uri) -> Unit,
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

        //Camera
        val context = LocalContext.current
        var showPhotoDialog by remember { mutableStateOf(false) }
        val userTitle: String by remember { mutableStateOf("") }
        var uri: Uri? by remember { mutableStateOf(null) }

        val intentCameraLauncher = cameraIntent(uri, onUploadImage)

        val intentGalleryLauncher = galleryIntent(onUploadImage)

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
                .padding(top = 48.dp, bottom = 68.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val spaceBetween = 8
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (profilePhoto != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(12.dp)
                            .clip(shape = CircleShape),
                        model = profilePhoto,
                        contentDescription = "Profile photo",
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = CircleShape)
                            .padding(12.dp),
                        painter = painterResource(id = R.drawable.defaultprofile),
                        contentDescription = "Profile photo",
                        contentScale = ContentScale.Crop
                    )
                }
                OutlinedButton(
                    onClick = { showPhotoDialog = true },
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(R.string.change_profile_photo))
                }
            }
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = nickname,
                hint = stringResource(id = R.string.nickname),
                readOnlyText = readOnlyNickname,
                onTextChanged = { nickname = it },
                onReadOnlyChanged = { readOnlyNickname = !readOnlyNickname }
            )
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = firstname,
                hint = stringResource(R.string.first_name),
                readOnlyText = readOnlyFirstname,
                onTextChanged = { firstname = it },
                onReadOnlyChanged = { readOnlyFirstname = !readOnlyFirstname }
            )
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = lastname,
                hint = stringResource(R.string.last_name),
                readOnlyText = readOnlyLastname,
                onTextChanged = { lastname = it },
                onReadOnlyChanged = { readOnlyLastname = !readOnlyLastname }
            )
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = if(age != -1) age.toString() else "",
                hint = stringResource(id = R.string.age),
                readOnlyText = readOnlyAge,
                onTextChanged = { age = if (it != "") it.toInt() else -1 },
                onReadOnlyChanged = { readOnlyAge = !readOnlyAge }
            )
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = if(weight != -1) weight.toString() else "",
                hint = stringResource(id = R.string.weight),
                readOnlyText = readOnlyWeight,
                onTextChanged = { weight = if (it != "") it.toInt() else -1 },
                onReadOnlyChanged = { readOnlyWeight = !readOnlyWeight }
            )
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = gender,
                hint = stringResource(id = R.string.gender),
                readOnlyText = readOnlyGender,
                onTextChanged = { gender = it },
                onReadOnlyChanged = { readOnlyGender = !readOnlyGender }
            )
            MySpacer(size = spaceBetween)
            MyPrivateGenericTextfield(
                text = goal,
                hint = stringResource(id = R.string.goal),
                readOnlyText = readOnlyGoal,
                onTextChanged = { goal = it },
                onReadOnlyChanged = { readOnlyGoal = !readOnlyGoal }
            )
        }
        //Bottom screen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
            ,
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

        if (showPhotoDialog) {
            UploadPhotoDialog(
                onDismiss = {
                    showPhotoDialog = false
                },
                onCameraClicked = {
                    uri = generateUri(userTitle, context)
                    intentCameraLauncher.launch(uri)
                    showPhotoDialog = false
                },
                onGalleryClicked = {
                    intentGalleryLauncher.launch("image/*")
                    showPhotoDialog = false
                })
        }
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
                weight = 76,
                age = 21,
                gender = "male",
                goal = "Bulking",
                workouts = null
            ),
            isError = false,
            errorMsg = null,
            profilePhoto = null,
            onModifyUserData = { _, _ -> },
            onCloseDialog = {},
            onBackClicked = {},
            onUploadImage = {}
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}