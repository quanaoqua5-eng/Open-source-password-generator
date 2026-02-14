package com.passdrop.passwordgen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.passdrop.passwordgen.R
import com.passdrop.passwordgen.domain.PassphraseSeparator
import com.passdrop.passwordgen.domain.PassphraseLanguage
import com.passdrop.passwordgen.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.separator_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            var expanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = viewModel.selectedSeparator.display,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.separator_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    PassphraseSeparator.values().forEach { separator ->
                        DropdownMenuItem(
                            text = { Text(separator.display) },
                            onClick = {
                                viewModel.selectedSeparator = separator
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.language_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            var languageExpanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = it }
            ) {
                OutlinedTextField(
                    value = viewModel.selectedLanguage.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.language_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(languageExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    PassphraseLanguage.values().forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.displayName) },
                            onClick = {
                                viewModel.selectedLanguage = language
                                languageExpanded = false
                            }
                        )
                    }
                }
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
