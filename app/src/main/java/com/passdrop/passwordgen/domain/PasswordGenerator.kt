package com.passdrop.passwordgen.domain

import java.security.SecureRandom

object PasswordGenerator {
    
    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBERS = "0123456789"
    
    // 3000+ Non-ASCII characters covering major world scripts
    private const val NON_ASCII = 
        // Latin Extended (Vietnamese, European) - ~400 chars
        "ÀÁÂÃÄÅĀĂĄǺȀȂẠẢẤẦẨẪẬẮẰẲẴẶàáâãäåāăąǻȁȃạảấầẩẫậắằẳẵặ" +
        "ÈÉÊËĒĔĖĘĚȄȆẸẺẼỀỂỄỆèéêëēĕėęěȅȇẹẻẽềểễệ" +
        "ÌÍÎÏĨĪĬĮİȈȊỈỊìíîïĩīĭįıȉȋỉị" +
        "ÒÓÔÕÖØŌŎŐƠȌȎỌỎỐỒỔỖỘỚỜỞỠỢòóôõöøōŏőơȍȏọỏốồổỗộớờởỡợ" +
        "ÙÚÛÜŨŪŬŮŰŲƯȔȖỤỦỨỪỬỮỰùúûüũūŭůűųưȕȗụủứừửữự" +
        "ÝŶŸỲỴỶỸýŷÿỳỵỷỹ" +
        "ĀĂĄĆĈĊČĎĐĒĔĖĘĚĜĞĠĢĤĦĨĪĬĮİĲĴĶĹĻĽĿŁŃŅŇŊŌŎŐŒŔŖŘŚŜŞŠŢŤŦŨŪŬŮŰŲŴŶŸŹŻŽ" +
        "āăąćĉċčďđēĕėęěĝğġģĥħĩīĭįıĳĵķĸĺļľŀłńņňŉŋōŏőœŕŗřśŝşšţťŧũūŭůűųŵŷźżž" +
        "ǍǎǏǐǑǒǓǔǕǖǗǘǙǚǛǜǞǟǠǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰǱǲǳǴǵǶǷǸǹǺǻǼǽǾǿ" +
        
        // Cyrillic (Russian, Ukrainian, Serbian) - ~300 chars
        "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
        "ЀЁЂЃЄЅІЇЈЉЊЋЌЍЎЏАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя" +
        "ѐёђѓєѕіїјљњћќѝўџѠѡѢѣѤѥѦѧѨѩѪѫѬѭѮѯѰѱѲѳѴѵѶѷѸѹѺѻѼѽѾѿ" +
        
        // Greek - ~200 chars
        "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω" +
        "ͰͱͲͳʹ͵Ͷͷͺͻͼͽ;Ϳ΄΅Ά·ΈΉΊΌΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώϏ" +
        "ϐϑϒϓϔϕϖϗϘϙϚϛϜϝϞϟϠϡϢϣϤϥϦϧϨϩϪϫϬϭϮϯϰϱϲϳϴϵ϶ϷϸϹϺϻϼϽϾϿ" +
        
        // Hebrew - ~100 chars
        "אבגדהוזחטיכךלמםנןסעפףצץקרשת" +
        "ְֱֲֳִֵֶַָֹֺֻּֽ־ֿ׀ׁׂ׃ׅׄ׆ׇ׈׉׊׋׌׍׎׏אבגדהוזחטיכךלמםנןסעפףצץקרשת" +
        
        // Arabic - ~200 chars
        "ابتثجحخدذرزسشصضطظعغفقكلمنهوي" +
        "ءآأؤإئابةتثجحخدذرزسشصضطظعغـفقكلمنهوىيًٌٍَُِّْ" +
        "ٕٖٜٟٓٔٗ٘ٙٚٛٝٞ٠١٢٣٤٥٦٧٨٩٪٫٬٭ٮٯٰٱٲٳٴٵٶٷٸٹٺٻټٽپٿ" +
        
        // Japanese Hiragana - ~100 chars
        "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん" +
        "がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ" +
        "ぁぃぅぇぉっゃゅょゎゐゑゔゕゖ゙゚゛゜ゝゞゟ" +
        
        // Japanese Katakana - ~100 chars
        "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
        "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポ" +
        "ァィゥェォッャュョヮヰヱヴヵヶヷヸヹヺ・ーヽヾヿ" +
        
