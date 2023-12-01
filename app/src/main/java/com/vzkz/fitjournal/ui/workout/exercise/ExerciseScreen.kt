package com.vzkz.fitjournal.ui.workout.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.ExerciseScreenDestination
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import kotlinx.coroutines.delay

@Destination
@Composable
fun ExerciseScreen(
    navigator: DestinationsNavigator,
    indexOfWorkout: Int,
    indexOfExercise: Int,
    exerciseViewModel: ExerciseViewModel = hiltViewModel()
) {
    exerciseViewModel.onInitExercise()
    if (!exerciseViewModel.state.start) {
        MyCircularProgressbar()
    } else {
        val user = exerciseViewModel.state.user
        ScreenBody(
            user = user,
            indexOfWorkout = indexOfWorkout,
            indexOfExercise = indexOfExercise,
            onEndExercise = { workoutComplete ->
                if (workoutComplete) navigator.navigate(HomeScreenDestination)
                else navigator.navigate(
                    ExerciseScreenDestination(
                        indexOfWorkout = indexOfWorkout,
                        indexOfExercise = (indexOfExercise + 1)
                    )
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    user: UserModel?,
    indexOfWorkout: Int,
    indexOfExercise: Int,
    onEndExercise: (Boolean) -> Unit
) {
    val exercise =
        user?.workouts?.get(indexOfWorkout)?.exercises?.get(indexOfExercise)
    val exNum =
        user?.workouts?.get(indexOfWorkout)?.exercises?.size
    if (exercise == null || exNum == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Error", style = MaterialTheme.typography.displayLarge)
        }
    } else {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = exercise.exData.exName) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Nav back")
                    }
                })
        }) { paddingValues ->
            var pointer by remember { mutableIntStateOf(1) }
            var showRest by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    for (rep in exercise.setXrepXweight) {
                        MyOutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            selected = pointer == (rep.exNum.toInt())
                        ) {
                            Row {
                                Text(text = rep.exNum, modifier = Modifier.padding(10.dp))
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = "${rep.reps} x ${rep.weight} kg",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (pointer == exercise.setNum) {
                            if (indexOfExercise < exNum - 1) {
                                onEndExercise(false)
                            } else {
                                onEndExercise(true)
                            }
                        } else {
                            pointer++
                            showRest = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.log_set))
                }

                if (showRest) {
                    RestTimer(rest = exercise.rest, modifier = Modifier.align(Alignment.Center)) {
                        showRest = false
                    }
                }

            }
        }
    }
}

@Composable
private fun MyOutlinedCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        colors = if (selected) CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primary
        ) else CardDefaults.outlinedCardColors()
    ) {
        content()
    }
}

@Composable
private fun RestTimer(rest: Int, modifier: Modifier = Modifier, onRestFinished: () -> Unit) {
    var timeLeft by remember { mutableIntStateOf(rest) }

    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
            if (timeLeft == 0) onRestFinished()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.rest2),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$timeLeft",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 80.sp,
                )
                Text(
                    text = " s",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

            }
            Button(
                onClick = { onRestFinished() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = stringResource(R.string.skip_rest))
            }
        }

    }

}


//@Preview(showSystemUi = false)
//@Composable
//fun LightPreview() {
//    FitJournalTheme {
//    }
//}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FitJournalTheme {
//        ScreenBody()
//    }
//}