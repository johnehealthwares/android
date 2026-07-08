package com.rxsoft.mobile.ui.theme

import androidx.compose.runtime.Composable
import com.rxsoft.mobile.ui.designsystem.theme.RxSoftMobileTheme

@Composable
fun RxSoftMobileTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    RxSoftMobileTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}
