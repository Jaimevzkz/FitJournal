package com.vzkz.fitjournal.core.boilerplate.template.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateScaffold(onBottomBarClicked: (DirectionDestinationSpec) -> Unit){
    Scaffold(bottomBar = {
        MyBottomBar(
            currentDestination = HomeScreenDestination,
            onClick = { onBottomBarClicked(it) })
    }) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {

            }
        }
    }

}
