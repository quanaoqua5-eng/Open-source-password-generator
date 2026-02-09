package com.passdrop.passwordgen.ui

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.passdrop.passwordgen.viewmodel.PasswordViewModel

@Composable
fun MainScreen(viewModel: PasswordViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                viewModel.clearPassword()
                try {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                    clipboard?.clearPrimaryClip()
                } catch (e: Exception) {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.clearPassword()
        }
    }
    
    LaunchedEffect(viewModel.isClipboardActive, viewModel.clipboardCountdown) {
        if (viewModel.isClipboardActive && viewModel.clipboardCountdown > 0) {
            kotlinx.coroutines.delay(1000L)
            viewModel.tickCountdown()
        } else if (viewModel.isClipboardActive && viewModel.clipboardCountdown == 0) {
            try {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                clipboard?.clearPrimaryClip()
            } catch (e: Exception) {}
            viewModel.isClipboardActive = false
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection(viewModel)
            
            PasswordOptions(viewModel, modifier = Modifier.padding(vertical = 8.dp))
            
            Button(
                onClick = {
                    viewModel.generatePassword().onFailure { error ->
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
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text(
                    text = stringResource(R.string.generate_button),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            ResultView(viewModel, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
private fun HeaderSection(viewModel: PasswordViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        TextButton(
            onClick = { viewModel.isEnglish = !viewModel.isEnglish },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1976D2))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "🌐", fontSize = 20.sp)
                Text(
                    text = if (viewModel.isEnglish) "EN" else "VI",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
