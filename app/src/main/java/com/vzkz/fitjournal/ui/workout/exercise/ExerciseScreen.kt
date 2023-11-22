package com.vzkz.fitjournal.ui.workout.exercise

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vzkz.fitjournal.ui.theme.FitJournalTheme

@Destination
@Composable
fun ExerciseScreen(navigator: DestinationsNavigator) {
    ScreenBody()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody() {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Incline Hammer Curls") }, navigationIcon = {
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
            val exNum = 4
            Column(modifier = Modifier.align(Alignment.Center)) {
                for (i in 1..exNum){
                    OutlinedCard(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ) {
                        Row {
                            Text(text = "$i", modifier = Modifier.padding(10.dp))
                            Spacer(Modifier.weight(1f))
                            Text(text = "8 x 70 kg", modifier = Modifier.padding(10.dp))
                        }
                    }

                }

            }


            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Log set")
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