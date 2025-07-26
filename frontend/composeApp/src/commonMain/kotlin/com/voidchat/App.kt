package com.voidchat

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.voidchat.theme.VoidTheme
import com.voidchat.ui.screen.SignInScreen
import com.voidchat.ui.screen.WelcomeScreen


@Composable
fun App() {
    VoidTheme {
        Navigator(SignInScreen)
    }
}