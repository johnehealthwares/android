package com.rxsoft.mobile.ui.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.rxsoft.mobile.ui.designsystem.token.ColorTokens

private val LightColorScheme = lightColorScheme(
    primary = ColorTokens.primaryLight,
    onPrimary = ColorTokens.onPrimaryLight,
    primaryContainer = ColorTokens.primaryContainerLight,
    onPrimaryContainer = ColorTokens.onPrimaryContainerLight,
    secondary = ColorTokens.secondaryLight,
    onSecondary = ColorTokens.onSecondaryLight,
    secondaryContainer = ColorTokens.secondaryContainerLight,
    onSecondaryContainer = ColorTokens.onSecondaryContainerLight,
    tertiary = ColorTokens.tertiaryLight,
    onTertiary = ColorTokens.onTertiaryLight,
    tertiaryContainer = ColorTokens.tertiaryContainerLight,
    onTertiaryContainer = ColorTokens.onTertiaryContainerLight,
    error = ColorTokens.errorLight,
    onError = ColorTokens.onErrorLight,
    errorContainer = ColorTokens.errorContainerLight,
    onErrorContainer = ColorTokens.onErrorContainerLight,
    background = ColorTokens.backgroundLight,
    onBackground = ColorTokens.onBackgroundLight,
    surface = ColorTokens.surfaceLight,
    onSurface = ColorTokens.onSurfaceLight,
    surfaceVariant = ColorTokens.surfaceVariantLight,
    onSurfaceVariant = ColorTokens.onSurfaceVariantLight,
    outline = ColorTokens.outlineLight,
    outlineVariant = ColorTokens.outlineVariantLight,
    inverseSurface = ColorTokens.inverseSurfaceLight,
    inverseOnSurface = ColorTokens.inverseOnSurfaceLight,
    inversePrimary = ColorTokens.inversePrimaryLight,
    scrim = ColorTokens.scrimLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorTokens.primaryDark,
    onPrimary = ColorTokens.onPrimaryDark,
    primaryContainer = ColorTokens.primaryContainerDark,
    onPrimaryContainer = ColorTokens.onPrimaryContainerDark,
    secondary = ColorTokens.secondaryDark,
    onSecondary = ColorTokens.onSecondaryDark,
    secondaryContainer = ColorTokens.secondaryContainerDark,
    onSecondaryContainer = ColorTokens.onSecondaryContainerDark,
    tertiary = ColorTokens.tertiaryDark,
    onTertiary = ColorTokens.onTertiaryDark,
    tertiaryContainer = ColorTokens.tertiaryContainerDark,
    onTertiaryContainer = ColorTokens.onTertiaryContainerDark,
    error = ColorTokens.errorDark,
    onError = ColorTokens.onErrorDark,
    errorContainer = ColorTokens.errorContainerDark,
    onErrorContainer = ColorTokens.onErrorContainerDark,
    background = ColorTokens.backgroundDark,
    onBackground = ColorTokens.onBackgroundDark,
    surface = ColorTokens.surfaceDark,
    onSurface = ColorTokens.onSurfaceDark,
    surfaceVariant = ColorTokens.surfaceVariantDark,
    onSurfaceVariant = ColorTokens.onSurfaceVariantDark,
    outline = ColorTokens.outlineDark,
    outlineVariant = ColorTokens.outlineVariantDark,
    inverseSurface = ColorTokens.inverseSurfaceDark,
    inverseOnSurface = ColorTokens.inverseOnSurfaceDark,
    inversePrimary = ColorTokens.inversePrimaryDark,
    scrim = ColorTokens.scrimDark,
)

@Composable
fun RxSoftMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
