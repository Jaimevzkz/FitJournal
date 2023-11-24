package com.vzkz.fitjournal.ui.splash

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.destinations.LoginScreenDestination
import com.vzkz.fitjournal.ui.components.MyImageLogo
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    ScreenBody()
    when(splashViewModel.getDestination()){
        SplashDestinations.HomeDest -> navigator.navigate(HomeScreenDestination)
        SplashDestinations.LoginDest -> navigator.navigate(LoginScreenDestination)
    }
}


@Composable
private fun ScreenBody() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MyImageLogo()
        Text(text= stringResource(id = R.string.app_name),modifier = Modifier, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview
@Composable
fun SplashPreview() {
    FitJournalTheme {
        ScreenBody()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    FitJournalTheme {
        ScreenBody()
    }
}