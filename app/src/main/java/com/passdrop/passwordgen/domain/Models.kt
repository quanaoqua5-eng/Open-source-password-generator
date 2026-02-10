package com.passdrop.passwordgen.domain

import androidx.compose.ui.graphics.Color

enum class GenerationType {
    PASSWORD, PASSPHRASE
}

data class PasswordResult(
    val password: String,
    val poolSize: Int,
    val stats: PasswordStats
)

data class PasswordStats(
    val uppercase: Int = 0,
    val lowercase: Int = 0,
    val numbers: Int = 0,
    val special: Int = 0,
    val nonAscii: Int = 0
)

data class PassphraseResult(
    val passphrase: String,
    val wordCount: Int
)

data class PasswordConfig(
    val length: Int,
    val includeUppercase: Boolean,
    val includeLowercase: Boolean,
    val includeNumbers: Boolean,
    val includeSpecialChars: Boolean,
    val selectedSpecialChars: Set<Char>,
    val includeNonAscii: Boolean
)

data class PasswordEntropy(
    val value: Double,
    val strength: StrengthLevel,
    val color: Color
)

enum class StrengthLevel {
    VERY_WEAK, WEAK, MODERATE, STRONG, VERY_STRONG
}
