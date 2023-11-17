package com.vzkz.fitjournal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.vzkz.fitjournal.NavGraphs
import com.vzkz.fitjournal.domain.usecases.ThemeDSUseCase
import com.vzkz.fitjournal.ui.theme.FitJournalTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeDSUseCase:ThemeDSUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var flow: Flow<Boolean> = MutableStateFlow(false)
        CoroutineScope(Dispatchers.IO).launch {
            flow = themeDSUseCase()
        }
        setContent {
            var darkTheme by remember{ mutableStateOf(false) }
            LaunchedEffect(flow) {
                flow.collect { value ->
                    darkTheme = value
                }
            }
            FitJournalTheme(useDarkTheme = darkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}