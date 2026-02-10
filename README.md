# 🔐 PassDrop - Password Generator

**PassDrop** is a secure, feature-rich Android password generator with advanced customization and multilingual support.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)

## ✨ Features

### 🔒 **Maximum Security**
- ✅ **SecureRandom**: Cryptographically secure random number generation
- ✅ **Screenshot Protection**: FLAG_SECURE prevents screenshots and screen recording
- ✅ **Auto-Clear**: Password automatically clears after copying
- ✅ **Memory Safe**: Automatic cleanup on app exit
- ✅ **No Storage**: Passwords never saved to disk
- ✅ **Offline**: No internet connection required

### 🎯 **Advanced Password Options**
- **Flexible Length**: 5-300 characters with dual input (slider + text field)
- **Character Types**:
  - Uppercase letters (A-Z)
  - Lowercase letters (a-z)
  - Numbers (0-9)
  - **Custom Special Characters**: Select individual symbols (!@#$%^&*...)
  - Non-ASCII (100+ languages, 3000+ characters)

### 🎨 **Custom Special Characters**
Click "Customize..." to select exactly which special characters to include:
```
! @ # $ % ^ & * ( ) - _ = + [ ] { } | ; : ' " , . < > ? / ` ~ \
```
- Tap to toggle individual characters
- Visual selection (blue = selected, gray = not selected)
- "Select All" / "Deselect All" quick actions

### 🌍 **Multilingual Support**
**UI Languages**: English & Vietnamese (toggle with 🌐 button)

**Non-ASCII Characters** (100+ languages):
- 🇪🇺 European: Vietnamese, French, German, Spanish, Portuguese, Italian, Polish, Czech, Romanian, Hungarian, Turkish, Nordic
- 🇷🇺 Cyrillic: Russian, Ukrainian, Serbian, Bulgarian
- 🇬🇷 Greek
- 🇸🇦 Middle East: Arabic, Hebrew  
- 🇨🇳 🇯🇵 🇰🇷 East Asian: Chinese (3000+ chars), Japanese (Hiragana + Katakana), Korean (3000+ Hangul)
- 🇹🇭 Southeast Asian: Thai, Burmese, Lao, Khmer

### 📊 **Password Strength Analysis**
Real-time entropy calculation with visual indicators:

| Entropy | Strength | Color | Est. Crack Time |
|---------|----------|-------|-----------------|
| < 40 bits | Very Weak | 🔴 Red | < 1 second |
| 40-60 bits | Weak | 🟠 Orange | < 1 hour |
| 60-80 bits | Moderate | 🟡 Yellow | Days |
| 80-100 bits | Strong | 🟢 Green | Months |
| 100+ bits | Very Strong | 🟢 Dark Green | 100+ years |

### 🛡️ **Smart Generation**
- **Guaranteed Diversity**: Each selected character type appears at least once
- **No Duplicates**: Unique characters when pool size allows
- **Secure Shuffling**: SecureRandom ensures unpredictable ordering

## 📸 Screenshots

```
🔐 PassDrop                    [🌐 EN]
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Password Length (5-300)
           25
━━━━━━●━━━━━━━━━━━━━━━━━━━━━━━
5                            300

☑ Uppercase (A-Z)
☑ Lowercase (a-z)
☑ Numbers (0-9)
☑ Special Characters [Customize... (32)]
☐ Non-ASCII (100+ Languages)

   [ GENERATE PASSWORD ]
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Generated Password:      [📋 Copy]
aB3#xY9@mK2pQ7!zW5

Length: 18 characters
Entropy: 118 bits
Strength: Very Strong 🟢
████████████████████ 100%

💡 Password will be cleared after copying
```

## 🚀 Installation

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- JDK 11+
- Android SDK API 24-34

### Build from Source
```bash
git clone https://github.com/yourusername/passdrop.git
cd passdrop
./gradlew assembleDebug
```

### Install APK
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 💡 Usage

### Basic Workflow
1. **Set Length**: Type number or use slider (5-300)
2. **Select Options**: Check desired character types
3. **Customize** (Optional): Click "Customize..." for special chars
4. **Generate**: Tap "GENERATE PASSWORD"
5. **Copy**: Tap "📋 Copy" (password auto-clears after copy)
6. **Paste**: Use immediately in target app

### Custom Special Characters
1. Enable "Special Characters" checkbox
2. Click "Customize... (count)"
3. Dialog shows 6x6 grid of all special characters:
   ```
   !  @  #  $  %  ^
   &  *  (  )  -  _
   =  +  [  ]  {  }
   |  ;  :  '  "  ,
   .  <  >  ?  /  `
   ~  \  
   ```
4. Tap characters to toggle (blue = selected, gray = not)
5. Use quick actions:
   - "Select All" - check all 32 characters
   - "Deselect All" - uncheck all
6. Click "OK" to apply selection

### Language Switching
- Tap 🌐 icon (top-right)
- Toggles between English ↔ Vietnamese
- All UI text updates instantly

## 🔐 Security Architecture

### Entropy Formula
```
Entropy = Length × log₂(PoolSize)

Example:
Length: 16 chars
Pool: 94 chars (uppercase + lowercase + numbers + specials)
Entropy: 16 × log₂(94) = 16 × 6.55 = 104.8 bits
```

### Password Generation Algorithm
```kotlin
1. Create character pools from selected options
2. Guarantee one char from each pool
3. Fill remaining length (avoid duplicates when possible)
4. Secure shuffle with SecureRandom
5. Return password + entropy metadata
```

### Memory Safety
- Pre-check available memory before generation
- StringBuilder with pre-allocated capacity
- Automatic cleanup on exceptions
- Lifecycle-aware state management

## 🏗️ Tech Stack

- **Language**: Kotlin 1.9.0
- **UI**: Jetpack Compose + Material Design 3
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34
- **Security**: SecureRandom, FLAG_SECURE
- **Architecture**: Single-Activity Compose

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/passdrop/passwordgen/
│   │   └── MainActivity.kt          # Complete app logic
│   ├── res/
│   │   ├── drawable/
│   │   │   ├── ic_launcher_background.xml  # Blue gradient
│   │   │   └── ic_launcher_foreground.xml  # Lock + drop icon
│   │   ├── mipmap-anydpi-v26/
│   │   │   ├── ic_launcher.xml
│   │   │   └── ic_launcher_round.xml
│   │   ├── values/
│   │   │   ├── strings.xml          # App name
│   │   │   └── themes.xml           # Light theme
│   │   ├── values-night/
│   │   │   └── themes.xml           # Dark theme
│   │   └── xml/
│   │       ├── backup_rules.xml     # Disable backups
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## 🤝 Contributing

Contributions welcome! Please:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

### Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable names
- Add comments for complex logic
- Write composable functions for reusable UI

## 📝 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

## 🌟 Features Roadmap

### Current (v1.0)
- ✅ Secure random generation
- ✅ Custom special chars
- ✅ 100+ languages
- ✅ Entropy calculator
- ✅ Auto-clear on copy
- ✅ EN/VI localization

### Planned (v1.1)
- ⏳ Password history (encrypted)
- ⏳ Pronounceable passwords
- ⏳ Password strength checker for existing passwords
- ⏳ Export/Import settings
- ⏳ More UI themes

### Future
- ⏳ Passphrase generation (diceware)
- ⏳ QR code export
- ⏳ Biometric lock
- ⏳ Widget support

## ⚠️ Security Notice

PassDrop generates cryptographically secure passwords. However:

- **Use a password manager** to store passwords safely
- **Never reuse passwords** across different services
- **Enable 2FA** whenever available
- **Update passwords regularly** for critical accounts

## 🙏 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI
- [Material Design 3](https://m3.material.io/) - Design system
- [SecureRandom](https://developer.android.com/reference/java/security/SecureRandom) - CSPRNG

## 📞 Support

- 🐛 [Report Bug](https://github.com/yourusername/passdrop/issues)
- 💡 [Request Feature](https://github.com/yourusername/passdrop/issues)
- 📖 [Documentation](https://github.com/yourusername/passdrop/wiki)

## 📊 Stats

![GitHub stars](https://img.shields.io/github/stars/yourusername/passdrop?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/passdrop?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/yourusername/passdrop?style=social)

---

**Made with ❤️ and Kotlin** | PassDrop - Secure Password Generation Made Easy
