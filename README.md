# # PhantomClone 👻

**AntiDetect App Cloner for Android** — Create isolated profiles with unique device fingerprints and run cloned apps in sandboxed environments.

[![Build APK](https://github.com/daterlydate-cyber/palka-kolbasy/actions/workflows/build.yml/badge.svg)](https://github.com/daterlydate-cyber/palka-kolbasy/actions/workflows/build.yml)

---

## 📱 Screenshots

> Coming soon — build and run in Android Studio to see the UI.

---

## 🚀 Features (Phase 1 MVP)

| Feature | Status |
|---|---|
| Profile Manager (CRUD) | ✅ |
| Fingerprint Generator (50+ real devices) | ✅ |
| Proxy Settings per profile (HTTP/SOCKS5) | ✅ |
| Sandbox Directory Isolation | ✅ |
| Virtual Engine Interface | ✅ (Foundation) |
| System Property Hook (stub) | ✅ (Foundation) |
| Fingerprint Viewer with copy | ✅ |
| App Manager UI | ✅ |
| Dark Cyber Theme (purple/cyan) | ✅ |
| GitHub Actions CI/CD | ✅ |

---

## 🏗️ Architecture

```
PhantomClone
├── data/
│   ├── model/          ← Domain models (Profile, DeviceFingerprint, etc.)
│   ├── database/       ← Room DB, entities, DAOs
│   └── repository/     ← ProfileRepository (single source of truth)
├── fingerprint/
│   ├── RealDeviceDatabase.kt   ← 50+ real device specs
│   └── FingerprintGenerator.kt ← Consistent fingerprint generation
├── engine/
│   ├── VirtualEngine.kt        ← Interface (Phase 2: full implementation)
│   ├── SandboxManager.kt       ← Isolated storage per profile
│   ├── SystemPropertyHook.kt   ← Build.* spoofing (Phase 2: hooks)
│   └── ProxyManager.kt         ← Per-profile proxy routing
├── network/
│   ├── ProxyInterceptor.kt     ← OkHttp proxy interceptor
│   └── NetworkFingerprintManager.kt
├── ui/
│   ├── screens/                ← Compose screens
│   ├── theme/                  ← Material 3 dark cyber theme
│   └── navigation/             ← Navigation graph
├── viewmodel/                  ← MVVM ViewModels
└── di/                         ← Hilt DI modules
```

### Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Database**: Room
- **Network**: OkHttp
- **Serialization**: kotlinx.serialization
- **Preferences**: DataStore

---

## 🔨 Build Instructions

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK 34

### Steps

```bash
# Clone the repository
git clone https://github.com/daterlydate-cyber/palka-kolbasy.git
cd palka-kolbasy

# Build debug APK
./gradlew assembleDebug

# Find APK at:
# app/build/outputs/apk/debug/app-debug.apk
```

Or open in Android Studio and press **▶ Run**.

### Download pre-built APK
After each push to main, GitHub Actions automatically builds the APK.  
Go to **Actions → Build PhantomClone APK → Artifacts** to download.

---

## 🗺️ Roadmap

### Phase 1 — MVP (Current) ✅
- Profile management with Room DB
- Fingerprint generator (50+ real devices, Luhn IMEI, proper MAC)
- Sandbox isolation architecture
- Proxy support per profile
- Dark cyber UI

### Phase 2 — Virtual Engine 🔄
- Full APK classloader injection
- `Build.*` property hooks via reflection
- Component proxy system (stub activities/services)
- File system redirection

### Phase 3 — Network & TLS 📡
- JA3/JA4 TLS fingerprint customization
- WebRTC leak prevention
- DNS-over-HTTPS routing
- Per-profile network namespace

### Phase 4 — Advanced Anti-Detect 🔬
- Canvas fingerprint noise injection
- Sensor data spoofing
- Play Integrity research
- Magisk module integration

---

## ⚠️ Legal Notice

This project is for **educational and research purposes** only.
Use responsibly and in compliance with the terms of service of any apps you interact with.

---

## 📄 License

MIT License — see [LICENSE](LICENSE)
