package com.passdrop.passwordgen.domain

import java.security.SecureRandom

object PasswordGenerator {
    
    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBERS = "0123456789"
    private const val NON_ASCII = "ÀÁÂÃÄÅĀĂĄǺȀȂẠẢẤẦẨẪẬẮẰẲẴẶàáâãäåāăąǻȁȃạảấầẩẫậắằẳẵặÈÉÊËĒĔĖĘĚȄȆẸẺẼỀỂỄỆèéêëēĕėęěȅȇẹẻẽềểễệÌÍÎÏĨĪĬĮİȈȊỈỊìíîïĩīĭįıȉȋỉịÒÓÔÕÖØŌŎŐƠȌȎỌỎỐỒỔỖỘỚỜỞỠỢòóôõöøōŏőơȍȏọỏốồổỗộớờởỡợÙÚÛÜŨŪŬŮŰŲƯȔȖỤỦỨỪỬỮỰùúûüũūŭůűųưȕȗụủứừửữựАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψωאבגדהוזחטיכלמנסעפצקרשתأبتثجحخدذرزسشصضطظعغفقكلمنهويあいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン一二三四五六七八九十百千万億兆가나다라마바사아자차카타파하한글กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ"
    
    fun generate(config: PasswordConfig): PasswordResult {
        val pools = buildPools(config)
        require(pools.isNotEmpty()) { "Must select at least one character type" }
        
        val allChars = pools.joinToString("")
        val random = SecureRandom()
        val chars = mutableListOf<Char>()
        
        for (pool in pools.shuffled(random)) {
            chars.add(pool[random.nextInt(pool.length)])
        }
        
        repeat(config.length - chars.size) {
            chars.add(allChars[random.nextInt(allChars.length)])
        }
        
        for (i in chars.size - 1 downTo 1) {
            val j = random.nextInt(i + 1)
            val temp = chars[i]
            chars[i] = chars[j]
            chars[j] = temp
        }
        
        val password = String(chars.toCharArray())
        val stats = calculateStats(password, config.selectedSpecialChars)
        
        return PasswordResult(password, allChars.length, stats)
    }
    
    private fun buildPools(config: PasswordConfig): List<String> {
        val pools = mutableListOf<String>()
        if (config.includeUppercase) pools.add(UPPERCASE)
        if (config.includeLowercase) pools.add(LOWERCASE)
        if (config.includeNumbers) pools.add(NUMBERS)
        if (config.includeSpecialChars && config.selectedSpecialChars.isNotEmpty()) {
            pools.add(config.selectedSpecialChars.joinToString(""))
        }
        if (config.includeNonAscii) pools.add(NON_ASCII)
        return pools
    }
    
    private fun calculateStats(password: String, specialChars: Set<Char>): PasswordStats {
        var upper = 0
        var lower = 0
        var nums = 0
        var special = 0
        var nonAscii = 0
        
        for (c in password) {
            when {
                c in UPPERCASE -> upper++
                c in LOWERCASE -> lower++
                c in NUMBERS -> nums++
                c in specialChars -> special++
                c.code > 127 -> nonAscii++
            }
        }
        
        return PasswordStats(upper, lower, nums, special, nonAscii)
    }
}
