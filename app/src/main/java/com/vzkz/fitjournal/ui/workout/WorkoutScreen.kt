package com.vzkz.fitjournal.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.ExListScreenDestination
import com.vzkz.fitjournal.destinations.SearchExerciseScreenDestination
import com.vzkz.fitjournal.destinations.WorkoutScreenDestination
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar

@Destination
@Composable
fun WorkoutScreen(
    navigator: DestinationsNavigator,
    workoutViewModel: WorkoutViewModel = hiltViewModel()
) {
    workoutViewModel.onInitWorkouts()
    ScreenBody(
        workoutViewModel = workoutViewModel,
        onBottomBarClicked = { navigator.navigate(it) },
        onAddWorkOutClicked = { navigator.navigate(SearchExerciseScreenDestination) },
        onWorkOutClicked = { navigator.navigate(ExListScreenDestination(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    workoutViewModel: WorkoutViewModel,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onAddWorkOutClicked: () -> Unit,
    onWorkOutClicked: (Int) -> Unit
) {
    Scaffold(
        bottomBar = {
            MyBottomBar(
                currentDestination = WorkoutScreenDestination,
                onClick = { onBottomBarClicked(it) }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Workouts",
                    style = MaterialTheme.typography.displaySmall
                )
            })
        }
    ) { paddingValues ->
        if(!workoutViewModel.state.start){
            MyCircularProgressbar()
        } else{
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.TopCenter
            ) {
                val workoutList = workoutViewModel.state.user?.workouts
                if (workoutList != null) {
                    LazyColumn {
                        items(workoutList) { workout ->
                            MyCardViewWorkout(
                                wotTitle = workout.wotName,
                                wotDuration = workout.duration,
                                exCount = workout.exCount,
                                onWorkOutClicked = {
                                    onWorkOutClicked(workoutList.indexOf(workout))
                                }
                            )
                        }
                        item {
                            MyAddWorkoutCardView { onAddWorkOutClicked() }
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun MyCardViewWorkout(
    wotTitle: String,
    wotDuration: Int,
    exCount: Int,
    onWorkOutClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = wotTitle,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )

                MySpacer(size = 4)

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ("$wotDuration min"),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    MySpacer(size = 12)
                    Text(
                        text = exCount.toString() + stringResource(R.string.exercises),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            IconButton(onClick = { onWorkOutClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "Go to Workout",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

    }
}

@Composable
private fun MyAddWorkoutCardView(onAddWorkOutClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.add_workout),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            IconButton(
                onClick = { onAddWorkOutClicked() }, modifier = Modifier
                    .weight(2f)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Workout",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}

//@Preview
//@Composable
//fun LightPreview() {
//    FitJournalTheme {
//        ScreenBody(onBottomBarClicked = {}, onAddWorkOutClicked = {})
//    }
//}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}


