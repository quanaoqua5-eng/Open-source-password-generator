package com.passdrop.passwordgen.domain

import java.security.SecureRandom

object PassphraseGenerator {
    
    // English wordlist - 256 common words (8 bits/word)
    private val ENGLISH_WORDS = listOf(
        "abacus", "ability", "able", "about", "above", "abroad", "absence", "absolute",
        "abstract", "academic", "accept", "access", "account", "achieve", "acid", "acquire",
        "across", "action", "active", "actual", "adapt", "added", "address", "adjust",
        "admit", "adopt", "adult", "advance", "advice", "affair", "affect", "afford",
        "afraid", "after", "again", "against", "agency", "agenda", "agent", "agree",
        "ahead", "airport", "alarm", "album", "alcohol", "alert", "alien", "alive",
        "allow", "almost", "alone", "along", "already", "also", "alter", "always",
        "amazing", "among", "amount", "ancient", "android", "angel", "anger", "angle",
        "animal", "ankle", "announce", "annual", "another", "answer", "anxiety", "anybody",
        "anyone", "anything", "anyway", "anywhere", "apart", "apologize", "apparent", "appeal",
        "appear", "apple", "apply", "appoint", "approach", "approve", "april", "area",
        "argue", "arise", "army", "around", "arrange", "arrest", "arrive", "arrow",
        "article", "artist", "aside", "asking", "asleep", "aspect", "assault", "assert",
        "assess", "asset", "assign", "assist", "assume", "assure", "athlete", "atmosphere",
        "atom", "attach", "attack", "attain", "attempt", "attend", "attention", "attitude",
        "attract", "auction", "audience", "audio", "august", "author", "auto", "autumn",
        "available", "average", "avoid", "awake", "award", "aware", "away", "awesome",
        "awful", "awkward", "baby", "back", "bacon", "badge", "badly", "balance",
        "ball", "bamboo", "banana", "band", "bank", "banner", "barely", "bargain",
        "barrel", "barrier", "base", "basic", "basket", "battle", "beach", "bean",
        "bear", "beat", "beautiful", "beauty", "became", "because", "become", "bedroom",
        "beer", "before", "began", "begin", "behave", "behind", "being", "belief",
        "believe", "bell", "belong", "below", "belt", "bench", "bend", "beneath",
        "benefit", "beside", "best", "better", "between", "beyond", "bicycle", "bigger",
        "billion", "bind", "bird", "birth", "birthday", "bishop", "bite", "bitter",
        "black", "blade", "blame", "blank", "blanket", "blast", "blaze", "bleed",
        "blend", "bless", "blind", "block", "blood", "bloom", "blossom", "blow",
        "blue", "board", "boat", "body", "boil", "bold", "bolt", "bomb",
        "bond", "bone", "bonus", "book", "boom", "boost", "boot", "border",
        "born", "borrow", "boss", "both", "bother", "bottle", "bottom", "bounce",
        "bound", "bowl", "boxing", "brain", "branch", "brand", "brave", "bread",
        "break", "breakfast", "breath", "breathe", "breeze", "brick", "bridge", "brief",
        "bright", "brilliant", "bring", "broad", "broken", "bronze", "brother", "brought"
    )
    
