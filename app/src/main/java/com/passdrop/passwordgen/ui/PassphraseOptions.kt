package com.passdrop.passwordgen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.passdrop.passwordgen.R
import com.passdrop.passwordgen.viewmodel.PasswordViewModel

@Composable
fun PassphraseOptions(viewModel: PasswordViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.passphrase_words),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = stringResource(R.string.word_count, viewModel.passphraseWordCount),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Slider(
                value = viewModel.passphraseWordCount.toFloat(),
                onValueChange = { viewModel.passphraseWordCount = it.toInt() },
                valueRange = 3f..10f,
                steps = 6,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF1976D2),
                    activeTrackColor = Color(0xFF1976D2),
                    inactiveTrackColor = Color(0xFFBBDEFB)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("3", color = Color.Gray, fontSize = 12.sp)
                Text("10", color = Color.Gray, fontSize = 12.sp)
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray)
            
            CheckboxRow(
                text = stringResource(R.string.auto_clear_clipboard),
                checked = viewModel.autoClearClipboard,
                onCheckedChange = { viewModel.autoClearClipboard = it }
            )
            
            CheckboxRow(
                text = stringResource(R.string.hide_by_default),
                checked = !viewModel.isPasswordVisible,
                onCheckedChange = { viewModel.isPasswordVisible = !it }
            )
        }
    }
}

@Composable
private fun CheckboxRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1976D2))
        )
        Text(
            text = text,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
