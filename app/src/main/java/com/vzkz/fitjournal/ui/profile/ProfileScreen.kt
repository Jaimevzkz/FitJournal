package com.vzkz.fitjournal.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.core.boilerplate.USERMODELFORTESTS
import com.vzkz.fitjournal.destinations.EditProfileScreenDestination
import com.vzkz.fitjournal.destinations.LoginScreenDestination
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.destinations.SettingsScreenDestination
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar
import com.vzkz.fitjournal.ui.theme.FitJournalTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    if (profileViewModel.state.logout) {
        navigator.navigate(LoginScreenDestination) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }
    } else {
        profileViewModel.onInitProfile()
        var user: UserModel? by remember { mutableStateOf(null) }
        user = profileViewModel.state.user
        val start = profileViewModel.state.start
        var progressPhotosList: List<Uri> by remember { mutableStateOf(emptyList()) }
        progressPhotosList = user?.progressPhotos ?: emptyList()
        ScreenBody(
            user = user,
            start = start,
            progressPhotosList = progressPhotosList,
            onLogout = { profileViewModel.onLogout() },
            onBottomBarClicked = { navigator.navigate(it) },
            onSettingsClicked = { navigator.navigate(SettingsScreenDestination) },
            onEditProfileClicked = { navigator.navigate(EditProfileScreenDestination) },
            onUploadImage = {
                profileViewModel.onUploadPhoto(it, profileViewModel.state.user ?: UserModel())
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    user: UserModel?,
    start: Boolean,
    progressPhotosList: List<Uri>,
    onUploadImage: (Uri) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onSettingsClicked: () -> Unit,
    onLogout: () -> Unit,
    onEditProfileClicked: () -> Unit
) {
    val defaultVal = "- "
    var nickname by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableIntStateOf(-1) }
    var age by remember { mutableIntStateOf(-1) }
    var gender by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    nickname = user?.nickname ?: defaultVal
    firstname = user?.firstname ?: defaultVal
    lastname = user?.lastname ?: defaultVal
    email = user?.email ?: defaultVal
    weight = user?.weight ?: -1
    age = user?.age ?: -1
    gender = user?.gender ?: defaultVal
    goal = user?.goal ?: defaultVal

    //Camera
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val userTitle: String by remember { mutableStateOf("") }
    var uri: Uri? by remember { mutableStateOf(null) }

    val intentCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it && uri?.path?.isNotEmpty() == true) {
                onUploadImage(uri!!)
            }
        }

    val intentGalleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            if (it?.path?.isNotEmpty() == true) {
                onUploadImage(it)
            }
        }


    Scaffold(bottomBar = {
        MyBottomBar(
            currentDestination = ProfileScreenDestination,
            onClick = { if(it != ProfileScreenDestination) onBottomBarClicked(it) })
    }) { paddingValues ->
        if (!start) {
            MyCircularProgressbar()
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //Top screen
                    Row(
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onEditProfileClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "App Profile Button"
                            )
                        }
                        MySpacer(size = 8)
                        IconButton(
                            onClick = { onSettingsClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "App Settings Button"
                            )
                        }

                    }

                    //Main Screen
                    Column(
                        Modifier
                            .weight(9f)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(shape = CircleShape)
                                        .padding(12.dp),
                                    painter = painterResource(id = R.drawable.defaultprofile),
                                    contentDescription = "Profile image"
                                )
                                MySpacer(size = 16)
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = firstname,
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = lastname,
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(125.dp)
                            ) {
                                LazyRow {
                                    item {
                                        AddProgressPhotoCard(
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                            onAddPressed = {
                                                showDialog = true
                                            }
                                        )
                                    }
                                    if(progressPhotosList.isNotEmpty()){
                                        items(progressPhotosList) { progressPhoto ->
                                            ProgressImgCard(uri = progressPhoto)
                                        }
                                    }
                                }
                            }
                        }

                        MySpacer(size = 16)
                        Column(
                            modifier = Modifier
                                .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            val padding = 12.dp
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.padding(horizontal = padding)) {
                                Text(
                                    text = stringResource(R.string.nickname) + ":",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = nickname, style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.padding(horizontal = padding)) {
                                Text(
                                    text = stringResource(R.string.email) + ":",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = email,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.padding(horizontal = padding)) {
                                Text(
                                    text = stringResource(R.string.weight) + ":",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = if (weight == -1) "- Kg" else "$weight Kg",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.padding(horizontal = padding)) {
                                Text(
                                    text = stringResource(R.string.age) + ":",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = if (age == -1) "- " else "$age",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.padding(horizontal = padding)) {
                                Text(
                                    text = stringResource(R.string.gender) + ":",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = gender, style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.padding(horizontal = padding)) {
                                Text(
                                    text = stringResource(R.string.goal) + ":",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = goal, style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                    }
                    //Bottom screen
                    Column(
                        modifier = Modifier
                            .weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { onLogout() }) {
                            Text(text = stringResource(R.string.logout))
                        }
                    }
                }
                if (showDialog) {
                    UploadPhotoDialog(
                        onDismiss = {
                            showDialog = false
                        },
                        onCameraClicked = {
                            uri = generateUri(userTitle, context)
                            intentCameraLauncher.launch(uri)
                            showDialog = false
                        },
                        onGalleryClicked = {
                            intentGalleryLauncher.launch("image/*")
                            showDialog = false
                        })
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddProgressPhotoCard(
    contentColor: Color,
    backgroundColor: Color,
    onAddPressed: () -> Unit
) {
    Card(
        onClick = { onAddPressed() },
        modifier = Modifier
            .padding(12.dp)
            .padding(vertical = 4.dp)
            .border(3.dp, contentColor, shape = RoundedCornerShape(30))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = contentColor)
            Text(text = "Add", color = contentColor)
            Text(text = "progress photo", color = contentColor)
        }
    }
}

@Composable
private fun ProgressImgCard(uri: Uri) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .padding(vertical = 4.dp)
            .width(145.dp)
            .border(
                BorderStroke(width = 3.dp, color = Color.Transparent),
                shape = RoundedCornerShape(30)
            )
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "progress photo",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun UploadPhotoDialog(
    onDismiss: () -> Unit,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(shape = RoundedCornerShape(12)) {
            Column(modifier = Modifier.padding(24.dp)) {
                OutlinedButton(
                    onClick = {
                        onCameraClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(42)
                ) {
                    Text(stringResource(R.string.camera))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        onGalleryClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(42)
                ) {
                    Text(stringResource(R.string.from_gallery))
                }
            }
        }
    }
}

private fun generateUri(userTitle: String, context: Context): Uri {
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.vzkz.fitjournal.provider",
        createFile(userTitle, context)
    )
}

private fun createFile(userTitle: String, context: Context): File {
    val name: String =
        userTitle.ifEmpty { generateUniqueId() + "image" }
    return File.createTempFile(name, ".jpg", context.externalCacheDir)
}

private fun generateUniqueId(): String {
    return SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
}


@Preview(device = Devices.NEXUS_5X)
@Composable
fun LightPreview() {
    FitJournalTheme {
        ScreenBody(
            user = USERMODELFORTESTS,
            start = true,
            progressPhotosList = USERMODELFORTESTS.progressPhotos,
            onBottomBarClicked = {},
            onSettingsClicked = { },
            onLogout = { },
            onEditProfileClicked = {},
            onUploadImage = {}
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}