    // French wordlist - 256 common words
    private val FRENCH_WORDS = listOf(
        "abord", "absence", "absolu", "accepter", "accès", "accord", "acheter", "acte",
        "acteur", "action", "actuel", "addition", "admettre", "adopter", "adorer", "adresse",
        "adulte", "affaire", "afin", "africain", "âge", "agence", "agent", "agir",
        "agréable", "aider", "aile", "aimer", "ainsi", "air", "ajouter", "album",
        "allemand", "aller", "allumer", "alors", "amateur", "amener", "américain", "ami",
        "amour", "amuser", "ancien", "anglais", "animal", "année", "annoncer", "août",
        "apercevoir", "apparaître", "appartement", "appartenir", "appel", "appeler", "apporter", "apprendre",
        "approcher", "après", "arbre", "argent", "armée", "arrêter", "arriver", "article",
        "artiste", "ascenseur", "asseoir", "assez", "assiette", "assister", "assurer", "attacher",
        "attaquer", "atteindre", "attendre", "attention", "attirer", "attitude", "aucun", "aujourd",
        "auprès", "aussi", "aussitôt", "autant", "auteur", "auto", "automne", "autorité",
        "autour", "autre", "autrefois", "autrement", "avance", "avancer", "avant", "avantage",
        "avec", "avenir", "aventure", "avenue", "avion", "avis", "avocat", "avoir",
        "avouer", "avril", "bague", "baigner", "baisser", "bal", "balance", "balcon",
        "balle", "ballon", "banc", "bande", "banque", "barbe", "barque", "barre",
        "bas", "base", "bataille", "bateau", "bâtiment", "bâtir", "baton", "battre",
        "bavarder", "beau", "beaucoup", "beauté", "bébé", "besoin", "bête", "beurre",
        "bibliothèque", "bicyclette", "bien", "bientôt", "bière", "bijou", "billet", "blanc",
        "blé", "blesser", "bleu", "bloc", "blond", "boire", "bois", "boisson",
        "boîte", "bon", "bonbon", "bond", "bonheur", "bonjour", "bonne", "bonsoir",
        "bord", "borgne", "borne", "bouche", "bouger", "bougie", "bouillir", "boulanger",
        "boule", "bouquet", "bourse", "bout", "bouteille", "bouton", "branche", "bras",
        "brave", "bravo", "bretagne", "breton", "brevet", "bridge", "briller", "briser",
        "bronze", "brosse", "brosser", "bruit", "brûler", "brume", "brun", "brusque",
        "brutal", "bruyant", "bureau", "bus", "but", "butter", "cabinet", "cacher",
        "cadeau", "cadre", "café", "cage", "caisse", "calcul", "calme", "camarade",
        "camera", "camion", "camp", "campagne", "canadien", "canal", "canard", "candidat",
        "canne", "canon", "capable", "capital", "capitaine", "caprice", "captif", "car",
        "caractère", "carafe", "carnet", "carré", "carte", "carton", "cas", "casser",
        "casserole", "catastrophe", "cathédrale", "catholique", "cause", "causer", "cave", "caverne",
        "célèbre", "celle", "celui", "cent", "centaine", "central", "centre", "cependant",
        "cercle", "cérémonie", "certain", "certes", "cerveau", "cesser", "ceux", "chacun"
    )
    
