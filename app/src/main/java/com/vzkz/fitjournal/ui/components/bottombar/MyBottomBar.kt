package com.vzkz.fitjournal.ui.components.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@Composable
fun MyBottomBar(
    currentDestination: DirectionDestinationSpec,
    onClick: (DirectionDestinationSpec) -> Unit
) {
    NavigationBar {
        BottomBarDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = destination.direction == currentDestination,
                onClick = {
                    onClick(destination.direction)
                },
                icon = { Icon(destination.icon, contentDescription = stringResource(destination.label))},
                label = { Text(stringResource(destination.label)) }
            )
        }
    }
}