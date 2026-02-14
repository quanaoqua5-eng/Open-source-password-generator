# 🔍 DEBUG GUIDE - Find Exact Error

## ❌ Current Error:
```
CodeQL Android Scan / Analyze (pull_request)
Failing after 45s
Error: Process completed with exit code 1.
```

**This is TOO GENERIC! We need MORE INFO!**

---

## 🔎 HOW TO GET FULL ERROR LOG:

### Step 1: Go to GitHub Actions
1. Open your repository on GitHub
2. Click **"Actions"** tab
3. Click on the **failed workflow run**
4. Click **"Analyze"** job

### Step 2: Expand Failed Step
Look for the step with ❌ red X:
- "Initialize CodeQL" - Click to expand
- "Build with Gradle" - Click to expand  
- "Perform CodeQL Analysis" - Click to expand

### Step 3: Copy Full Error
Look for error messages like:
- `error:` lines
- `FAILURE:` messages
- `Exception` traces
- Last 20-30 lines of output

**Screenshot or copy the error and I'll fix it!**

---

## 🎯 COMMON ISSUES & FIXES:

### Issue 1: CodeQL Initialization Failed
**Error pattern:**
```
Error: Unable to initialize CodeQL
```

**Fix:**
```yaml
# In codeql.yml, check:
- name: Initialize CodeQL
  uses: github/codeql-action/init@v3
  with:
    languages: java-kotlin  # Make sure this is correct
```

### Issue 2: Gradle Build Failed
**Error pattern:**
```
Task :app:compileDebugKotlin FAILED
```

**Possible causes:**
- Kotlin version mismatch
- Missing dependencies
- Code syntax error

**Fix:** Check local build first:
```bash
./gradlew assembleDebug --stacktrace
```

### Issue 3: Android SDK Not Found
**Error pattern:**
```
SDK location not found
```

**Fix:**
```yaml
# Make sure this step runs BEFORE build:
- name: Setup Android SDK
  uses: android-actions/setup-android@v3
```

### Issue 4: Out of Memory
**Error pattern:**
```
OutOfMemoryError: Java heap space
```

**Fix:**
```yaml
- name: Build with Gradle
  run: ./gradlew assembleDebug
  env:
    GRADLE_OPTS: "-Xmx4096m -XX:MaxMetaspaceSize=512m"
```

### Issue 5: Network/Download Failed
**Error pattern:**
```
Could not download...
Connection timeout
```

**Fix:** Re-run the workflow (temporary issue)

---

## 📋 WORKFLOWS PROVIDED:

### 1. codeql.yml (Main)
**Approach:** Generate wrapper, then build
**When to use:** Default for all pushes/PRs
**Steps:**
1. Setup Gradle
2. Generate wrapper
3. Build with wrapper

### 2. codeql-simple.yml (Fallback)
**Approach:** Build directly with gradle (no wrapper)
**When to use:** If main workflow fails
**How to trigger:** Manual (workflow_dispatch)

---

## 🧪 TEST WORKFLOWS:

### Test Main Workflow
```bash
# After pushing:
git push origin main

# Wait 1-2 minutes, then:
# Go to Actions tab → See workflow running
```

### Test Simple Workflow
```bash
# On GitHub:
1. Go to Actions tab
2. Select "CodeQL Simple"
3. Click "Run workflow"
4. Select branch
5. Click "Run workflow" button
```

---

## 🔧 EMERGENCY FIX:

If BOTH workflows fail, try this minimal workflow:

```yaml
# .github/workflows/test.yml
name: Test Build

on: workflow_dispatch

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Setup Android SDK
        uses: android-actions/setup-android@v3
      
      - name: Setup Gradle
        uses: gradle/gradle-build-action@v2
      
      - name: Build
        run: gradle assembleDebug --stacktrace
```

**This tests JUST the build - no CodeQL**

---

## 📊 EXPECTED SUCCESS LOG:

When it works, you'll see:

```
✓ Checkout repository                (1s)
✓ Set up JDK 17                      (2s)
✓ Setup Android SDK                 (15s)
✓ Initialize CodeQL                 (20s)
✓ Setup Gradle                      (10s)
✓ Generate Gradle Wrapper            (5s)
✓ Make gradlew executable            (0s)
✓ Build with Gradle Wrapper        (120s)
  > Task :app:compileDebugKotlin
  > Task :app:compileDebugJavaWithJavac
  > Task :app:assembleDebug
  BUILD SUCCESSFUL in 2m 5s
✓ Perform CodeQL Analysis           (60s)
  Analyzing Java-Kotlin...
  0 vulnerabilities found
```

---

## 🎯 NEXT STEPS:

1. **Get full error log** (expand failed step in Actions)
2. **Check which step failed:**
   - Initialize CodeQL? → Language issue
   - Build with Gradle? → Build config issue
   - Perform Analysis? → CodeQL issue

3. **Try simple workflow** (workflow_dispatch)
4. **Share error log** for specific fix

---

## 📝 QUICK CHECKLIST:

- [ ] Extracted PassDrop_v2.1_ULTIMATE_FIX.zip?
- [ ] Uploaded ALL files including .github folder?
- [ ] Workflow file exists: `.github/workflows/codeql.yml`?
- [ ] Can you see workflow in Actions tab?
- [ ] Which step shows ❌ red X?
- [ ] Got full error message?

---

**GET ERROR LOG → WE'LL FIX EXACT ISSUE! 🔧**
