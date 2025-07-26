package com.voidchat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.voidchat.ui.component.BlinkingText
import com.voidchat.utils.DeviceConfiguration
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.EyeOff

object SignInScreen : Screen {
    @Composable
    override fun Content() {


        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                // TODO: Compact layout
                SignInScreenMobile()
            }

            DeviceConfiguration.MOBILE_LANDSCAPE, DeviceConfiguration.TABLET_PORTRAIT -> {
                // TODO: Medium layout
            }

            DeviceConfiguration.TABLET_LANDSCAPE, DeviceConfiguration.DESKTOP -> {
                // TODO: Expanded layout
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SignInScreenMobile() {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val width = (maxWidth / 2f)
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(
                        WindowInsets.systemBars
                    ), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top
            )
            {

                BlinkingText(
                    "Before every message, there’s a moment of silence. This is where that moment begins",
                    modifier = Modifier.padding(8.dp).width(width).align(Alignment.End)
                        .height(70.dp),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(Modifier.height(40.dp))

                Text(
                    "sign in", style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 64.sp, fontStyle = FontStyle.Italic
                    ), color = MaterialTheme.colorScheme.onBackground
                )

                ColoredBox()

                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = MaterialShapes.Pill.toShape(),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.4f)
                    ) {}
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        SignInTextField(
                            email, "email",
                            contentAlign = Alignment.CenterStart,
                            boxAlign = Alignment.CenterEnd,
                            textAlign = Alignment.Start,
                            showPasswordIcon = false, isPassword = false
                        )


                        Spacer(Modifier.height(24.dp))


                        SignInTextField(
                            password, "password",
                            contentAlign = Alignment.CenterEnd,
                            boxAlign = Alignment.CenterStart,
                            textAlign = Alignment.End,
                            showPasswordIcon = true, isPassword = true
                        )
                    }
                }



                ColoredBox()

                BlinkingText(
                    "No words yet, but the silence is listening. Your voice begins now.",
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                )

            }
        }

    }
}

@Composable
private fun SignInTextField(
    variable: MutableState<String>,
    name: String,
    contentAlign: Alignment,
    showPasswordIcon: Boolean,
    isPassword: Boolean,
    textAlign: Alignment.Horizontal,
    boxAlign: Alignment
) {
    val showPassword = remember { mutableStateOf(isPassword) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(boxAlign)
                .height(70.dp),
            contentAlignment = contentAlign
        )
        {
            OutlinedTextField(
                shape = RoundedCornerShape(0.dp),
                visualTransformation = if (showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = if (showPassword.value) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                value = variable.value,
                onValueChange = { variable.value = it },
                trailingIcon = {
                    if (showPasswordIcon) {
                        IconButton(onClick = {
                            showPassword.value = !showPassword.value
                        }) {
                            Icon(
                                if (showPassword.value) EvaIcons.Outline.Eye else EvaIcons.Outline.EyeOff,
                                contentDescription = null
                            )
                        }
                    }
                },
                label = {
                    Text(
                        name,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )

                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                        alpha = 0.6f
                    ),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                        alpha = 0.6f
                    ),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ColoredBox() {
    Box(
        modifier = Modifier.fillMaxWidth().height(50.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}