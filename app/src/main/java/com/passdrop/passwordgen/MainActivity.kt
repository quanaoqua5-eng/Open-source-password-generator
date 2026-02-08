package com.passdrop.passwordgen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import java.security.SecureRandom
import kotlin.math.log2
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    
    private var isAppInBackground = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        
        setContent {
            MaterialTheme {
                PasswordGeneratorApp(
                    onAppBackgrounded = {
                        isAppInBackground = true
                    }
                )
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        isAppInBackground = true
    }
    
    override fun onStop() {
        super.onStop()
        isAppInBackground = true
    }
}

data class AppStrings(
    val title: String,
    val passwordLength: String,
    val uppercase: String,
    val lowercase: String,
    val numbers: String,
    val specialChars: String,
    val customizeSpecial: String,
    val selectAll: String,
    val deselectAll: String,
    val nonAscii: String,
    val generateButton: String,
    val generatedPassword: String,
    val copyButton: String,
    val length: String,
    val characters: String,
    val entropy: String,
    val bits: String,
    val strength: String,
    val veryWeak: String,
    val weak: String,
    val moderate: String,
    val strong: String,
    val veryStrong: String,
    val copiedMessage: String,
    val generatedMessage: String,
    val errorLength: String,
    val errorMinLength: String,
    val errorSelectOne: String,
    val errorMemory: String,
    val selectedChars: String,
    val autoClearNote: String,
    val autoClearClipboard: String,
    val clipboardWillClear: String
)

object Strings {
    val en = AppStrings(
        title = "🔐 PassDrop",
        passwordLength = "Password Length (5-300)",
        uppercase = "Uppercase (A-Z)",
        lowercase = "Lowercase (a-z)",
        numbers = "Numbers (0-9)",
        specialChars = "Special Characters",
        customizeSpecial = "Customize...",
        selectAll = "Select All",
        deselectAll = "Deselect All",
        nonAscii = "Non-ASCII (100+ Languages)",
        generateButton = "GENERATE PASSWORD",
        generatedPassword = "Generated Password:",
        copyButton = "📋 Copy",
        length = "Length",
        characters = "characters",
        entropy = "Entropy",
        bits = "bits",
        strength = "Strength",
        veryWeak = "Very Weak",
        weak = "Weak",
        moderate = "Moderate",
        strong = "Strong",
        veryStrong = "Very Strong",
        copiedMessage = "Copied!",
        generatedMessage = "Password generated! Press Copy to save",
        errorLength = "Length must be 5-300 characters",
        errorMinLength = "Minimum length must be at least",
        errorSelectOne = "Please select at least one character type",
        errorMemory = "Error: Length too large, please choose smaller",
        selectedChars = "Selected",
        autoClearNote = "💡 Password will be cleared after copying",
        autoClearClipboard = "Auto-clear clipboard after 60s",
        clipboardWillClear = "Clipboard will clear in"
    )
    
    val vi = AppStrings(
        title = "🔐 PassDrop",
        passwordLength = "Độ Dài Mật Khẩu (5-300)",
        uppercase = "Chữ hoa (A-Z)",
        lowercase = "Chữ thường (a-z)",
        numbers = "Số (0-9)",
        specialChars = "Ký tự đặc biệt",
        customizeSpecial = "Tùy chỉnh...",
        selectAll = "Chọn tất cả",
        deselectAll = "Bỏ chọn tất cả",
        nonAscii = "Ký tự Non-ASCII (100+ Ngôn ngữ)",
        generateButton = "TẠO MẬT KHẨU",
        generatedPassword = "Mật khẩu đã tạo:",
        copyButton = "📋 Copy",
        length = "Độ dài",
        characters = "ký tự",
        entropy = "Độ mạnh",
        bits = "bits",
        strength = "Mức độ",
        veryWeak = "Rất yếu",
        weak = "Yếu",
        moderate = "Trung bình",
        strong = "Mạnh",
        veryStrong = "Rất mạnh",
        copiedMessage = "Đã copy!",
        generatedMessage = "Mật khẩu đã tạo! Nhấn Copy để sao chép",
        errorLength = "Độ dài phải từ 5-300 ký tự",
        errorMinLength = "Độ dài tối thiểu phải là",
        errorSelectOne = "Vui lòng chọn ít nhất một loại ký tự",
        errorMemory = "Lỗi: Độ dài quá lớn, vui lòng chọn số nhỏ hơn",
        selectedChars = "Đã chọn",
        autoClearNote = "💡 Mật khẩu sẽ tự xóa sau khi copy",
        autoClearClipboard = "Tự động xóa clipboard sau 60 giây",
        clipboardWillClear = "Clipboard sẽ xóa sau"
    )
}

