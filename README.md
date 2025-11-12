# Network Scanner

<div align="center">

**Android network discovery tool with port scanning**

[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://www.android.com/)
[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.java.com/)
[![Release](https://img.shields.io/badge/Release-12%2F11%2F2025-blue.svg)]()
[![Download](https://img.shields.io/badge/Download-APK-brightgreen)](https://github.com/om-kumar-singh/Network-Scanner/releases/download/v0.0.1/app-debug.apk)

</div>

## 📱 Features

- 🔍 **Network Discovery** - Find active devices on local network
- 🔌 **Port Scanning** - Detect open ports (22, 80, 443, 8080, etc.)
- 📊 **Real-time Results** - Live progress with Material Design 3
- 📤 **Share Results** - Export scan data easily
- 🎨 **Dark Theme** - Professional dark UI with green accents

## 🛠️ Tech Stack

- **Language**: Java + XML
- **Minimum SDK**: 26 (Android 8.0)
- **Architecture**: MVC with concurrent threading
- **UI**: Material Design 3

## 📦 Installation

### Download APK
[![Download APK](https://img.shields.io/badge/Download-Latest_APK-brightgreen?style=for-the-badge&logo=android)](https://github.com/om-kumar-singh/Network-Scanner/releases/download/v0.0.1/app-debug.apk)

1. Download the APK from the link above
2. Enable "Install from unknown sources"
3. Install and launch the app

### Build from Source
```bash
git clone https://github.com/om-kumar-singh/Network-Scanner.git
cd Network-Scanner
# Open in Android Studio and build
```

## 🚀 Usage

1. Connect to your WiFi network
2. Launch Network Scanner
3. Tap "Scan Network"
4. View discovered devices and open ports
5. Share results if needed

## 🏗️ Project Structure

```
app/src/main/
├── java/com/example/networkscannerv1/
│   ├── MainActivity.java      # UI Controller
│   ├── NetworkScanner.java    # IP scanning logic
│   ├── PortScanner.java       # Port detection
│   ├── DeviceInfo.java        # Data model
│   └── DeviceAdapter.java     # List adapter
└── res/
    ├── layout/                # XML layouts
    ├── values/                # Colors, strings
    └── drawable/              # Icons
```

## 📋 Permissions

- `INTERNET` - Network scanning
- `ACCESS_NETWORK_STATE` - Check connectivity
- `ACCESS_WIFI_STATE` - Detect network info

## 🎨 UI Design

- **Background**: #0D0B0A (Dark charcoal)
- **Surface**: #2A3132 (Navy blue)
- **Accent**: #AAFFC3 (Electric lime green)
- **Text**: #FDF6F6 (Soft white)

## 📝 License

MIT License - See [LICENSE](LICENSE) file

## 👤 Developer

**Om Kumar Singh**
- GitHub: [@om-kumar-singh](https://github.com/om-kumar-singh)
- Email: omkumarsingh2004@gmail.com

---

<div align="center">

**First Release: 12/11/2025**

[Download Now](https://github.com/om-kumar-singh/Network-Scanner/releases/download/v0.0.1/app-debug.apk)

</div>
