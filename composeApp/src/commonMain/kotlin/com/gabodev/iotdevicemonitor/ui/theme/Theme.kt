package com.gabodev.iotdevicemonitor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Spacing scale (`space-*` tokens, 4px grid). */
object Spacing {
    val xs = 4.dp   // space-1
    val sm = 8.dp   // space-2
    val md = 12.dp  // space-3
    val lg = 16.dp  // space-4
    val xl = 20.dp  // space-5
    val xxl = 24.dp // space-6
    val x3l = 32.dp // space-8
    val x5l = 48.dp // space-12
}

/** Data styles (`reading-*` tokens). Monospaced so digits don't jump on live updates. */
object ReadingStyles {
    val large = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium, fontSize = 32.sp, lineHeight = 40.sp)
    val medium = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium, fontSize = 22.sp, lineHeight = 28.sp)
    val small = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp)
}

private val IotShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),  // radius-xs
    small = RoundedCornerShape(8.dp),       // radius-sm
    medium = RoundedCornerShape(12.dp),     // radius-md
    large = RoundedCornerShape(16.dp),      // radius-lg
    extraLarge = RoundedCornerShape(28.dp), // radius-xl
)

@Composable
fun IotTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalIotColors provides if (darkTheme) DarkIotColors else LightIotColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = Typography(), // Material 3 type scale on the platform's Roboto
            shapes = IotShapes,
            content = content,
        )
    }
}

/** Access point for the extended palette: `IotTheme.colors.success`. */
object IotTheme {
    val colors: IotColors
        @Composable @ReadOnlyComposable get() = LocalIotColors.current
}
