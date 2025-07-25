package com.voidchat.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.delay

@Composable
fun BlinkingText(
    text: String,
    color: Color,
    style: TextStyle = TextStyle.Default,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Visible
) {
    val typingSpeed = 50L
    val cursorBlinkSpeed = 500L
    var visibleText by remember { mutableStateOf("") }
    var showCursor by remember { mutableStateOf(true) }
    LaunchedEffect(text) {
        visibleText = ""
        text.forEachIndexed { index, _ ->
            visibleText = text.substring(0, index + 1)
            delay(typingSpeed)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            showCursor = true
            delay(cursorBlinkSpeed)
            showCursor = false
            delay(cursorBlinkSpeed)
        }
    }

    Text(
        color = color,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier,
        textAlign = textAlign,
        fontSize = fontSize,
        text = if (showCursor) "$visibleText|" else "$visibleText ",
    )
}
