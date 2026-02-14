package com.passdrop.passwordgen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.passdrop.passwordgen.R
import com.passdrop.passwordgen.viewmodel.PasswordViewModel
import kotlin.math.roundToInt

@Composable
fun PasswordOptions(
    viewModel: PasswordViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LengthSection(viewModel)
        Spacer(modifier = Modifier.height(8.dp))
        CharacterTypesSection(viewModel)
    }
}

@Composable
private fun LengthSection(viewModel: PasswordViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.password_length),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = viewModel.lengthText,
                onValueChange = { viewModel.updateLength(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                ),
                isError = viewModel.lengthText.isNotEmpty() && 
                         (viewModel.lengthText.toIntOrNull() ?: 0) !in 5..300
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Slider(
                value = viewModel.length,
                onValueChange = { 
                    viewModel.length = it
                    viewModel.lengthText = it.roundToInt().toString()
                },
                valueRange = 5f..300f,
                steps = 294,
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
                Text("5", color = Color.Gray, fontSize = 12.sp)
                Text("300", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun CharacterTypesSection(viewModel: PasswordViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CheckboxRow(
                text = stringResource(R.string.uppercase),
                checked = viewModel.includeUppercase,
                onCheckedChange = { viewModel.includeUppercase = it }
            )
            
            CheckboxRow(
                text = stringResource(R.string.lowercase),
                checked = viewModel.includeLowercase,
                onCheckedChange = { viewModel.includeLowercase = it }
            )
            
            CheckboxRow(
                text = stringResource(R.string.numbers),
                checked = viewModel.includeNumbers,
                onCheckedChange = { viewModel.includeNumbers = it }
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CheckboxRow(
                    text = stringResource(R.string.special_chars),
                    checked = viewModel.includeSpecialChars,
                    onCheckedChange = { viewModel.includeSpecialChars = it },
                    modifier = Modifier.weight(1f)
                )
                
                if (viewModel.includeSpecialChars) {
                    TextButton(onClick = { viewModel.showSpecialCharDialog = true }) {
                        Text(
                            text = stringResource(
                                R.string.customize_special_count,
                                viewModel.selectedSpecialChars.size
                            ),
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            CheckboxRow(
                text = stringResource(R.string.non_ascii),
                checked = viewModel.includeNonAscii,
                onCheckedChange = { viewModel.includeNonAscii = it }
            )
            
            if (viewModel.includeNonAscii) {
                Text(
                    text = "⚠️ " + stringResource(R.string.non_ascii_warning),
                    fontSize = 11.sp,
                    color = Color(0xFFFF6F00),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.padding(start = 40.dp, top = 4.dp, bottom = 8.dp)
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
            
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
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
