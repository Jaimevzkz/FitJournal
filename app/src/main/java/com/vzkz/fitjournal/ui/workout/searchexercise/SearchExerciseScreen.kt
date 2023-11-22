package com.vzkz.fitjournal.ui.workout.searchexercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.ui.components.MyGenericTextField
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@Destination
@Composable
fun SearchExerciseScreen(navigator: DestinationsNavigator) {
    ScreenBody()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody() {
    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
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
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MyGenericTextField(
                    modifier = Modifier,
                    hint = "Search exercise",
                    text = searchContent,
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                        }
                    },
                    onTextChanged = {
                        searchContent = it
                    })

                MySpacer(size = 8)

                LazyColumn(modifier = Modifier) {
                    item { MyExerciseCardView() }
                    item { MyExerciseCardView() }
                    item { MyExerciseCardView() }
                    item { MyExerciseCardView() }
                    item { MyExerciseCardView() }
                    item { MyExerciseCardView() }
                    item { MyExerciseCardView() }
                }

            }

            Button(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Create")
            }

        }

    }
}

@Composable
private fun MyExerciseCardView() {
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
                    text = "Incline Hammer Curls",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                MySpacer(size = 8)
                Row {
                    Text(
                        text = "Muscle: Biceps",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    MySpacer(size = 4)
                    Text(
                        text = "Difficulty: Beginner",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (expand) {
                    MySpacer(size = 8)
                    Text(
                        text = "Instructions: Seat yourself on an incline bench with a dumbbell in each hand. You should pressed firmly against he back with your feet together. Allow the dumbbells to hang straight down at your side, holding them with a neutral grip. This will be your starting position. Initiate the movement by flexing at the elbow, attempting to keep the upper arm stationary. Continue to the top of the movement and pause, then slowly return to the start position.",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }

            IconButton(
                onClick = {
                    if (!added) {
                        /*TODO*/
                    }
                    added = true

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
        ScreenBody()
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FitJournalTheme {
//        ScreenBody()
//    }
//}