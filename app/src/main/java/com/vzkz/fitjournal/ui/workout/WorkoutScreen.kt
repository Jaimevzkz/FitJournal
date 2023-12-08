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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.ExListScreenDestination
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.destinations.SearchExerciseScreenDestination
import com.vzkz.fitjournal.destinations.WorkoutScreenDestination
import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.Exercises
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyConfirmDialog
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@Destination
@Composable
fun WorkoutScreen(
    navigator: DestinationsNavigator,
    workoutViewModel: WorkoutViewModel = hiltViewModel()
) {
    workoutViewModel.onInitWorkouts()
    val start = workoutViewModel.state.start
    var user: UserModel? by remember{ mutableStateOf(null) }
    user = workoutViewModel.state.user
    ScreenBody(
        start = start,
        user = user,
        onBottomBarClicked = { navigator.navigate(it) },
        onAddWorkOutClicked = { navigator.navigate(SearchExerciseScreenDestination) },
        onWorkOutClicked = { navigator.navigate(ExListScreenDestination(it)) },
        onDeleteWorkout = { workoutViewModel.onDeleteWorkout(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    start: Boolean,
    user: UserModel?,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit,
    onAddWorkOutClicked: () -> Unit,
    onWorkOutClicked: (Int) -> Unit,
    onDeleteWorkout: (String) -> Unit
) {
    var editable by remember { mutableStateOf(false) }
    var widToDelete by remember { mutableStateOf("") }
    Scaffold(
        bottomBar = {
            MyBottomBar(
                currentDestination = WorkoutScreenDestination,
                onClick = { if(it != WorkoutScreenDestination)onBottomBarClicked(it) }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Workouts",
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                actions = {
                    IconButton(onClick = {
                        editable = !editable
                    }) {
                        Icon(
                            imageVector = if (!editable) Icons.Filled.Edit else Icons.Filled.EditOff,
                            contentDescription = "Edit workouts"
                        )
                    }
                })
        }
    ) { paddingValues ->
        if(!start){
            MyCircularProgressbar()
        } else{
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.TopCenter
            ) {
                val workoutList = user?.workouts
                if (workoutList != null) {
                    var showDialog by remember { mutableStateOf(false) }
                    LazyColumn {
                        items(workoutList) { workout ->
                            MyCardViewWorkout(
                                wotTitle = workout.wotName,
                                wid = workout.wid,
                                wotDuration = workout.duration,
                                exCount = workout.exCount,
                                editable = editable,
                                onDeleteWorkout = { wid ->
                                    widToDelete = wid
                                    showDialog = true
                                },
                                onWorkOutClicked = {
                                    onWorkOutClicked(workoutList.indexOf(workout))
                                }
                            )
                        }
                        item {
                            MyAddWorkoutCardView { onAddWorkOutClicked() }
                        }

                    }
                    if (showDialog) {
                        MyConfirmDialog(
                            modifier = Modifier.align(Alignment.Center),
                            title = "Are you sure?",
                            text = "Deleting a workout is undoable",
                            onDismiss = { showDialog = false },
                            onConfirm = {
                                if (widToDelete != "")
                                    onDeleteWorkout(widToDelete)
                                showDialog = false
                            },
                            showDialog = showDialog
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MyCardViewWorkout(
    wid: String,
    wotTitle: String,
    wotDuration: Int,
    exCount: Int,
    editable: Boolean,
    onDeleteWorkout: (String) -> Unit,
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
        if (editable) {
            IconButton(onClick = { onDeleteWorkout(wid) }, Modifier.align(Alignment.TopStart)) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Delete Workout")
            }
        }
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

@Preview
@Composable
fun LightPreview() {
    FitJournalTheme {
        ScreenBody(
            start = true,
            user = UserModel(
                uid = "fl",
                nickname = "k",
                email = null,
                firstname = "ja",
                lastname = "kf",
                workouts = listOf(
                    WorkoutModel(
                        wid = "wZSTSkzOQN25oEKDeocg",
                        wotName = "Upper body strength",
                        duration = 89,
                        exCount = 3,
                        wotOrder = 1,
                        exercises = listOf(
                            Exercises(
                                exid = "vaK3TB32xdKgMifrKCV3",
                                rest = 60,
                                exData = ExerciseModel(
                                    exName = "Squat",
                                    muscle = "Legs",
                                    difficulty = "Begginer",
                                    instructions = "Example instruction"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1"),
                                    SetXrepXweight(exNum = "2"),
                                    SetXrepXweight(exNum = "3")
                                ),
                                setNum = 3,
                                exOrder = 1
                            ),
                            Exercises(
                                exid = "DIsDh0yIFJ5j5XWfOW81",
                                rest = 30,
                                exData = ExerciseModel(
                                    exName = "Bench Press",
                                    muscle = "Chest",
                                    difficulty = "Intermediate",
                                    instructions = "Example instruction 2"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1"),
                                    SetXrepXweight(exNum = "2"),
                                    SetXrepXweight(exNum = "3"),
                                    SetXrepXweight(exNum = "4")
                                ),
                                setNum = 4,
                                exOrder = 2
                            ),
                            Exercises(
                                exid = "HpAkRh7jcECHRawrtqjI",
                                rest = 60,
                                exData = ExerciseModel(
                                    exName = "Biceps curl",
                                    muscle = "Biceps",
                                    difficulty = "Intermediate",
                                    instructions = "Example instruction 3"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1"),
                                    SetXrepXweight(exNum = "2"),
                                    SetXrepXweight(exNum = "3"),
                                    SetXrepXweight(exNum = "4")
                                ),
                                setNum = 4,
                                exOrder = 3
                            )
                        )
                    ),
                    WorkoutModel(
                        wid = "rduoIqR4EJVN4FiEvucp",
                        wotName = "Legs strength",
                        duration = 105,
                        exCount = 4,
                        wotOrder = 2,
                        exercises = listOf(
                            Exercises(
                                exid = "QOYW1RvcU0aVJnB1aiNa",
                                rest = 120,
                                exData = ExerciseModel(
                                    exName = "Squat",
                                    muscle = "Legs",
                                    difficulty = "Begginer",
                                    instructions = "Example instruction"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1"),
                                    SetXrepXweight(exNum = "2"),
                                    SetXrepXweight(exNum = "3")
                                ),
                                setNum = 3,
                                exOrder = 1
                            ),
                            Exercises(
                                exid = "DIsDh0yIFJ5j5XWfOW81",
                                rest = 50,
                                exData = ExerciseModel(
                                    exName = "Bench Press",
                                    muscle = "Chest",
                                    difficulty = "Intermediate",
                                    instructions = "Example instruction 2"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1"),
                                    SetXrepXweight(exNum = "2"),
                                    SetXrepXweight(exNum = "3"),
                                    SetXrepXweight(exNum = "4")
                                ),
                                setNum = 4,
                                exOrder = 2
                            ),
                            Exercises(
                                exid = "aWIAv7909ody9t1kQQMe",
                                rest = 50,
                                exData = ExerciseModel(
                                    exName = "Biceps curl",
                                    muscle = "Biceps",
                                    difficulty = "Intermediate",
                                    instructions = "Example instruction 3"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1"),
                                    SetXrepXweight(exNum = "2"),
                                    SetXrepXweight(exNum = "3"),
                                    SetXrepXweight(exNum = "4")
                                ),
                                setNum = 4,
                                exOrder = 3
                            ),
                            Exercises(
                                exid = "dmdFqRZcATGPv0N3cltR",
                                rest = 50,
                                exData = ExerciseModel(
                                    exName = "Press",
                                    muscle = "Chest",
                                    difficulty = "Intermediate",
                                    instructions = "Example instruction 2"
                                ),
                                setXrepXweight = listOf(
                                    SetXrepXweight(exNum = "1")
                                ),
                                setNum = 1,
                                exOrder = 4
                            )
                        )
                    )
                )
            ),
            onBottomBarClicked = {},
            onAddWorkOutClicked = {},
            onWorkOutClicked = {},
            onDeleteWorkout = {}
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//
//}


