package com.passdrop.passwordgen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.passdrop.passwordgen.R

@Composable
fun SpecialCharDialog(
    selectedChars: Set<Char>,
    allChars: String,
    onDismiss: () -> Unit,
    onConfirm: (Set<Char>) -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedChars) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.special_chars),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = stringResource(
                        R.string.selected_chars,
                        currentSelection.size,
                        allChars.length
                    ),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { currentSelection = allChars.toSet() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.select_all), fontSize = 12.sp)
                    }
                    
                    OutlinedButton(
                        onClick = { currentSelection = emptySet() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.deselect_all), fontSize = 12.sp)
                    }
                }
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(allChars.toList()) { char ->
                        CharItem(
                            char = char,
                            isSelected = currentSelection.contains(char),
                            onToggle = {
                                currentSelection = if (currentSelection.contains(char)) {
                                    currentSelection - char
                                } else {
                                    currentSelection + char
                                }
                            }
                        )
                    }
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.dialog_cancel))
                    }
                    
                    Button(
                        onClick = { onConfirm(currentSelection) },
                        modifier = Modifier.weight(1f),
                        enabled = currentSelection.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.dialog_ok))
                    }
                }
            }
        }
    }
}

@Composable
private fun CharItem(
    char: Char,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = if (isSelected) Color(0xFF1976D2) else Color(0xFFE0E0E0),
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}
