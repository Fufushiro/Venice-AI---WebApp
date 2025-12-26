# 🔨 Guía Completa de Compilación - Venice AI WebApp

## Requisitos Previos

### 1. Instalar Android Studio
- **Versión mínima:** 2024.1 o superior
- Descargar desde: https://developer.android.com/studio
- Instalar con SDKs recomendados

### 2. Instalar Java/JDK
```bash
# Linux/Mac
java -version

# Debe retornar Java 11 o superior
# openjdk version "11.0.x"
```

### 3. Instalar Android SDK
En Android Studio:
1. Tools → SDK Manager
2. Android SDK → SDK Platforms
3. Instalar:
   - Android 8.0 (API 26)
   - Android 13.0 (API 33)
   - Android 14.0 (API 34)
   - Android 15.0 (API 35)

### 4. Instalar Build Tools
En Android Studio:
1. Tools → SDK Manager
2. Android SDK → SDK Tools
3. Instalar:
   - Android SDK Build-Tools 35.0.0+
   - Android SDK Platform-Tools
   - Android SDK Tools

## Compilación Paso a Paso

### Método 1: Android Studio GUI (Recomendado)

#### 1. Abrir el Proyecto
```bash
# En Android Studio
File → Open → /home/fufushiro/AndroidStudioProjects/VeniceAI WebApp
```

#### 2. Sincronizar Gradle
- Esperar a que aparezca el diálogo "Gradle Sync"
- Hacer clic en "Sync Now"
- Esperar a que complete (2-3 minutos aprox.)

#### 3. Verificar Configuración
```
Build → Refresh Linked C++ Projects (si aparece)
```

#### 4. Compilar APK Debug
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

Salida:
```
app/build/outputs/apk/debug/app-debug.apk
```

#### 5. Compilar APK Release (Para Google Play)
```
Build → Generate Signed Bundle / APK
  → APK
  → Create New (Crear keystore)
  → Rellenar datos
  → Finish
```

### Método 2: Terminal/Gradle

#### 1. Navegar al proyecto
```bash
cd /home/fufushiro/AndroidStudioProjects/VeniceAI\ WebApp
```

#### 2. Compilar APK Debug
```bash
# Linux/Mac
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

**Salida esperada:**
```
BUILD SUCCESSFUL in 45s
app/build/outputs/apk/debug/app-debug.apk
```

#### 3. Compilar APK Release
```bash
# Linux/Mac
./gradlew assembleRelease

# Windows
gradlew.bat assembleRelease
```

**Nota:** Requiere keystore creado previamente.

#### 4. Compilar e Instalar en Dispositivo
```bash
# Ver dispositivos conectados
adb devices

# Compilar e instalar (debug)
./gradlew installDebug

# O en una línea
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
```

#### 5. Ejecutar en Dispositivo
```bash
# Iniciar la aplicación
adb shell am start -n ia.ankherth.veniceai/.MainActivity

