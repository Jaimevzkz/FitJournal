
package com.vzkz.fitjournal.ui.workout.searchexercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.WorkoutScreenDestination
import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.Exercises
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.ui.components.MyCircularProgressbar
import com.vzkz.fitjournal.ui.components.MyGenericTextField
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@Destination
@Composable
fun SearchExerciseScreen(
    navigator: DestinationsNavigator,
    searchExerciseViewModel: SearchExerciseViewModel = hiltViewModel()
) {
    val vmExerciseList = searchExerciseViewModel.state.exerciseList
    val newWorkoutExerciseList = searchExerciseViewModel.state.newWorkoutExerciseList
    val loading = searchExerciseViewModel.state.loading
    val noResults = searchExerciseViewModel.state.noResults
    val exerciseModelToAdd = searchExerciseViewModel.state.exerciseModelToAdd
    ScreenBody(
        vmExerciseList = vmExerciseList,
        newWorkoutExerciseList = newWorkoutExerciseList,
        loading = loading,
        noResults = noResults,
        exerciseModelToAdd = exerciseModelToAdd,
        onWorkoutAdded = { wotName, exList ->
            searchExerciseViewModel.onWorkoutAdded(wotName = wotName, exerciseList = exList)
        },
        onExerciseAdded = { exercise ->
            searchExerciseViewModel.onExerciseAdded(exercise)
        },
        onSetExerciseModelToAdd = { exModel ->
            searchExerciseViewModel.onSetExerciseModelToAdd(exModel)
        },
        onSearchByName = { searchContent ->
            searchExerciseViewModel.onSearchByName(searchContent)
        },
        onBackCLicked = {
            navigator.navigate(WorkoutScreenDestination)
        },
        onWorkoutCreated = {
            navigator.navigate(WorkoutScreenDestination)
        })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun ScreenBody(
    vmExerciseList: List<ExerciseModel>?,
    newWorkoutExerciseList: MutableList<Exercises>,
    loading: Boolean,
    noResults: Boolean,
    onWorkoutAdded: (String, List<Exercises>) -> Unit,
    onExerciseAdded: (Exercises) -> Unit,
    exerciseModelToAdd: ExerciseModel?,
    onSetExerciseModelToAdd: (ExerciseModel) -> Unit,
    onSearchByName: (String) -> Unit,
    onBackCLicked: () -> Unit,
    onWorkoutCreated: () -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.search_exercise)) },
            navigationIcon = {
                IconButton(onClick = { onBackCLicked() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Nav back")
                }
            })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            var searchContent: String by remember { mutableStateOf("") }
            var exerciseList: List<ExerciseModel> by remember { mutableStateOf(emptyList()) }
            val keyboardController = LocalSoftwareKeyboardController.current

            var isAddExCardViewVisible by remember { mutableStateOf(false) }
            var isAddCreateWorkoutViewVisible by remember { mutableStateOf(false) }


            exerciseList = vmExerciseList ?: emptyList()

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MyGenericTextField(
                    modifier = Modifier.weight(1.5f),
                    hint = "",
                    text = searchContent,
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchContent != "") {
                                keyboardController?.hide()
                                onSearchByName(searchContent)
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                        }
                    },
                    onTextChanged = {
                        searchContent = it
                    })

                MySpacer(size = 4)

                Text(
                    text = stringResource(R.string.introduce_the_name_of_the_exercise_to_search),
                    style = MaterialTheme.typography.labelMedium
                )

                MySpacer(size = 4)

                if (exerciseList.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.weight(10f)) {
                        items(exerciseList) { exercise ->
                            var addedEx by remember { mutableStateOf(false) }
                            for (ex in newWorkoutExerciseList) {
                                if (ex.exData == exercise) {
                                    addedEx = true
                                    break
                                }
                            }
                            MyExerciseCardView(
                                exerciseModel = exercise,
                                added = addedEx,
                                onVisibleAddExerciseCV = { exModel, isExAdded ->
                                    if (isExAdded) { //Delete exercise from list
                                        for (exToDelete in newWorkoutExerciseList) {
                                            if (exToDelete.exData == exercise) {
                                                newWorkoutExerciseList.remove(
                                                    exToDelete
                                                )
                                                break
                                            }
                                        }
                                        addedEx = false
                                    } else { //Add Exercise to list
                                        onSetExerciseModelToAdd(exModel)
                                        isAddExCardViewVisible = true
                                    }
                                }
                            )
                        }
                    }
                } else if (loading) MyCircularProgressbar(Modifier.weight(10f))
                else Spacer(modifier = Modifier.weight(10f))

                Button(
                    onClick = {
                        isAddCreateWorkoutViewVisible = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.create))
                }
            }

            if (noResults) {
                Text(
                    text = stringResource(R.string.no_exercises_found_with_that_name),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (isAddExCardViewVisible) {
                AddExerciseCardView(
                    exerciseModel = exerciseModelToAdd!!,
                    onAddExercise = {
                        val exerciseToAdd = it.copy(
                            exOrder = exerciseList.size + 1
                        )
                        onExerciseAdded(exerciseToAdd)
                        isAddExCardViewVisible = false
                    },
                    onCancel = {
                        isAddExCardViewVisible = false
                    }
                )
            }

            if (isAddCreateWorkoutViewVisible) {
                CreateWorkoutCardView(
                    onAddWorkout = {
                        onWorkoutAdded(
                            it,
                            newWorkoutExerciseList
                        )
                        onWorkoutCreated()
                    }, onCancel = {
                        isAddCreateWorkoutViewVisible = false
                    }
                )

            }
        }
    }
}

