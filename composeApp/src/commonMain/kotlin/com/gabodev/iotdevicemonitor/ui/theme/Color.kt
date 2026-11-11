package com.gabodev.iotdevicemonitor.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Values mirror tokens.json of the "IoT Device Monitor" design system (Material 3, seed #0e7c86).

internal val LightColors = lightColorScheme(
    primary = Color(0xFF00687A), onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFABEDFF), onPrimaryContainer = Color(0xFF001F26),
    secondary = Color(0xFF4B6269), onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCEE7EF), onSecondaryContainer = Color(0xFF061F24),
    tertiary = Color(0xFF565D7E), onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDDE1FF), onTertiaryContainer = Color(0xFF131A37),
    error = Color(0xFFBA1A1A), onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6), onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF5FAFC), onBackground = Color(0xFF171C1E),
    surface = Color(0xFFF5FAFC), onSurface = Color(0xFF171C1E),
    surfaceVariant = Color(0xFFDBE4E7), onSurfaceVariant = Color(0xFF3F484B),
    surfaceContainerLowest = Color(0xFFFFFFFF), surfaceContainerLow = Color(0xFFEFF4F6),
    surfaceContainer = Color(0xFFE9EFF1), surfaceContainerHigh = Color(0xFFE3E9EB),
    surfaceContainerHighest = Color(0xFFDEE3E5),
    outline = Color(0xFF6F797C), outlineVariant = Color(0xFFBFC8CB),
    inverseSurface = Color(0xFF2C3133), inverseOnSurface = Color(0xFFECF2F4),
    inversePrimary = Color(0xFF84D2E7), scrim = Color(0xFF000000),
)

internal val DarkColors = darkColorScheme(
    primary = Color(0xFF84D2E7), onPrimary = Color(0xFF003640),
    primaryContainer = Color(0xFF004E5C), onPrimaryContainer = Color(0xFFABEDFF),
    secondary = Color(0xFFB2CBD3), onSecondary = Color(0xFF1D343A),
    secondaryContainer = Color(0xFF334A51), onSecondaryContainer = Color(0xFFCEE7EF),
    tertiary = Color(0xFFBEC4EB), onTertiary = Color(0xFF283050),
    tertiaryContainer = Color(0xFF3E4565), onTertiaryContainer = Color(0xFFDDE1FF),
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A), onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0F1416), onBackground = Color(0xFFDEE3E5),
    surface = Color(0xFF0F1416), onSurface = Color(0xFFDEE3E5),
    surfaceVariant = Color(0xFF3F484B), onSurfaceVariant = Color(0xFFBFC8CB),
    surfaceContainerLowest = Color(0xFF0A0F11), surfaceContainerLow = Color(0xFF171C1E),
    surfaceContainer = Color(0xFF1B2022), surfaceContainerHigh = Color(0xFF252B2C),
    surfaceContainerHighest = Color(0xFF303637),
    outline = Color(0xFF899295), outlineVariant = Color(0xFF3F484B),
    inverseSurface = Color(0xFFDEE3E5), inverseOnSurface = Color(0xFF2C3133),
    inversePrimary = Color(0xFF00687A), scrim = Color(0xFF000000),
)

/** Status and sensor colors: not Material roles, exposed through [LocalIotColors]. */
@Immutable
data class IotColors(
    val success: Color, val onSuccess: Color, val successContainer: Color, val onSuccessContainer: Color,
    val warning: Color, val onWarning: Color, val warningContainer: Color, val onWarningContainer: Color,
    val sensorTemperature: Color, val sensorTemperatureContainer: Color,
    val sensorHumidity: Color, val sensorHumidityContainer: Color,
    val sensorPressure: Color, val sensorAir: Color,
)

internal val LightIotColors = IotColors(
    success = Color(0xFF1E6C33), onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFA6F5B4), onSuccessContainer = Color(0xFF002109),
    warning = Color(0xFF7A5900), onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFDEA2), onWarningContainer = Color(0xFF261900),
    sensorTemperature = Color(0xFF9C4400), sensorTemperatureContainer = Color(0xFFFFDBC9),
    sensorHumidity = Color(0xFF00639B), sensorHumidityContainer = Color(0xFFCEE5FF),
    sensorPressure = LightColors.tertiary, sensorAir = Color(0xFF1E6C33),
)

internal val DarkIotColors = IotColors(
    success = Color(0xFF8BD89A), onSuccess = Color(0xFF00391A),
    successContainer = Color(0xFF005321), onSuccessContainer = Color(0xFFA6F5B4),
    warning = Color(0xFFF5BF48), onWarning = Color(0xFF402D00),
    warningContainer = Color(0xFF5C4300), onWarningContainer = Color(0xFFFFDEA2),
    sensorTemperature = Color(0xFFFFB68E), sensorTemperatureContainer = Color(0xFF773300),
    sensorHumidity = Color(0xFF96CCFF), sensorHumidityContainer = Color(0xFF004A76),
    sensorPressure = DarkColors.tertiary, sensorAir = Color(0xFF8BD89A),
)

val LocalIotColors = staticCompositionLocalOf { LightIotColors }
