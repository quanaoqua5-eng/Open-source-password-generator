package com.passdrop.passwordgen.util

import androidx.compose.ui.graphics.Color
import com.passdrop.passwordgen.domain.PasswordEntropy
import com.passdrop.passwordgen.domain.StrengthLevel
import kotlin.math.log2

object EntropyCalculator {
    
    fun calculate(text: String): PasswordEntropy {
        val entropy = calculateShannonEntropy(text)
        val level = when {
            entropy < 40 -> StrengthLevel.VERY_WEAK
            entropy < 60 -> StrengthLevel.WEAK
            entropy < 80 -> StrengthLevel.MODERATE
            entropy < 100 -> StrengthLevel.STRONG
            else -> StrengthLevel.VERY_STRONG
        }
        val color = when (level) {
            StrengthLevel.VERY_WEAK -> Color(0xFFD32F2F)
            StrengthLevel.WEAK -> Color(0xFFFF6F00)
            StrengthLevel.MODERATE -> Color(0xFFFBC02D)
            StrengthLevel.STRONG -> Color(0xFF388E3C)
            StrengthLevel.VERY_STRONG -> Color(0xFF1B5E20)
        }
        return PasswordEntropy(entropy, level, color)
    }
    
    private fun calculateShannonEntropy(text: String): Double {
        if (text.isEmpty()) return 0.0
        
        val freq = mutableMapOf<Char, Int>()
        for (c in text) {
            freq[c] = freq.getOrDefault(c, 0) + 1
        }
        
        var entropy = 0.0
        val len = text.length.toDouble()
        
        for (count in freq.values) {
            val p = count / len
            entropy -= p * log2(p)
        }
        
        return entropy * text.length
    }
    
    fun getProgress(entropy: Double): Float = when {
        entropy < 40 -> 0.2f
        entropy < 60 -> 0.4f
        entropy < 80 -> 0.6f
        entropy < 100 -> 0.8f
        else -> 1.0f
    }
}