data class PasswordEntropy(
    val entropy: Double,
    val strength: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorApp(
    onAppBackgrounded: () -> Unit = {}
) {
    var sliderValue by remember { mutableFloatStateOf(12f) }
    var textFieldValue by remember { mutableStateOf("12") }
    var includeUppercase by remember { mutableStateOf(true) }
    var includeLowercase by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSpecialChars by remember { mutableStateOf(true) }
    var includeNonAscii by remember { mutableStateOf(false) }
    var autoClearClipboard by remember { mutableStateOf(true) }
    var generatedPassword by remember { mutableStateOf("") }
    var passwordEntropy by remember { mutableStateOf<PasswordEntropy?>(null) }
    var isEnglish by remember { mutableStateOf(true) }
    var showSpecialCharDialog by remember { mutableStateOf(false) }
    var clipboardCountdown by remember { mutableIntStateOf(0) }
    var isClipboardActive by remember { mutableStateOf(false) }
    
    val allSpecialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~\\"
    var selectedSpecialChars by remember { 
        mutableStateOf(allSpecialChars.toSet()) 
    }
    
    val strings = if (isEnglish) Strings.en else Strings.vi
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    generatedPassword = ""
                    passwordEntropy = null
                    onAppBackgrounded()
                    
                    try {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                        clipboard?.clearPrimaryClip()
                    } catch (e: Exception) {
                        // Ignore
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    generatedPassword = ""
                    passwordEntropy = null
                }
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            generatedPassword = ""
            passwordEntropy = null
        }
    }

    LaunchedEffect(sliderValue) {
        textFieldValue = sliderValue.roundToInt().toString()
    }

    // Clipboard auto-clear countdown timer
    LaunchedEffect(isClipboardActive, clipboardCountdown) {
        if (isClipboardActive && clipboardCountdown > 0) {
            kotlinx.coroutines.delay(1000L)
            clipboardCountdown -= 1
        } else if (isClipboardActive && clipboardCountdown == 0) {
            // Clear clipboard
            try {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                clipboard?.clearPrimaryClip()
            } catch (e: Exception) {
                // Ignore
            }
            isClipboardActive = false
        }
    }

    if (showSpecialCharDialog) {
        SpecialCharDialog(
            selectedChars = selectedSpecialChars,
            allChars = allSpecialChars,
            strings = strings,
            onDismiss = { showSpecialCharDialog = false },
            onConfirm = { newSelection ->
                selectedSpecialChars = newSelection
                showSpecialCharDialog = false
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.title,
                    fontSize = if (isLandscape) 24.sp else 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                TextButton(
                    onClick = { isEnglish = !isEnglish },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF1976D2)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "🌐",
                            fontSize = 20.sp
                        )
                        Text(
                            text = if (isEnglish) "EN" else "VI",
                            color = Color(0xFF1976D2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = strings.passwordLength,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty()) {
                                textFieldValue = ""
                                sliderValue = 5f
                            } else if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                                textFieldValue = newValue
                                val intValue = newValue.toIntOrNull()
                                if (intValue != null && intValue in 5..300) {
                                    sliderValue = intValue.toFloat()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        ),
                        isError = textFieldValue.isNotEmpty() && 
                                 (textFieldValue.toIntOrNull() ?: 0) !in 5..300
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Slider(
                        value = sliderValue,
                        onValueChange = { 
                            sliderValue = it
                            textFieldValue = it.roundToInt().toString()
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CheckboxOption(
                        text = strings.uppercase,
                        checked = includeUppercase,
                        onCheckedChange = { includeUppercase = it }
                    )

                    CheckboxOption(
                        text = strings.lowercase,
                        checked = includeLowercase,
                        onCheckedChange = { includeLowercase = it }
                    )

                    CheckboxOption(
                        text = strings.numbers,
                        checked = includeNumbers,
                        onCheckedChange = { includeNumbers = it }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CheckboxOption(
                            text = strings.specialChars,
                            checked = includeSpecialChars,
                            onCheckedChange = { includeSpecialChars = it },
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (includeSpecialChars) {
                            TextButton(
                                onClick = { showSpecialCharDialog = true }
                            ) {
                                Text(
                                    text = "${strings.customizeSpecial} (${selectedSpecialChars.size})",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    CheckboxOption(
                        text = strings.nonAscii,
                        checked = includeNonAscii,
                        onCheckedChange = { includeNonAscii = it }
                    )
                    
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    
                    CheckboxOption(
                        text = strings.autoClearClipboard,
                        checked = autoClearClipboard,
                        onCheckedChange = { autoClearClipboard = it }
                    )
                }
            }

            Button(
                onClick = {
                    try {
                        val length = textFieldValue.toIntOrNull() ?: sliderValue.roundToInt()
                        
                        if (length !in 5..300) {
                            Toast.makeText(
                                context,
                                strings.errorLength,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        val selectedOptions = listOf(
                            includeUppercase, includeLowercase, includeNumbers, 
                            includeSpecialChars, includeNonAscii
                        ).count { it }
                        
                        if (selectedOptions == 0) {
                            Toast.makeText(
                                context,
                                strings.errorSelectOne,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        if (length < selectedOptions) {
                            Toast.makeText(
                                context,
                                "${strings.errorMinLength} $selectedOptions",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }
                        
                        val result = generatePasswordSafe(
                            length = length,
                            uppercase = includeUppercase,
                            lowercase = includeLowercase,
                            numbers = includeNumbers,
                            special = includeSpecialChars,
                            specialChars = selectedSpecialChars.joinToString(""),
                            nonAscii = includeNonAscii
                        )
                        
                        generatedPassword = result.password
                        passwordEntropy = calculateEntropy(
                            result.password,
                            result.poolSize,
                            strings
                        )
                        
                        Toast.makeText(
                            context,
                            strings.generatedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: OutOfMemoryError) {
                        Toast.makeText(
                            context,
                            strings.errorMemory,
                            Toast.LENGTH_LONG
                        ).show()
                        generatedPassword = ""
                        passwordEntropy = null
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        generatedPassword = ""
                        passwordEntropy = null
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text(strings.generateButton, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            if (generatedPassword.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = strings.generatedPassword,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )
                            
                            TextButton(
                                onClick = {
                                    try {
                                        copyToClipboardSafe(context, generatedPassword)
                                        Toast.makeText(
                                            context,
                                            strings.copiedMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        
                                        // Start 60-second countdown only if auto-clear is enabled
                                        if (autoClearClipboard) {
                                            clipboardCountdown = 60
                                            isClipboardActive = true
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Text(
                                    text = if (isClipboardActive && autoClearClipboard) 
                                        "${strings.copyButton} (${clipboardCountdown}s)" 
                                    else 
                                        strings.copyButton
                                )
                            }
                        }
                        
                        Text(
                            text = generatedPassword,
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
                                text = "${strings.length}: ${generatedPassword.length} ${strings.characters}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            
                            passwordEntropy?.let { entropy ->
                                Text(
                                    text = "${strings.entropy}: ${entropy.entropy.roundToInt()} ${strings.bits}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        
                        passwordEntropy?.let { entropy ->
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${strings.strength}: ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                                
                                Text(
                                    text = entropy.strength,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = entropy.color
                                )
                            }
                            
                            LinearProgressIndicator(
                                progress = {
                                    when {
                                        entropy.entropy < 40 -> 0.2f
                                        entropy.entropy < 60 -> 0.4f
                                        entropy.entropy < 80 -> 0.6f
                                        entropy.entropy < 100 -> 0.8f
                                        else -> 1.0f
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                color = entropy.color
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (isClipboardActive && autoClearClipboard) {
                            Text(
                                text = "⏰ ${strings.clipboardWillClear} ${clipboardCountdown}s",
                                fontSize = 11.sp,
                                color = Color(0xFFFF6F00),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        
                        Text(
                            text = strings.autoClearNote,
                            fontSize = 11.sp,
                            color = Color(0xFF1976D2),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpecialCharDialog(
    selectedChars: Set<Char>,
    allChars: String,
    strings: AppStrings,
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
                    text = strings.specialChars,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "${strings.selectedChars}: ${currentSelection.size}/${allChars.length}",
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
                        Text(strings.selectAll, fontSize = 12.sp)
                    }
                    
                    OutlinedButton(
                        onClick = { currentSelection = emptySet() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(strings.deselectAll, fontSize = 12.sp)
                    }
                }
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(allChars.toList()) { char ->
                        SpecialCharItem(
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
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = { onConfirm(currentSelection) },
                        modifier = Modifier.weight(1f),
                        enabled = currentSelection.isNotEmpty()
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun SpecialCharItem(
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

@Composable
fun CheckboxOption(
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
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF1976D2)
            )
        )
        Text(
            text = text,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

data class PasswordResult(
    val password: String,
    val poolSize: Int
)

fun generatePasswordSafe(
    length: Int,
    uppercase: Boolean,
    lowercase: Boolean,
    numbers: Boolean,
    special: Boolean,
    specialChars: String,
    nonAscii: Boolean
): PasswordResult {
    if (length < 5 || length > 300) {
        throw IllegalArgumentException("Length must be 5-300")
    }
    
    val runtime = Runtime.getRuntime()
    val maxMemory = runtime.maxMemory()
    val usedMemory = runtime.totalMemory() - runtime.freeMemory()
    val estimatedNeeded = length * 4L
    
    if (usedMemory + estimatedNeeded > maxMemory * 0.9) {
        throw OutOfMemoryError("Insufficient memory")
    }
    
    val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
    val numberChars = "0123456789"
    
    val nonAsciiChars = buildString {
        if (nonAscii) {
            append("ÀÁÂÃÄÅĀĂĄǺȀȂẠẢẤẦẨẪẬẮẰẲẴẶàáâãäåāăąǻȁȃạảấầẩẫậắằẳẵặ")
            append("ÈÉÊËĒĔĖĘĚȄȆẸẺẼỀỂỄỆèéêëēĕėęěȅȇẹẻẽềểễệ")
            append("ÌÍÎÏĨĪĬĮİȈȊỈỊìíîïĩīĭįıȉȋỉị")
            append("ÒÓÔÕÖØŌŎŐƠȌȎỌỎỐỒỔỖỘỚỜỞỠỢòóôõöøōŏőơȍȏọỏốồổỗộớờởỡợ")
            append("ÙÚÛÜŨŪŬŮŰŲƯȔȖỤỦỨỪỬỮỰùúûüũūŭůűųưȕȗụủứừửữự")
            append("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя")
            append("ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω")
            append("אבגדהוזחטיכלמנסעפצקרשת")
            append("أبتثجحخدذرزسشصضطظعغفقكلمنهوي")
            append("あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん")
            append("アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン")
            append("一二三四五六七八九十百千万億兆")
            append("가나다라마바사아자차카타파하한글")
            append("กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ")
        }
    }

    val charPools = mutableListOf<String>()
    if (uppercase) charPools.add(uppercaseChars)
    if (lowercase) charPools.add(lowercaseChars)
    if (numbers) charPools.add(numberChars)
    if (special && specialChars.isNotEmpty()) charPools.add(specialChars)
    if (nonAscii) charPools.add(nonAsciiChars)
    
    if (charPools.isEmpty()) {
        throw IllegalArgumentException("Must select at least one character type")
    }
    
    val allChars = charPools.joinToString("")
    val poolSize = allChars.length
    val secureRandom = SecureRandom()
    val password = StringBuilder(length)
    val usedChars = mutableSetOf<Char>()
    
    charPools.forEach { pool ->
        var attempts = 0
        while (attempts < 100) {
            val char = pool[secureRandom.nextInt(pool.length)]
            if (!usedChars.contains(char)) {
                password.append(char)
                usedChars.add(char)
                break
            }
            attempts++
        }
        if (attempts >= 100) {
            val char = pool[secureRandom.nextInt(pool.length)]
            password.append(char)
            usedChars.add(char)
        }
    }
    
    val remainingLength = length - password.length
    val availableChars = allChars.filter { !usedChars.contains(it) }
    
    if (availableChars.length < remainingLength) {
        for (i in 0 until remainingLength) {
            password.append(allChars[secureRandom.nextInt(allChars.length)])
        }
    } else {
        repeat(remainingLength) {
            val char = availableChars[secureRandom.nextInt(availableChars.length)]
            password.append(char)
            usedChars.add(char)
        }
    }
    
    // Fisher-Yates shuffle algorithm (proper implementation)
    val passwordArray = password.toString().toCharArray()
    for (i in passwordArray.size - 1 downTo 1) {
        val j = secureRandom.nextInt(i + 1)
        val temp = passwordArray[i]
        passwordArray[i] = passwordArray[j]
        passwordArray[j] = temp
    }
    
    return PasswordResult(String(passwordArray), poolSize)
}

fun calculateEntropy(password: String, poolSize: Int, strings: AppStrings): PasswordEntropy {
    val entropy = password.length * log2(poolSize.toDouble())
    
    val (strength, color) = when {
        entropy < 40 -> strings.veryWeak to Color(0xFFD32F2F)
        entropy < 60 -> strings.weak to Color(0xFFFF6F00)
        entropy < 80 -> strings.moderate to Color(0xFFFBC02D)
        entropy < 100 -> strings.strong to Color(0xFF388E3C)
        else -> strings.veryStrong to Color(0xFF1B5E20)
    }
    
    return PasswordEntropy(entropy, strength, color)
}

fun copyToClipboardSafe(context: Context, text: String) {
    try {
        if (text.isEmpty()) {
            throw IllegalArgumentException("No content to copy")
        }
        
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            ?: throw IllegalStateException("Cannot access clipboard")
        
        val clip = ClipData.newPlainText("password", text)
        clipboard.setPrimaryClip(clip)
    } catch (e: Exception) {
        throw Exception("Copy error: ${e.message}")
    }
}
