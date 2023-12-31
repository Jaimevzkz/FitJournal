package com.vzkz.fitjournal.ui.workout.exlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.ExerciseScreenDestination
import com.vzkz.fitjournal.destinations.WorkoutScreenDestination
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MySpacer

@Destination
@Composable
fun ExListScreen(
    navigator: DestinationsNavigator,
    indexOfWorkout: Int,
    exlistViewModel: ExlistViewModel = hiltViewModel()
) {
    exlistViewModel.onInitWorkouts()
    val start = exlistViewModel.state.start
    val user = exlistViewModel.state.user
    ScreenBody(
        user = user,
        start = start,
        indexOfWorkout = indexOfWorkout,
        onBackClicked = { navigator.navigate(WorkoutScreenDestination) },
        onStartWorkout = { navigator.navigate(ExerciseScreenDestination(indexOfWorkout = indexOfWorkout, indexOfExercise = 0)) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    user: UserModel?,
    start: Boolean,
    indexOfWorkout: Int,
    onBackClicked: () -> Unit,
    onStartWorkout: () -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Upper body strength") }, navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Nav back")
            }
        })
    }) { paddingValues ->
        if (!start) {
            MyCircularProgressbar()
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                val exercisesList = user?.workouts?.get(indexOfWorkout)?.exercises
                if (exercisesList != null) {
                    LazyColumn(modifier = Modifier.padding(bottom = 68.dp)) {
                        items(exercisesList) { exercise ->
                            MyExerciseAddedCardView(
                                exName = exercise.exData.exName,
                                setNum = exercise.setNum,
                                rest = exercise.rest
                            )
                        }
                    }
                }

                Button(
                    onClick = { onStartWorkout() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.start_workout))
                }

            }
        }
    }
}

@Composable
private fun MyExerciseAddedCardView(
    exName: String,
    setNum: Int,
    rest: Int
) {
    val backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(backgroundColor)
    ) {
        Row(
            Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = exName,
                    color = contentColor,
                    style = MaterialTheme.typography.titleLarge
                )
                MySpacer(size = 8)
                Row {
                    Text(
                        text = "$setNum sets",
                        color = contentColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    MySpacer(size = 8)
                    Text(
                        text = stringResource(R.string.rest) + "$rest segs",
                        color = contentColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun LightPreview() {
//    FitJournalTheme {
//        ScreenBody()
//    }
//}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FitJournalTheme {
//        ScreenBody()
//    }
//}