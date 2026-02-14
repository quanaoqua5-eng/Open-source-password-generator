# 🔄 Workflows Guide - 3 Options Available

## 📋 OVERVIEW:

This package includes **3 workflows** to handle different scenarios:

```
.github/workflows/
├── codeql.yml           ← Main (auto-runs on push/PR)
├── codeql-minimal.yml   ← Minimal CodeQL (manual trigger)
└── simple-build.yml     ← Build only, no CodeQL (manual trigger)
```

---

## 🎯 WHEN TO USE EACH:

### 1. codeql.yml (DEFAULT)
**Auto-runs on:** Push & Pull Request  
**Purpose:** Full CodeQL security scan  
**Best for:** Production use

**What it does:**
1. Sets up environment
2. Downloads Gradle
3. Generates wrapper
4. Builds APK
5. Runs CodeQL analysis

**Use when:**
- ✅ Repository is public
- ✅ You have GitHub Advanced Security
- ✅ You want automatic security scanning

---

### 2. codeql-minimal.yml (FALLBACK)
**Auto-runs on:** Manual trigger only  
**Purpose:** CodeQL with autobuild  
**Best for:** When custom build fails

**What it does:**
1. Sets up environment
2. Initializes CodeQL
3. Uses autobuild (CodeQL auto-detects build)
4. Runs analysis

**Use when:**
- ⚠️ Main workflow's build step fails
- ⚠️ You want simpler approach
- ⚠️ Custom build is too complex

**How to run:**
```
GitHub → Actions → "CodeQL Minimal Scan" → Run workflow
```

---

### 3. simple-build.yml (TEST ONLY)
**Auto-runs on:** Manual trigger or push  
**Purpose:** Build APK without CodeQL  
**Best for:** Testing if build works

**What it does:**
1. Sets up environment
2. Downloads Gradle
3. Generates wrapper
4. Builds APK
5. Uploads APK artifact

**Use when:**
- 🔧 Testing if build configuration works
- 🔧 CodeQL not available (private repo without Advanced Security)
- 🔧 Just need to verify compilation

**How to run:**
```
GitHub → Actions → "Simple Android Build" → Run workflow
```

**APK Location:**
After successful run → Actions → Click run → Artifacts → Download app-debug

---

## 🚀 RECOMMENDED WORKFLOW:

### First Time Upload:
```bash
# 1. Upload code
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/passdrop.git
git push -u origin main

# 2. Main workflow auto-runs (codeql.yml)
# Wait 5-10 minutes
```

### If Main Workflow Fails:

#### Option A: Try Simple Build First
```
Actions → Simple Android Build → Run workflow
```

**If SUCCESS:** Build config is OK, problem is CodeQL
**If FAIL:** Build config needs fixing

#### Option B: Try Minimal CodeQL
```
Actions → CodeQL Minimal Scan → Run workflow
```

**If SUCCESS:** Use this as main workflow (edit on: section)
**If FAIL:** CodeQL not available for this repo

---

## 📊 COMPARISON:

| Workflow | Auto-run? | CodeQL? | Build Method | Use Case |
|----------|-----------|---------|--------------|----------|
| codeql.yml | ✅ Yes | ✅ Yes | Custom | Production |
| codeql-minimal.yml | ❌ Manual | ✅ Yes | Autobuild | Fallback |
| simple-build.yml | ⚠️ Optional | ❌ No | Custom | Testing |

---

## 🔧 SWITCHING WORKFLOWS:

### To use codeql-minimal.yml as main:

Edit `.github/workflows/codeql-minimal.yml`:

```yaml
# Change from:
on:
  workflow_dispatch:

# To:
on:
  push:
    branches: ["main", "master"]
  pull_request:
    branches: ["main", "master"]
```

Then rename codeql.yml to codeql-custom.yml (to disable it).

---

## ✅ EXPECTED RESULTS:

### codeql.yml Success:
```
✓ Checkout repository
✓ Setup JDK 17
✓ Setup Android SDK
✓ Initialize CodeQL
✓ Setup Gradle and Build
  BUILD SUCCESSFUL
✓ Perform CodeQL Analysis
  0 vulnerabilities found
```

### codeql-minimal.yml Success:
```
✓ Checkout
✓ Setup JDK
✓ Initialize CodeQL
✓ Autobuild
  BUILD SUCCESSFUL
✓ Perform CodeQL Analysis
  0 vulnerabilities found
```

### simple-build.yml Success:
```
✓ Checkout
✓ Set up JDK 17
✓ Setup Android SDK
✓ Setup Gradle Wrapper
✓ Build Debug APK
  BUILD SUCCESSFUL
✓ Upload APK
  Artifact uploaded
```

---

## 🆘 TROUBLESHOOTING:

### Error after 45s?
**Likely:** CodeQL initialization failed

**Solutions:**
1. Try `simple-build.yml` first to verify build works
2. If simple build works, problem is CodeQL access
3. Check repository security settings
4. Try `codeql-minimal.yml` (uses autobuild)

### Build fails?
**Likely:** Gradle/Android SDK issue

**Solutions:**
1. Check JDK version (should be 17)
2. Check Android SDK components
3. View full logs for specific error
4. Check build.gradle.kts dependencies

### CodeQL not available?
**Likely:** Private repo without Advanced Security

**Solutions:**
1. Make repository public (temporary)
2. Enable GitHub Advanced Security (paid)
3. Use `simple-build.yml` to verify build only

---

## 📝 QUICK REFERENCE:

**Want automatic scans?**
→ Use `codeql.yml` (default)

**Custom build failing?**
→ Try `codeql-minimal.yml`

**Just need to build APK?**
→ Use `simple-build.yml`

**Private repo without Advanced Security?**
→ Use `simple-build.yml` or make repo public

---

**All workflows are configured and ready to use!**
