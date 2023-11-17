package com.vzkz.fitjournal.ui.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.EditProfileScreenDestination
import com.vzkz.fitjournal.destinations.LoginScreenDestination
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.destinations.SettingsScreenDestination
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    profileViewModel.onInitProfile()
    when (profileViewModel.state.logout) {
        true -> {
            navigator.navigate(LoginScreenDestination) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true
                }
            }
        }

        false -> ScreenBody(
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
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onSettingsClicked: () -> Unit,
    onEditProfileClicked: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }

    nickname = profileViewModel.state.user?.nickname ?: ""
    Scaffold(bottomBar = {
        MyBottomBar(
            currentDestination = ProfileScreenDestination,
            onClick = { onBottomBarClicked(it) })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
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
            Text(text = nickname, style = MaterialTheme.typography.titleLarge)
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

@Preview
@Composable
fun LightPreview() {
    ScreenBody(onBottomBarClicked = {}, onSettingsClicked = {}, onEditProfileClicked = {})
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {

}