    // Spanish wordlist - 256 common words
    private val SPANISH_WORDS = listOf(
        "ábaco", "abajo", "abandonar", "abarcar", "abierto", "abogado", "abrazar", "abreviatura",
        "abrir", "absoluto", "absorber", "abstracto", "abuelo", "abundancia", "aburrido", "abusar",
        "acabar", "academia", "acceso", "accidente", "acción", "aceite", "acelerar", "acento",
        "aceptar", "acerca", "acero", "acertar", "ácido", "aclarar", "acoger", "acompañar",
        "aconsejar", "acontecer", "acordar", "acortar", "acostar", "acostumbrar", "actitud", "actividad",
        "activo", "acto", "actor", "actriz", "actual", "actuar", "acudir", "acuerdo",
        "acumular", "acusar", "adaptar", "adecuado", "adelante", "adelantar", "adelgazar", "además",
        "adentro", "adivinar", "adjetivo", "administrar", "admirar", "admitir", "adolescente", "adonde",
        "adoptar", "adorar", "adorno", "adquirir", "adulto", "adversario", "advertir", "aéreo",
        "aeropuerto", "afán", "afectar", "afecto", "afeitar", "afición", "aficionado", "afirmar",
        "afligir", "afortunado", "afuera", "agachar", "agarrar", "agencia", "agenda", "agente",
        "ágil", "agitar", "agotar", "agradable", "agradar", "agradecer", "agrandar", "agrario",
        "agregar", "agresivo", "agrícola", "agricultor", "agricultura", "agrupar", "agua", "aguantar",
        "aguardar", "agudo", "águila", "aguja", "agujero", "ahí", "ahijado", "ahora",
        "ahorrar", "ahorro", "aire", "aislado", "aislar", "ajedrez", "ajeno", "ajetero",
        "ajo", "ajustar", "ajuste", "alargar", "alarma", "alba", "albanés", "albergar",
        "álbum", "alcalde", "alcance", "alcanzar", "alcoba", "alcohol", "aldea", "alegrar",
        "alegre", "alegría", "alejar", "alemán", "alentar", "alerta", "alfabeto", "alfarero",
        "alfiler", "alfombra", "algo", "algodón", "alguien", "algún", "alguno", "alhaja",
        "aliado", "alianza", "alimentar", "alimento", "alinear", "aliviar", "alivio", "alma",
        "almacén", "almacenar", "almanaque", "almendra", "almirante", "almohada", "almorzar", "almuerzo",
        "alojamiento", "alojar", "alpino", "alquilar", "alquiler", "alrededor", "altar", "alterar",
        "alternativa", "alterno", "alteza", "altitud", "alto", "altura", "aluminio", "alumno",
        "alzar", "allá", "allí", "amabilidad", "amable", "amado", "amanecer", "amante",
        "amar", "amargo", "amarillo", "amarrar", "amasar", "ambición", "ambicioso", "ambiental",
        "ambiente", "ambiguo", "ambos", "ambulancia", "amenaza", "amenazar", "América", "americano",
        "amigable", "amigo", "amistad", "amistoso", "amo", "amontonar", "amor", "amoroso",
        "amparar", "amparo", "ampliación", "ampliar", "amplio", "amuelar", "análisis", "analizar",
        "anaranjado", "anarquía", "anatomía", "ancho", "anchura", "anciano", "ancla", "andar",
        "andén", "anécdota", "anexo", "ángel", "ángulo", "angustia", "anhelo", "anillo",
        "animal", "animar", "ánimo", "aniquilar", "aniversario", "anoche", "anotar", "ansia",
        "ansioso", "ante", "anteayer", "antecedente", "antemano", "antena", "anteojo", "anterior"
    )
    
    private val random = SecureRandom()
    
    fun getWordListSize(language: PassphraseLanguage): Int {
        return when (language) {
            PassphraseLanguage.ENGLISH -> ENGLISH_WORDS.size
            PassphraseLanguage.FRENCH -> FRENCH_WORDS.size
            PassphraseLanguage.SPANISH -> SPANISH_WORDS.size
            PassphraseLanguage.MIXED -> ENGLISH_WORDS.size + FRENCH_WORDS.size + SPANISH_WORDS.size
        }
    }
    
    fun generate(
        wordCount: Int,
        separator: PassphraseSeparator = PassphraseSeparator.DASH,
        language: PassphraseLanguage = PassphraseLanguage.ENGLISH
    ): PassphraseResult {
        require(wordCount in 3..10) { "Word count must be between 3 and 10" }
        
        val wordPool = when (language) {
            PassphraseLanguage.ENGLISH -> ENGLISH_WORDS
            PassphraseLanguage.FRENCH -> FRENCH_WORDS
            PassphraseLanguage.SPANISH -> SPANISH_WORDS
            PassphraseLanguage.MIXED -> ENGLISH_WORDS + FRENCH_WORDS + SPANISH_WORDS
        }
        
        val selectedWords = mutableListOf<String>()
        
        repeat(wordCount) {
            val word = wordPool[random.nextInt(wordPool.size)]
            val capitalized = word.replaceFirstChar { it.uppercase() }
            selectedWords.add(capitalized)
        }
        
        // Handle random separator
        val passphrase = if (separator == PassphraseSeparator.RANDOM) {
            val separators = listOf("-", "_", ".", " ", ",", "|", "~", "#")
            selectedWords.joinToString("") { word ->
                if (word == selectedWords.last()) word
                else word + separators[random.nextInt(separators.size)]
            }
        } else {
            selectedWords.joinToString(separator.char)
        }
        
        val passphraseArray = passphrase.toCharArray()
        selectedWords.clear()
        
        return PassphraseResult(passphraseArray, wordCount)
    }
}
