# GitHub Setup Instructions

Follow these steps to push the Network Scanner project to GitHub.

## Prerequisites

- Git installed on your system
- GitHub account (https://github.com/om-kumar-singh)
- Repository created at: https://github.com/om-kumar-singh/Network-Scanner

## Steps to Push to GitHub

### 1. Initialize Git Repository (if not already initialized)

```bash
cd "C:\Users\OM KUMAR SINGH\Desktop\github proj\Network Scanner"
git init
```

### 2. Add All Files

```bash
git add .
```

### 3. Commit Changes

```bash
git commit -m "Initial commit: Network Scanner Android app with dark theme"
```

### 4. Add Remote Repository

```bash
git remote add origin https://github.com/om-kumar-singh/Network-Scanner.git
```

### 5. Push to GitHub

```bash
git branch -M main
git push -u origin main
```

## If Repository Already Exists

If you've already initialized the repository, you can skip step 1 and use:

```bash
git add .
git commit -m "Add README, LICENSE, and screenshots"
git push origin main
```

## Troubleshooting

### If you get authentication errors:
- Use GitHub Personal Access Token instead of password
- Or use SSH: `git remote set-url origin git@github.com:om-kumar-singh/Network-Scanner.git`

### If you need to force push (use with caution):
```bash
git push -u origin main --force
```

## Verify Upload

After pushing, visit: https://github.com/om-kumar-singh/Network-Scanner

You should see:
- ✅ README.md with screenshots
- ✅ LICENSE file
- ✅ All source code files
- ✅ Screenshots folder with images

