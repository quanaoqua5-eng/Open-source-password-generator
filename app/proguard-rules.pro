# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Compose runtime
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }

# Keep Material3
-keep class androidx.compose.material3.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep Activity and ComponentActivity
-keep public class * extends androidx.activity.ComponentActivity
-keepclassmembers class * extends androidx.activity.ComponentActivity {
    public <init>(...);
}

# Keep MainActivity
-keep class com.passdrop.passwordgen.MainActivity { *; }

# Keep data classes
-keep class com.passdrop.passwordgen.AppStrings { *; }
-keep class com.passdrop.passwordgen.PasswordEntropy { *; }
-keep class com.passdrop.passwordgen.PasswordResult { *; }

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# Keep SecureRandom
-keep class java.security.SecureRandom { *; }

# Keep ClipboardManager
-keep class android.content.ClipboardManager { *; }
-keep class android.content.ClipData { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
