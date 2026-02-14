package com.passdrop.passwordgen.util

import androidx.compose.ui.graphics.Color
import com.passdrop.passwordgen.domain.PasswordEntropy
import com.passdrop.passwordgen.domain.StrengthLevel
import kotlin.math.log2

object EntropyCalculator {
    
    /**
     * Tính Entropy dựa trên công thức: E = L * log2(R)
     * L: Độ dài mật khẩu (hoặc số từ trong passphrase)
     * R: Kích thước tập ký tự (hoặc số lượng từ trong từ điển)
     */
    fun calculate(length: Int, poolSize: Int): PasswordEntropy {
        if (length == 0 || poolSize <= 1) {
            return PasswordEntropy(0.0, StrengthLevel.VERY_WEAK, Color(0xFFD32F2F))
        }

        // Công thức chuẩn an ninh mật mã
        val entropy = length * log2(poolSize.toDouble())
        
        // Ngưỡng đánh giá (Bits of Entropy)
        val level = when {
            entropy < 30 -> StrengthLevel.VERY_WEAK
            entropy < 50 -> StrengthLevel.WEAK
            entropy < 70 -> StrengthLevel.MODERATE
            entropy < 100 -> StrengthLevel.STRONG
            else -> StrengthLevel.VERY_STRONG
        }
        
        val color = when (level) {
            StrengthLevel.VERY_WEAK -> Color(0xFFD32F2F)   // Đỏ
            StrengthLevel.WEAK -> Color(0xFFFF6F00)        // Cam
            StrengthLevel.MODERATE -> Color(0xFFFBC02D)    // Vàng
            StrengthLevel.STRONG -> Color(0xFF388E3C)      // Xanh lá
            StrengthLevel.VERY_STRONG -> Color(0xFF1B5E20) // Xanh đậm
        }
        
        return PasswordEntropy(entropy, level, color)
    }

    fun getProgress(entropy: Double): Float {
        // Giả định 128 bits là tối đa cho thanh tiến trình (rất an toàn)
        return (entropy / 128.0).coerceIn(0.0, 1.0).toFloat()
    }
}
