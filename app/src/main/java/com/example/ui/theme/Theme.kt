package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NuggetGold,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF4A3400),
    onPrimaryContainer = NuggetGoldLight,
    secondary = CodexPurpleLight,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3C1460),
    onSecondaryContainer = Color(0xFFE9D5FF),
    tertiary = ShameCrimson,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF5C0E1B),
    onTertiaryContainer = Color(0xFFFFD8DE),
    background = DarkObsidian,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = NuggetGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECB3),
    onPrimaryContainer = Color(0xFF3E2723),
    secondary = CodexPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE7F6),
    onSecondaryContainer = Color(0xFF311B92),
    tertiary = ShameCrimson,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFEBEE),
    onTertiaryContainer = Color(0xFFB71C1C),
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFD7CCC8)
)

@Composable
fun TwatNuggetTheme(
    darkTheme: Boolean = true, // Default to Late Night dark mode
    dynamicColor: Boolean = false, // Preserve branded gold & crimson theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
