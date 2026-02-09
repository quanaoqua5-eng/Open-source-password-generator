package com.passdrop.passwordgen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.passdrop.passwordgen.domain.PasswordConfig
import com.passdrop.passwordgen.domain.PasswordEntropy
import com.passdrop.passwordgen.domain.PasswordGenerator
import com.passdrop.passwordgen.domain.PasswordStats
import com.passdrop.passwordgen.util.EntropyCalculator

class PasswordViewModel : ViewModel() {
    
    var length by mutableFloatStateOf(12f)
    var lengthText by mutableStateOf("12")
    
    var includeUppercase by mutableStateOf(true)
    var includeLowercase by mutableStateOf(true)
    var includeNumbers by mutableStateOf(true)
    var includeSpecialChars by mutableStateOf(true)
    var includeNonAscii by mutableStateOf(false)
    var autoClearClipboard by mutableStateOf(true)
    
    val allSpecialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~\\"
    var selectedSpecialChars by mutableStateOf(allSpecialChars.toSet())
    
    var generatedPassword by mutableStateOf("")
        private set
    
    var passwordEntropy by mutableStateOf<PasswordEntropy?>(null)
        private set
    
    var passwordStats by mutableStateOf<PasswordStats?>(null)
        private set
    
    var clipboardCountdown by mutableIntStateOf(0)
    var isClipboardActive by mutableStateOf(false)
    
    var showSpecialCharDialog by mutableStateOf(false)
    var isEnglish by mutableStateOf(true)
    
    fun updateLength(newLength: String) {
        if (newLength.isEmpty()) {
            lengthText = ""
            length = 5f
            return
        }
        if (newLength.all { it.isDigit() } && newLength.length <= 3) {
            lengthText = newLength
            newLength.toIntOrNull()?.let {
                if (it in 5..300) length = it.toFloat()
            }
        }
    }
    
    fun generatePassword(): Result<Unit> = runCatching {
        val len = lengthText.toIntOrNull()?.coerceIn(5, 300) ?: length.toInt()
        
        val selectedTypes = listOf(
            includeUppercase, includeLowercase, includeNumbers,
            includeSpecialChars, includeNonAscii
        ).count { it }
        
        require(selectedTypes > 0) { "Please select at least one character type" }
        require(len >= selectedTypes) { "Minimum length must be at least $selectedTypes" }
        
        val config = PasswordConfig(
            length = len,
            includeUppercase = includeUppercase,
            includeLowercase = includeLowercase,
            includeNumbers = includeNumbers,
            includeSpecialChars = includeSpecialChars,
            selectedSpecialChars = selectedSpecialChars,
            includeNonAscii = includeNonAscii
        )
        
        val result = PasswordGenerator.generate(config)
        generatedPassword = result.password
        passwordEntropy = EntropyCalculator.calculate(result.password)
        passwordStats = result.stats
        
        isClipboardActive = false
        clipboardCountdown = 0
    }
    
    fun startClipboardCountdown() {
        if (autoClearClipboard && !isClipboardActive) {
            clipboardCountdown = 60
            isClipboardActive = true
        }
    }
    
    fun tickCountdown() {
        if (clipboardCountdown > 0) {
            clipboardCountdown--
        } else if (isClipboardActive) {
            isClipboardActive = false
        }
    }
    
    fun clearPassword() {
        generatedPassword = ""
        passwordEntropy = null
        passwordStats = null
    }
}
