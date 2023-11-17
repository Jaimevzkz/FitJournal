package com.vzkz.fitjournal.ui.components.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.destinations.WorkoutScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    HomeDest(HomeScreenDestination, Icons.Outlined.Home, R.string.home),
    WorkoutDest(WorkoutScreenDestination, Icons.Outlined.FitnessCenter, R.string.workout),
    ProfileDest(ProfileScreenDestination, Icons.Outlined.Person, R.string.profile)
}