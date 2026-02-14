# Gradle Wrapper Setup

## Option 1: Let GitHub Actions Download (Recommended)

The workflow is configured to auto-download gradle-wrapper.jar using `gradle/gradle-build-action`.

**No manual setup needed!** Just push to GitHub.

## Option 2: Generate Locally (Before Upload)

If you want to test build locally first:

```bash
# Install Gradle 8.4 first, then:
gradle wrapper --gradle-version 8.4

# This will create:
# gradle/wrapper/gradle-wrapper.jar
# gradle/wrapper/gradle-wrapper.properties
# gradlew
# gradlew.bat
```

## Option 3: Download Manually

Download gradle-wrapper.jar from official Gradle releases:

```bash
cd gradle/wrapper
curl -L -o gradle-wrapper.jar \
  https://github.com/gradle/gradle/raw/v8.4.0/gradle/wrapper/gradle-wrapper.jar
```

## Current Status

- ✅ gradlew scripts present
- ✅ gradle-wrapper.properties configured
- ⚠️  gradle-wrapper.jar: Auto-downloaded by GitHub Actions
- ✅ GitHub workflow: Uses gradle/gradle-build-action

**→ Upload and let GitHub Actions handle it!**
