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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.WorkoutScreenDestination
import com.vzkz.fitjournal.domain.model.ExerciseModel
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
    ScreenBody(searchExerciseViewModel = searchExerciseViewModel, onBackCLicked = {
        navigator.navigate(WorkoutScreenDestination)
    })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun ScreenBody(searchExerciseViewModel: SearchExerciseViewModel, onBackCLicked: () -> Unit) {
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

            exerciseList = searchExerciseViewModel.state.exerciseList ?: emptyList()

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MyGenericTextField(
                    modifier = Modifier.weight(1f),
                    hint = "",
                    text = searchContent,
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchContent != "") {
                                keyboardController?.hide()
                                searchExerciseViewModel.onSearchByName(searchContent)

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
                            MyExerciseCardView(exerciseModel = exercise)
                        }
                    }
                } else if (searchExerciseViewModel.state.loading) MyCircularProgressbar(Modifier.weight(10f))
                else Spacer(modifier = Modifier.weight(10f))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp, top = 10.dp)
                        .weight(1f)
                        .shadow(elevation = 20.dp, shape = RoundedCornerShape(20.dp))
                ) {
                    Text(text = "Create")
                }
            }

            if (searchExerciseViewModel.state.noResults) {
                Text(
                    text = stringResource(R.string.no_exercises_found_with_that_name),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun MyExerciseCardView(exerciseModel: ExerciseModel) {
    var expand by remember { mutableStateOf(false) }
    var added by remember { mutableStateOf(false) }
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
                    text = exerciseModel.name,
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
                    if (!added) {
                        /*TODO add exercise*/
                        added = true
                    } else {
                        /*TODO delete exercise*/
                        added = false
                    }

                },
                modifier = Modifier
                    .weight(1f)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = if (added) Icons.Filled.TaskAlt else Icons.Filled.Add,
                    contentDescription = "Go to Workout",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
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
        ScreenBody(hiltViewModel(), onBackCLicked = {})
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FitJournalTheme {
//        ScreenBody()
//    }
//}