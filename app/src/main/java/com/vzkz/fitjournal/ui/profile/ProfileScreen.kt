package com.vzkz.fitjournal.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.EditProfileScreenDestination
import com.vzkz.fitjournal.destinations.LoginScreenDestination
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.destinations.SettingsScreenDestination
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    if(profileViewModel.state.logout){
        navigator.navigate(LoginScreenDestination) {
            popUpTo(LoginScreenDestination) {
                inclusive = true
            }
        }
    }
    else{
        profileViewModel.onInitProfile()
        ScreenBody(
            profileViewModel,
            onBottomBarClicked = { navigator.navigate(it) },
            onSettingsClicked = { navigator.navigate(SettingsScreenDestination) },
            onEditProfileClicked = { navigator.navigate(EditProfileScreenDestination) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    profileViewModel: ProfileViewModel,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onSettingsClicked: () -> Unit,
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

    nickname = profileViewModel.state.user?.nickname ?: defaultVal
    firstname = profileViewModel.state.user?.firstname ?: defaultVal
    lastname = profileViewModel.state.user?.lastname ?: defaultVal
    email = profileViewModel.state.user?.email ?: defaultVal
    weight = profileViewModel.state.user?.weight ?: -1
    age = profileViewModel.state.user?.age ?: -1
    gender = profileViewModel.state.user?.gender ?: defaultVal
    goal = profileViewModel.state.user?.goal ?: defaultVal


    Scaffold(bottomBar = {
        MyBottomBar(
            currentDestination = ProfileScreenDestination,
            onClick = { onBottomBarClicked(it) })
    }) { paddingValues ->
        if(!profileViewModel.state.start){
            MyCircularProgressbar()
        } else{
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                //Top screen
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
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
                        .align(Alignment.TopCenter)
                        .padding(top = 72.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(shape = CircleShape),
                            painter = painterResource(id = R.drawable.defaultprofile),
                            contentDescription = "Profile image"
                        )
                        MySpacer(size = 16)
                        Column {
                            Text(
                                text = firstname,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = lastname,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                    MySpacer(size = 16)
                    Column(modifier = Modifier) {
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Text(
                                text = stringResource(R.string.nickname) +":",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = nickname, style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Text(
                                text = stringResource(R.string.email) +":",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = email, style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Text(
                                text = stringResource(R.string.weight) +":",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (weight == -1) "- Kg" else "$weight Kg",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Text(
                                text = stringResource(R.string.age) +":",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (age == -1) "- " else "$age",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Text(
                                text = stringResource(R.string.gender) +":",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = gender, style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Text(
                                text = stringResource(R.string.goal) +":",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = goal, style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.weight(3f))

                    }

                }
                //Bottom screen
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { profileViewModel.onLogout() }) {
                        Text(text = stringResource(R.string.logout))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LightPreview() {
    FitJournalTheme {
        ScreenBody(
            onBottomBarClicked = {},
            onSettingsClicked = {},
            onEditProfileClicked = {},
            profileViewModel = hiltViewModel()
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}