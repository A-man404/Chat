package com.voidchat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.voidchat.ui.component.BlinkingText
import com.voidchat.utils.DeviceConfiguration

object WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                // TODO: Compact layout
                WelcomeContent()

            }

            DeviceConfiguration.MOBILE_LANDSCAPE, DeviceConfiguration.TABLET_PORTRAIT -> {
                // TODO: Medium layout
            }

            DeviceConfiguration.TABLET_LANDSCAPE, DeviceConfiguration.DESKTOP -> {
                // TODO: Expanded layout
                WelcomeContent()
            }
        }
    }
}

@Composable
private fun WelcomeContent() {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlinkingText(
            "Step into the Void..",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayLarge
        )
    }
}