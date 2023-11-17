package com.vzkz.fitjournal.ui.components

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun MyAuthHeader(modifier: Modifier) {
    val activity = (LocalContext.current as? Activity)
    IconButton(onClick = { activity?.finish() }, modifier = modifier) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close app")
    }
}