@Composable
private fun MyExerciseCardView(
    exerciseModel: ExerciseModel,
    added: Boolean,
    onVisibleAddExerciseCV: (ExerciseModel, Boolean) -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { expand = !expand }
    ) {
        Row(
            Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(4f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = exerciseModel.exName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                MySpacer(size = 8)
                Text(
                    text = stringResource(R.string.muscle) + exerciseModel.muscle,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                MySpacer(size = 4)
                Text(
                    text = stringResource(R.string.difficulty) + exerciseModel.difficulty,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                if (expand) {
                    MySpacer(size = 8)
                    Text(
                        text = stringResource(R.string.instructions) + exerciseModel.instructions,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }

            IconButton(
                onClick = {
                    onVisibleAddExerciseCV(exerciseModel, added)
                },
                modifier = Modifier
                    .weight(1f)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = if (added) Icons.Filled.TaskAlt else Icons.Filled.Add,
                    contentDescription = "Add/delete exercise",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}

@Composable
private fun AddExerciseCardView(
    exerciseModel: ExerciseModel,
    onAddExercise: (Exercises) -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.BottomCenter
    ) {
        var rest by remember { mutableStateOf("") }
        var setNum by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = exerciseModel.exName)
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.rest2) + ": ",
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                MyGenericTextField(
                    modifier = Modifier,
                    hint = "",
                    text = rest,
                    onTextChanged = { rest = it },
                    numberKeyboard = true,
                    outlined = false
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.sets),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.weight(1f))
                MyGenericTextField(
                    modifier = Modifier,
                    hint = "",
                    text = setNum,
                    onTextChanged = { setNum = it },
                    numberKeyboard = true,
                    outlined = false
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onCancel() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
            MySpacer(size = 8)
            Button(onClick = {
                val listOfSets = mutableListOf<SetXrepXweight>()
                for (i in 1..setNum.toInt()) {
                    listOfSets.add(SetXrepXweight(exNum = i.toString()))
                }
                val exercise = Exercises(
                    exid = "-1",
                    rest = rest.toInt(),
                    exData = exerciseModel,
                    setNum = setNum.toInt(),
                    exOrder = -1,
                    setXrepXweight = listOfSets
                )
                onAddExercise(exercise)
            }) {
                Text(text = stringResource(R.string.add_exercise))
            }
        }


    }
}

@Composable
private fun CreateWorkoutCardView(
    onAddWorkout: (String) -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.BottomCenter
    ) {
        var wotName by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_workout),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.workout_name) + ": ",
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.bodyLarge
                )
                MyGenericTextField(
                    modifier = Modifier,
                    hint = "",
                    text = wotName,
                    onTextChanged = { wotName = it },
                    numberKeyboard = false,
                    outlined = false
                )
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onCancel() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
            MySpacer(size = 8)
            Button(onClick = {
                onAddWorkout(wotName)
            }) {
                Text(text = stringResource(R.string.create))
            }
        }


    }
}

@Preview(showSystemUi = false, device = Devices.NEXUS_5X)
@Composable
fun LightPreview() {
    FitJournalTheme {
        ScreenBody(
            vmExerciseList = listOf(
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
                ExerciseModel(
                    exName = "Bench press",
                    muscle = "Chest",
                    difficulty = "Intermediate",
                    instructions = "example instruction"
                ),
            ),
            newWorkoutExerciseList = mutableListOf(),
            loading = false,
            noResults = false,
            onWorkoutAdded = { _, _ -> },
            onExerciseAdded = {},
            exerciseModelToAdd = ExerciseModel(
                exName = "Bench press",
                muscle = "Chest",
                difficulty = "Intermediate",
                instructions = "example instruction"
            ),
            onSetExerciseModelToAdd = {},
            onSearchByName = {},
            onBackCLicked = {  }) {

        }
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FitJournalTheme {
//        ScreenBody()
//    }
//}