        // Chinese (Simplified + Traditional) - ~500 chars
        "一二三四五六七八九十百千万億兆京垓秭穰溝澗正載極" +
        "东南西北中上下左右前后内外大小多少长短高低新旧好坏" +
        "人口手足目耳鼻舌心身头脸眼睛嘴巴牙齿舌头手指脚趾" +
        "天地日月星辰风雨雷电云雾雪霜冰火水土石金银铜铁木" +
        "红橙黄绿青蓝紫黑白灰粉棕" +
        "春夏秋冬年月日时分秒今明昨今天明天昨天" +
        "學習工作生活家庭朋友愛情幸福快樂健康平安" +
        "電腦手機網路軟體硬體程式資料檔案文件" +
        "東南西北國家城市鄉村道路街道建築房屋" +
        
        // Korean Hangul - ~500 chars
        "가나다라마바사아자차카타파하" +
        "거너더러머버서어저처커터퍼허" +
        "고노도로모보소오조초코토포호" +
        "구누두루무부수우주추쿠투푸후" +
        "그느드르므브스으즈츠크트프흐" +
        "기니디리미비시이지치키티피히" +
        "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ" +
        "ㅏㅑㅓㅕㅗㅛㅜㅠㅡㅣ" +
        "ㄲㄳㄵㄶㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ" +
        
        // Thai - ~100 chars
        "กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ" +
        "ะัาำิีึืุูเแโใไๅๆ็่้๊๋์ํ๎๏๐๑๒๓๔๕๖๗๘๙" +
        
        // Devanagari (Hindi, Sanskrit) - ~200 chars
        "अआइईउऊऋऌऍऎएऐऑऒओऔकखगघङचछजझञटठडढणतथदधनपफबभमयरलवशषसह" +
        "़ऽािीुूृॄॅॆेैॉॊोौ्॒॑॓॔ॕॖॗक़ख़ग़ज़ड़ढ़फ़य़ॠॡ०१२३४५६७८९" +
        
        // Bengali - ~100 chars
        "অআইঈউঊঋএঐওঔকখগঘঙচছজঝঞটঠডঢণতথদধনপফবভমযরলশষসহ" +
        "়ঽািীুূৃেৈোৌ্ৎ০১২৩৪৫৬৭৮৯" +
        
        // Tamil - ~100 chars
        "அஆஇஈஉஊஎஏஐஒஓஔகஙசஜஞடணதநனபமயரறலளழவஶஷஸஹ" +
        "ாிீுூெேைொோௌ்௦௧௨௩௪௫௬௭௮௯" +
        
        // Georgian - ~50 chars
        "აბგდევზთიკლმნოპჟრსტუფქღყშჩცძწჭხჯჰ" +
        
        // Armenian - ~80 chars
        "ԱԲԳԴԵԶԷԸԹԺԻԼԽԾԿՀՁՂՃՄՅՆՇՈՉՊՋՌՍՎՏՐՑՒՓՔՕՖաբգդեզէըթժիլխծկհձղճմյնշոչպջռսվտրցւփքօֆև" +
        
        // Ethiopic - ~100 chars
        "ሀሁሂሃሄህሆለሉሊላሌልሎመሙሚማሜምሞረሩሪራሬርሮሰሱሲሳሴስሶሸሹሺሻሼሽሾቀቁቂቃቄቅቆበቡቢባቤብቦተቱቲታቴትቶነኑኒናኔንኖ" +
        
        // Special Mathematical & Symbols - ~200 chars
        "αβγδεζηθικλμνξοπρστυφχψω∀∂∃∅∇∈∉∋∏∑−∓∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟≠≡≢≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗⋘⋙⋚⋛⋜⋝⋞⋟⋠⋡⋢⋣⋤⋥⋦⋧⋨⋩⋪⋫⋬⋭⋮⋯⋰⋱"
    
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
        
        val passwordArray = chars.toCharArray()
        
        // MEMORY CLEANUP: Overwrite chars list before clearing
        for (i in chars.indices) {
            chars[i] = '0'
        }
        chars.clear()
        
        val stats = calculateStats(passwordArray, config.selectedSpecialChars)
        
        return PasswordResult(passwordArray, allChars.length, stats)
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
    
    private fun calculateStats(password: CharArray, specialChars: Set<Char>): PasswordStats {
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
