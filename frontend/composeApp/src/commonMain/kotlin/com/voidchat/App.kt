package com.voidchat

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import com.voidchat.theme.VoidTheme
import com.voidchat.utils.DeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    VoidTheme {
      val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
      val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

      when (deviceConfiguration) {
          DeviceConfiguration.MOBILE_PORTRAIT -> {
              // TODO: Compact layout
          }

          DeviceConfiguration.MOBILE_LANDSCAPE,
          DeviceConfiguration.TABLET_PORTRAIT -> {
              // TODO: Medium layout
          }

          DeviceConfiguration.TABLET_LANDSCAPE,
          DeviceConfiguration.DESKTOP -> {
              // TODO: Expanded layout
          }
      }


    }
}