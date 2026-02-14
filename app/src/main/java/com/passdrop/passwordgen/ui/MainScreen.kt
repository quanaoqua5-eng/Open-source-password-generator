package com.passdrop.passwordgen.ui

import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.passdrop.passwordgen.R
import com.passdrop.passwordgen.domain.GenerationType
import com.passdrop.passwordgen.viewmodel.PasswordViewModel
import java.util.Locale

@Composable
fun MainScreen(viewModel: PasswordViewModel = viewModel()) {
    val baseContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val configuration = remember(viewModel.currentLocale) {
        val locale = Locale(viewModel.currentLocale)
        Locale.setDefault(locale)
        val config = Configuration(baseContext.resources.configuration)
        config.setLocale(locale)
        baseContext.createConfigurationContext(config)
    }
    
    CompositionLocalProvider(LocalContext provides configuration) {
        MainScreenContent(viewModel, baseContext, lifecycleOwner)
    }
}

@Composable
private fun MainScreenContent(
    viewModel: PasswordViewModel,
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                viewModel.clearText(context)
                try {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                    clipboard?.clearPrimaryClip()
                } catch (e: Exception) {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.clearText(context)
        }
    }
    
    LaunchedEffect(viewModel.isClipboardActive, viewModel.clipboardCountdown) {
        if (viewModel.isClipboardActive && viewModel.clipboardCountdown > 0) {
            kotlinx.coroutines.delay(1000L)
            viewModel.tickCountdown(context)
        }
    }
    
    if (viewModel.showSpecialCharDialog) {
        SpecialCharDialog(
            selectedChars = viewModel.selectedSpecialChars,
            allChars = viewModel.allSpecialChars,
            onDismiss = { viewModel.showSpecialCharDialog = false },
            onConfirm = { 
                viewModel.selectedSpecialChars = it
                viewModel.showSpecialCharDialog = false
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            TextButton(onClick = { viewModel.toggleLocale() }) {
                Text(
                    text = if (viewModel.currentLocale == "en") "🌐 EN" else "🌐 VI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = viewModel.generationType == GenerationType.PASSWORD,
                onClick = { viewModel.generationType = GenerationType.PASSWORD },
                label = { Text(stringResource(R.string.password_mode)) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = viewModel.generationType == GenerationType.PASSPHRASE,
                onClick = { viewModel.generationType = GenerationType.PASSPHRASE },
                label = { Text(stringResource(R.string.passphrase_mode)) },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (viewModel.generationType) {
            GenerationType.PASSWORD -> PasswordOptions(viewModel)
            GenerationType.PASSPHRASE -> PassphraseOptions(viewModel)
        }
        
        Button(
            onClick = {
                viewModel.generateText().onFailure { error ->
                    Toast.makeText(
                        context,
                        error.message ?: context.getString(R.string.error_generic),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.generate_button),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (viewModel.generatedText.isNotEmpty()) {
            ResultView(viewModel)
        }
    }
}
