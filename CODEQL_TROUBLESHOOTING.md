# 🔧 CodeQL Troubleshooting Guide

## ❌ Error: "Process completed with exit code 1" after 45s

### Possible Causes:

#### 1. CodeQL Initialization Failed
**Symptoms:** Fails at "Initialize CodeQL" step
**Solution:** Repository must be public OR have GitHub Advanced Security enabled

**Check:**
- Go to Settings → Code security and analysis
- Enable "CodeQL analysis" if available
- For private repos: Need GitHub Enterprise or Advanced Security

#### 2. Build Configuration Issue
**Symptoms:** Fails at build step
**Solution:** Use one of our 3 workflows

#### 3. Permissions Issue
**Symptoms:** "security-events: write" error
**Solution:** Already fixed in workflow (permissions section added)

---

## 🚀 3 WORKFLOWS INCLUDED:

### 1. codeql.yml (Main - Auto-runs)
**When:** On every push/PR
**What:** Full CodeQL scan
**Use:** Default workflow

```yaml
name: CodeQL Android Scan
on: [push, pull_request]
```

### 2. simple-build.yml (Build Only)
**When:** Manual trigger
**What:** Just builds APK, no CodeQL
**Use:** To test if build works without CodeQL

```yaml
name: Simple Android Build
on: workflow_dispatch
```

**How to run:**
1. Go to Actions tab
2. Select "Simple Android Build"
3. Click "Run workflow"

### 3. codeql-minimal.yml (Autobuild)
**When:** Manual trigger
**What:** CodeQL with autobuild
**Use:** If custom build fails

```yaml
name: CodeQL Minimal Scan
on: workflow_dispatch
```

---

## 📊 DEBUGGING STEPS:

### Step 1: Test Simple Build First
```
Actions → Simple Android Build → Run workflow
```

**If this FAILS:**
- Problem is with Android build configuration
- Check build.gradle.kts files
- Check dependencies

**If this SUCCEEDS:**
- Build config is OK
- Problem is with CodeQL integration

### Step 2: Try Minimal CodeQL
```
Actions → CodeQL Minimal Scan → Run workflow
```

**If this SUCCEEDS:**
- Use autobuild instead of custom build
- Update main workflow to use autobuild

**If this FAILS:**
- Check repository security settings
- Verify permissions

### Step 3: Check Logs
Click on failed workflow → Expand failed step → Read error

**Common errors:**

#### "Resource not accessible by integration"
**Fix:** Enable security-events write permission
**Status:** ✅ Already added to workflow

#### "CodeQL is not supported for this language"
**Fix:** Change language from `java-kotlin` to `java`
**Workflow line:** `languages: java`

#### "Build failed"
**Fix:** Use autobuild instead
**Replace:**
```yaml
- name: Setup Gradle and Build
  run: |
    # ... build steps
```

**With:**
```yaml
- name: Autobuild
  uses: github/codeql-action/autobuild@v3
```

---

## ✅ RECOMMENDED APPROACH:

### For Public Repositories:
1. Use `codeql.yml` (main workflow)
2. Should work automatically
3. If fails, try `codeql-minimal.yml`

### For Private Repositories:
**Requires GitHub Advanced Security**

**Check eligibility:**
- GitHub Enterprise account
- OR GitHub Team/Enterprise plan
- OR educational/open source program

**If not eligible:**
- Make repository public (temporarily) for scan
- OR use `simple-build.yml` to verify build works
- OR use external CodeQL runners

---

## 🔍 VERIFY WORKFLOW FILES:

```bash
# After extracting package:

# List workflows
ls -la .github/workflows/

# Should see:
# codeql.yml
# simple-build.yml
# codeql-minimal.yml

# Check main workflow
cat .github/workflows/codeql.yml | grep "languages"
# Should show: languages: java-kotlin
```

---

## 🎯 QUICK FIXES:

### Fix 1: Use Autobuild
Replace build step with:
```yaml
- name: Autobuild
  uses: github/codeql-action/autobuild@v3
```

### Fix 2: Change Language
If java-kotlin fails, try just java:
```yaml
languages: java
```

### Fix 3: Disable CodeQL Temporarily
Use `simple-build.yml` to verify build:
```bash
Actions → Simple Android Build → Run workflow
```

---

## 📝 MOST COMMON ISSUE:

### Issue: CodeQL not available for private repo

**Symptoms:**
- Fails after 45s
- "Resource not accessible"
- "Advanced Security required"

**Solution A (Free):**
Make repository public temporarily

**Solution B (Paid):**
Enable GitHub Advanced Security

**Solution C (Workaround):**
Use build-only workflow:
```
Actions → Simple Android Build → Run workflow
```

This will:
- ✅ Verify code compiles
- ✅ Build APK successfully
- ✅ No CodeQL scan (but build verified)

---

## ✅ SUCCESS CRITERIA:

### Simple Build Success:
```
✓ Checkout
✓ Setup JDK 17
✓ Setup Android SDK
✓ Setup Gradle Wrapper
✓ Build Debug APK
  BUILD SUCCESSFUL
✓ Upload APK
```

### CodeQL Success:
```
✓ Checkout
✓ Setup JDK 17
✓ Setup Android SDK
✓ Initialize CodeQL
✓ Setup Gradle and Build
  BUILD SUCCESSFUL
✓ Perform CodeQL Analysis
  0 vulnerabilities found
```

---

## 🆘 STILL STUCK?

1. **Check workflow logs in detail**
2. **Copy exact error message**
3. **Check which step failed**
4. **Try alternative workflows**
5. **Verify repository settings**

---

**Need help? Check GitHub Actions logs for details!**
