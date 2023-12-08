package com.vzkz.fitjournal.ui.workout.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.theme.FitJournalTheme


@Destination
@Composable
fun WorkoutCompleteScreen(
    navigator: DestinationsNavigator,
    indexOfWorkout: Int,
    exerciseViewModel: ExerciseViewModel = hiltViewModel()
) {
    exerciseViewModel.onInitExercise()
    if(exerciseViewModel.state.start){
        val user = exerciseViewModel.state.user

        ScreenBody(
            onGoToHome ={
                exerciseViewModel.onSyncDates(user ?: UserModel(), indexOfWorkout)
                navigator.navigate(HomeScreenDestination)
            }
        )
    }
}


@Composable
fun ScreenBody(
    onGoToHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Tick",
                Modifier.size(80.dp),
            )
            Text(text = "Workout complete!", style = MaterialTheme.typography.displayMedium)
            MySpacer(size = 16)
            Button(onClick = { onGoToHome() }, Modifier.padding(24.dp)) {
                Text("Go to home", fontSize = 20.sp)
            }
        }

    }

}

@Preview
@Composable
fun LightWotCompletePreview() {
    FitJournalTheme {
        ScreenBody(
            onGoToHome = {}
        )
    }
}