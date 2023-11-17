package com.vzkz.fitjournal.ui.splash

sealed class SplashDestinations {
    data object HomeDest: SplashDestinations()
    data object LoginDest: SplashDestinations()
}