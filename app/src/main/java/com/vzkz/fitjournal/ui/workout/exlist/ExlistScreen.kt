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
    ScreenBody(
        exlistViewModel = exlistViewModel,
        indexOfWorkout = indexOfWorkout,
        onStartWorkout = { navigator.navigate(ExerciseScreenDestination(indexOfWorkout = indexOfWorkout, indexOfExercise = 0)) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    exlistViewModel: ExlistViewModel,
    indexOfWorkout: Int,
    onStartWorkout: () -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Upper body strength") }, navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Nav back")
            }
        })
    }) { paddingValues ->
        if (!exlistViewModel.state.start) {
            MyCircularProgressbar()
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                val exercisesList =
                    exlistViewModel.state.user?.workouts?.get(indexOfWorkout)?.exercises
                if (exercisesList != null) {
                    LazyColumn(modifier = Modifier) {
                        items(exercisesList) { exercise ->
                            MyExerciseAddedCardView(
                                exName = exercise.exData.exName,
                                setNum = exercise.setNum,
                                rest = exercise.rest
                            )
                        }

                        //TODO item add exercise
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
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
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                MySpacer(size = 8)
                Row {
                    Text(
                        text = "$setNum sets",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    MySpacer(size = 8)
                    Text(
                        text = stringResource(R.string.rest) + "$rest segs",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
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