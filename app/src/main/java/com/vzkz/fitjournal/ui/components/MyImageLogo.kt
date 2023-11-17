package com.vzkz.fitjournal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vzkz.fitjournal.R

@Composable
fun MyImageLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "App Logo",
        modifier = modifier.size(90.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
    )
}