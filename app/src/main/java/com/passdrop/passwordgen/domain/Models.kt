package com.passdrop.passwordgen.domain

import androidx.compose.ui.graphics.Color

enum class GenerationType {
    PASSWORD, PASSPHRASE
}

enum class PassphraseLanguage(val displayName: String) {
    ENGLISH("English"),
    FRENCH("Français"),
    SPANISH("Español"),
    MIXED("Mixed (All)")
}

enum class PassphraseSeparator(val char: String, val display: String) {
    DASH("-", "Dash (-)"),
    UNDERSCORE("_", "Underscore (_)"),
    DOT(".", "Dot (.)"),
    SPACE(" ", "Space ( )"),
    COMMA(",", "Comma (,)"),
    PIPE("|", "Pipe (|)"),
    TILDE("~", "Tilde (~)"),
    HASH("#", "Hash (#)"),
    RANDOM("?", "Random Mix")  // NEW!
}

data class PasswordResult(
    val password: CharArray,
    val poolSize: Int,
    val stats: PasswordStats
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PasswordResult
        return password.contentEquals(other.password)
    }
    
    override fun hashCode(): Int = password.contentHashCode()
    
    fun clear() {
        password.fill('0')
    }
}

data class PasswordStats(
    val uppercase: Int = 0,
    val lowercase: Int = 0,
    val numbers: Int = 0,
    val special: Int = 0,
    val nonAscii: Int = 0
)

data class PassphraseResult(
    val passphrase: CharArray,
    val wordCount: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PassphraseResult
        return passphrase.contentEquals(other.passphrase)
    }
    
    override fun hashCode(): Int = passphrase.contentHashCode()
    
    fun clear() {
        passphrase.fill('0')
    }
}

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
