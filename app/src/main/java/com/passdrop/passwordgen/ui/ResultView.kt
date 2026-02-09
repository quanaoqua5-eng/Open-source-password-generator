package com.passdrop.passwordgen.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.passdrop.passwordgen.R
import com.passdrop.passwordgen.domain.PasswordEntropy
import com.passdrop.passwordgen.util.EntropyCalculator
import com.passdrop.passwordgen.viewmodel.PasswordViewModel
import kotlin.math.roundToInt

@Composable
fun ResultView(
    viewModel: PasswordViewModel,
    modifier: Modifier = Modifier
) {
    if (viewModel.generatedPassword.isEmpty()) return
    
    val context = LocalContext.current
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.generated_password),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
                
                TextButton(
                    onClick = {
                        copyToClipboard(context, viewModel.generatedPassword)
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied_message),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.startClipboardCountdown()
                    }
                ) {
                    Text(
                        text = if (viewModel.isClipboardActive && viewModel.autoClearClipboard)
                            stringResource(R.string.copy_button_countdown, viewModel.clipboardCountdown)
                        else
                            stringResource(R.string.copy_button)
                    )
                }
            }
            
            Text(
                text = viewModel.generatedPassword,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp),
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        R.string.length_chars,
                        viewModel.generatedPassword.length
                    ),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                viewModel.passwordEntropy?.let { entropy ->
                    Text(
                        text = stringResource(
                            R.string.entropy_bits,
                            entropy.value.roundToInt()
                        ),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            viewModel.passwordStats?.let { stats ->
                Spacer(modifier = Modifier.height(4.dp))
                val parts = mutableListOf<String>()
                if (stats.uppercase > 0) parts.add("${stats.uppercase}U")
                if (stats.lowercase > 0) parts.add("${stats.lowercase}L")
                if (stats.numbers > 0) parts.add("${stats.numbers}N")
                if (stats.special > 0) parts.add("${stats.special}S")
                if (stats.nonAscii > 0) parts.add("${stats.nonAscii}A")
                
                Text(
                    text = parts.joinToString(" · "),
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    fontWeight = FontWeight.Medium
                )
            }
            
            viewModel.passwordEntropy?.let { entropy ->
                Spacer(modifier = Modifier.height(8.dp))
                StrengthIndicator(entropy)
            }
            
            if (viewModel.isClipboardActive && viewModel.autoClearClipboard) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.clipboard_will_clear,
                        viewModel.clipboardCountdown
                    ),
                    fontSize = 11.sp,
                    color = Color(0xFFFF6F00),
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.auto_clear_note),
                fontSize = 11.sp,
                color = Color(0xFF1976D2),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
private fun StrengthIndicator(entropy: PasswordEntropy) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.strength_label) + ": ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        
        Text(
            text = entropy.strength.name.lowercase().replaceFirstChar { it.uppercase() },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = entropy.color
        )
    }
    
    LinearProgressIndicator(
        progress = { EntropyCalculator.getProgress(entropy.value) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        color = entropy.color
    )
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("password", text)
    clipboard.setPrimaryClip(clip)
}
