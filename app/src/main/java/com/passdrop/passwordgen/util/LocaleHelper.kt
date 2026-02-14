package com.passdrop.passwordgen.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun LocaleWrapper(
    language: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val locale = Locale(language)
    
    val configuration = LocalConfiguration.current
    val newConfig = android.content.res.Configuration(configuration)
    newConfig.setLocale(locale)
    
    val localizedContext = context.createConfigurationContext(newConfig)
    
    CompositionLocalProvider(
        LocalContext provides localizedContext
    ) {
        content()
    }
}