# Ver logs en tiempo real
adb logcat | grep MainActivity
```

## Tamaño del APK

**Esperado:**
- APK Debug: ~3-5 MB
- APK Release: ~2-3 MB (con minificación)

**Para reducir más:**
```gradle
buildTypes {
    release {
        isMinifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

## Solución de Problemas de Compilación

### Error: "SDK location not found"
```bash
# Crear archivo local.properties
echo "sdk.dir=/path/to/android-sdk" > local.properties

# Linux/Mac (típicamente)
echo "sdk.dir=$HOME/Android/Sdk" > local.properties
```

### Error: "Gradle build daemon has stopped unexpectedly"
```bash
# Limpiar caché de Gradle
./gradlew clean

# Luego compilar de nuevo
./gradlew assembleDebug
```

### Error: "compileSdkVersion mismatch"
Verificar `build.gradle.kts`:
```kotlin
android {
    compileSdk = 36  // Debe estar instalado
    targetSdk = 36
    minSdk = 26
}
```

### Error: "Java version mismatch"
```bash
# Verificar versión de Java
java -version

# Debe ser 11 o superior
# Si no lo es, instalar openjdk-11-jdk o superior
```

### Error: "Insufficient memory"
```bash
# Aumentar memoria en gradle.properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m
```

## Instalación en Dispositivo

### Opción 1: USB Cable
```bash
# 1. Habilitar depuración USB en el dispositivo
#    Configuración → Opciones de Desarrollo → Depuración USB

# 2. Conectar dispositivo via USB

# 3. Verificar conexión
adb devices
# Debe aparecer el dispositivo

# 4. Instalar APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 5. Ejecutar
adb shell am start -n ia.ankherth.veniceai/.MainActivity
```

### Opción 2: Emulador Android
```bash
# 1. Crear emulador en Android Studio
#    Tools → Device Manager → Create Device

# 2. Iniciar emulador
emulator -avd Pixel_API_33

# 3. Instalar APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 4. Ejecutar
adb shell am start -n ia.ankherth.veniceai/.MainActivity
```

### Opción 3: Android Studio
```
Run → Run 'app' 
  → Seleccionar dispositivo/emulador
  → OK
```

## Comandos Útiles de Gradle

```bash
# Limpiar compilaciones previas
./gradlew clean

# Compilar solo verificar sintaxis
./gradlew build --offline

# Ver tareas disponibles
./gradlew tasks

# Compilar sin ejecutar tests
./gradlew build -x test

# Mostrar dependencias
./gradlew dependencies

# Actualizar dependencias
./gradlew --refresh-dependencies build
```

## Debugging

### Ver logs en tiempo real
```bash
adb logcat | grep MainActivity
```

### Capturar logs completos
```bash
adb logcat > logs.txt
```

### Depuración remota con Chrome DevTools
```bash
# Habilitar en código (MainActivity.kt)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    WebView.setWebContentsDebuggingEnabled(true)
}

# Luego ir a: chrome://inspect/#devices
```

## Publicar en Google Play

### 1. Crear Keystore (Una sola vez)
```bash
keytool -genkey -v -keystore venice-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias venice_key
```

### 2. Configurar en gradle.properties
```gradle
KEYSTORE_PATH=/path/to/venice-key.jks
KEYSTORE_PASSWORD=tu_password
KEY_ALIAS=venice_key
KEY_PASSWORD=tu_password
```

### 3. Compilar APK Release Firmado
```bash
./gradlew assembleRelease
```

### 4. Subir a Google Play Console
1. Ir a https://play.google.com/console
2. Crear aplicación
3. Build → Release → New release
4. Subir APK release
5. Completar información de la app
6. Enviar a revisión

## Versionamiento

Para actualizar versión en `build.gradle.kts`:
```kotlin
defaultConfig {
    versionCode = 2       // Incrementar en cada release
    versionName = "1.1"   // X.Y.Z semántico
}
```

## Checklist de Compilación

- ✅ Android Studio actualizado (2024.1+)
- ✅ SDK Tools instalados (API 26+)
- ✅ Java 11+ instalado
- ✅ Proyecto sincronizado con Gradle
- ✅ Sin errores en build.gradle.kts
- ✅ Todos los permisos en AndroidManifest.xml
- ✅ Todas las clases Kotlin sin errores
- ✅ APK generado correctamente
- ✅ APK instalado en dispositivo
- ✅ App ejecuta sin crashes

## Performance de Compilación

**Tiempo esperado:**
- Primera compilación: 1-2 minutos
- Compilaciones posteriores: 20-40 segundos
- Compilación con limpieza: 1-2 minutos

Para acelerar:
```gradle
# En gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
android.useAndroidX=true
```

## Archivos Generados

```
app/build/
├── outputs/
│   ├── apk/
│   │   ├── debug/
│   │   │   └── app-debug.apk          ← APK Debug
│   │   └── release/
│   │       └── app-release.apk        ← APK Release
│   └── bundle/
│       └── release/
│           └── app-release.aab        ← App Bundle
├── intermediates/
│   ├── classes/
│   ├── manifests/
│   └── resources/
└── reports/
```

---

**Última actualización:** Diciembre 2024  
**Versión:** 1.0  
**Estado:** Producción

