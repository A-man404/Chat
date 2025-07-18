import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import voidchat.composeapp.generated.resources.JetBrainsMono_bold
import voidchat.composeapp.generated.resources.JetbrainsMono_medium
import voidchat.composeapp.generated.resources.JetbrainsMono_regular
import voidchat.composeapp.generated.resources.Res
import voidchat.composeapp.generated.resources.RobotoMono_bold
import voidchat.composeapp.generated.resources.RobotoMono_medium
import voidchat.composeapp.generated.resources.RobotoMono_regular

@Composable
fun appTypography(): Typography {
    val displayFontFamily = FontFamily(
        Font(Res.font.JetbrainsMono_regular, weight = FontWeight.Normal),
        Font(Res.font.JetbrainsMono_medium, weight = FontWeight.Medium),
        Font(Res.font.JetBrainsMono_bold, weight = FontWeight.Bold)
    )

    val bodyFontFamily = FontFamily(
        Font(Res.font.RobotoMono_regular, weight = FontWeight.Normal),
        Font(Res.font.RobotoMono_medium, weight = FontWeight.Medium),
        Font(Res.font.RobotoMono_bold, weight = FontWeight.Bold)
    )

    return Typography(
        displayLarge = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp
        ),
        displayMedium = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Medium, fontSize = 22.sp
        ),
        displaySmall = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Normal, fontSize = 18.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Medium, fontSize = 18.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp
        ),
        titleLarge = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp
        ),
        titleMedium = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp
        ),
        titleSmall = TextStyle(
            fontFamily = displayFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = bodyFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = bodyFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp
        ),
        bodySmall = TextStyle(
            fontFamily = bodyFontFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp
        ),
        labelLarge = TextStyle(
            fontFamily = bodyFontFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp
        ),
        labelMedium = TextStyle(
            fontFamily = bodyFontFamily, fontWeight = FontWeight.Medium, fontSize = 10.sp
        ),
        labelSmall = TextStyle(
            fontFamily = bodyFontFamily, fontWeight = FontWeight.Medium, fontSize = 8.sp
        ),
    )
}
