#!/bin/bash
# Setup Gradle Wrapper for Local Testing
# This script downloads gradle-wrapper.jar if missing

echo "🔧 Setting up Gradle Wrapper..."

WRAPPER_DIR="gradle/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/gradle-wrapper.jar"
GRADLE_VERSION="8.4"

# Check if gradle-wrapper.jar exists and is valid
if [ -f "$WRAPPER_JAR" ] && [ -s "$WRAPPER_JAR" ]; then
    SIZE=$(stat -f%z "$WRAPPER_JAR" 2>/dev/null || stat -c%s "$WRAPPER_JAR" 2>/dev/null)
    if [ "$SIZE" -gt 1000 ]; then
        echo "✅ gradle-wrapper.jar already exists and looks valid"
        exit 0
    fi
fi

echo "📥 Downloading gradle-wrapper.jar..."

# Try multiple sources
URLS=(
    "https://github.com/gradle/gradle/raw/v${GRADLE_VERSION}.0/gradle/wrapper/gradle-wrapper.jar"
    "https://raw.githubusercontent.com/gradle/gradle/v${GRADLE_VERSION}.0/gradle/wrapper/gradle-wrapper.jar"
    "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
)

for URL in "${URLS[@]}"; do
    echo "Trying: $URL"
    if curl -L -f -o "$WRAPPER_JAR" "$URL" 2>/dev/null; then
        SIZE=$(stat -f%z "$WRAPPER_JAR" 2>/dev/null || stat -c%s "$WRAPPER_JAR" 2>/dev/null)
        if [ "$SIZE" -gt 1000 ]; then
            echo "✅ Successfully downloaded gradle-wrapper.jar ($SIZE bytes)"
            exit 0
        fi
    fi
done

echo "⚠️  Could not download gradle-wrapper.jar"
echo "📝 Options:"
echo "   1. Install Gradle and run: gradle wrapper --gradle-version 8.4"
echo "   2. Upload to GitHub - workflow will auto-download"
echo "   3. Download manually from: https://github.com/gradle/gradle/releases"

exit 1
