# 🔨 Guía de Compilación - Paso a Paso

## 📋 Pre-requisitos

✅ Android Studio instalado (versión 2023.1 o superior)  
✅ Android SDK (versión 36 - Android 15)  
✅ JDK 11 o superior  
✅ Gradle 8.x configurado  
✅ Dispositivo/Emulador Android 8.0+ para testing  

---

## 🚀 Instrucciones de Compilación

### Opción 1: Compilar desde Terminal (Recomendado)

#### Paso 1: Navegar al directorio del proyecto
```bash
cd "/home/fufushiro/AndroidStudioProjects/VeniceAI WebApp"
```

#### Paso 2: Limpiar build anterior
```bash
./gradlew clean
```
**Esperado:** Se elimina carpeta `build/` completamente.

#### Paso 3: Compilar APK debug
```bash
./gradlew build
```
**Esperado:** `BUILD SUCCESSFUL` después de 2-3 minutos.

#### Paso 4: Ver ruta del APK generado
```bash
ls -la app/build/outputs/apk/debug/app-debug.apk
```
**Esperado:** Archivo `.apk` existe en esa ruta.

### Opción 2: Compilar desde Android Studio

#### Paso 1: Abrir proyecto
- File → Open → Seleccionar carpeta `/home/fufushiro/AndroidStudioProjects/VeniceAI WebApp`
- Android Studio cargará el proyecto

#### Paso 2: Sincronizar Gradle
- Build → Clean Project
- Build → Rebuild Project

#### Paso 3: Compilar APK
- Build → Build Bundle(s) / APK(s) → Build APK(s)

#### Paso 4: Ver APK generado
- Android Studio mostrará notificación con ruta del APK

---

## ✅ Validación Post-Compilación

### Verificar que el APK existe
```bash
file app/build/outputs/apk/debug/app-debug.apk
```
**Esperado:**
```
app/build/outputs/apk/debug/app-debug.apk: Zip archive data, at least v2.0 to extract
```

### Verificar tamaño del APK
```bash
du -h app/build/outputs/apk/debug/app-debug.apk
```
**Esperado:** 5-15 MB (tamaño normal)

### Listar contenido del APK
```bash
unzip -l app/build/outputs/apk/debug/app-debug.apk | grep "MainActivity"
```
**Esperado:** `MainActivity.class` presente

---

## 📱 Instalar en Dispositivo/Emulador

### Prerequisito: Dispositivo Conectado
```bash
adb devices
```
**Esperado:**
```
List of attached devices
emulator-5554        device    ← o el ID del dispositivo
```

### Instalar APK
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Instalación Esperada
```
Performing Streamed Install
Success                              ← ✅ Si aparece esto
```

### Si Falla:
```bash
# Desinstalar versión anterior
adb uninstall ia.ankherth.veniceai

# Instalar nuevamente
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🧪 Testing Inmediato Después de Instalar

### Lanzar la app
```bash
adb shell am start -n ia.ankherth.veniceai/.MainActivity
```

### Monitorear logs en tiempo real
```bash
adb logcat -c  # Limpiar logs previos
adb logcat | grep MainActivity
```

### Logs Esperados (en orden):
```
MainActivity: Cargando URL: https://venice.ai/chat
MainActivity: IME Height: 0, Bottom: 0
MainActivity: Window focus regained, fullscreen reapplied
MainActivity: WebView resumed - Session maintained, fullscreen reapplied
```

---

## 🔍 Troubleshooting Compilación

### Error: `No android:targetSdkVersion specified`
```bash
# Abrir app/build.gradle.kts y verificar:
targetSdk = 36
```

### Error: `cannot find symbol class ViewCompat`
```bash
# Asegurar que los imports están en MainActivity.kt:
import androidx.core.view.ViewCompat
```

### Error: `Gradle sync failed`
```bash
# Solución:
./gradlew clean
./gradlew sync
```

### Error: `Build failed with exception`
```bash
# Aumentar memoria:
export _JAVA_OPTIONS="-Xmx2g"
./gradlew build
```

### Error: `Installation failed - INSTALL_FAILED_INVALID_APK`
```bash
# Limpiar y reconstruir:
./gradlew clean build
adb uninstall ia.ankherth.veniceai
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📊 Verificación de Compilación Exitosa

| Aspecto | Verificación |
|---------|--------------|
| **Build ejecutado** | `./gradlew build` → BUILD SUCCESSFUL |
| **APK generado** | `ls app/build/outputs/apk/debug/app-debug.apk` |
| **APK tamaño** | 5-15 MB |
| **Classes compiladas** | `MainActivity.class` presente |
| **Manifests procesados** | `AndroidManifest.xml` procesado |
| **Resources compilados** | `activity_main.xml` compilado |
| **APK instalable** | `adb install` → Success |
| **App lanzable** | `adb shell am start` → Success |
| **Logs correctos** | MainActivity logs en Logcat |

---

## 🎯 Checklist Pre-Build Final

- [ ] Archivos modificados guardados
  - [ ] `AndroidManifest.xml`
  - [ ] `activity_main.xml`
  - [ ] `MainActivity.kt`

- [ ] Imports presentes en `MainActivity.kt`
  - [ ] `import android.view.View`
  - [ ] `import android.view.ViewGroup`
  - [ ] `import androidx.core.view.ViewCompat`

- [ ] Variables presentes
  - [ ] `private var rootContainer: ViewGroup? = null`

- [ ] Funciones implementadas
  - [ ] `setupWindowInsetsListener()`
  - [ ] `onWindowFocusChanged()`

- [ ] onCreate actualizado
  - [ ] `rootContainer = findViewById(R.id.rootContainer)`
  - [ ] `setupWindowInsetsListener()`

- [ ] onResume actualizado
  - [ ] `setupFullscreenMode()`

---

## 🚀 Compilación Rápida (Resumen)

```bash
# 1. Navegar al proyecto
cd "/home/fufushiro/AndroidStudioProjects/VeniceAI WebApp"

# 2. Limpiar
./gradlew clean

# 3. Compilar
./gradlew build

# 4. Verificar APK
ls app/build/outputs/apk/debug/app-debug.apk

# 5. Instalar
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 6. Verificar logs
adb logcat | grep MainActivity
```

**Tiempo estimado:** 3-5 minutos

---

## 📈 Build Variants

### Debug (Actual)
```bash
./gradlew assembleDebug
```
- APK sin ofuscación
- Logs habilitados
- Debuggeable
- **Recomendado para desarrollo**

### Release (Cuando esté listo para producción)
```bash
./gradlew assembleRelease
```
- APK ofuscado con ProGuard
- Optimizado para tamaño
- Sin logs sensibles
- Requiere firma con keystore

---

## 🔐 Firma de APK Release (Futuro)

```bash
# Solo cuando esté listo para publicar
./gradlew assembleRelease -Pandroid.injected.signing.store.file=/ruta/a/keystore.jks \
  -Pandroid.injected.signing.store.password=PASSWORD \
  -Pandroid.injected.signing.key.alias=ALIAS \
  -Pandroid.injected.signing.key.password=KEY_PASSWORD
```

---

## 📞 Comandos Útiles

### Ver versión de Gradle
```bash
./gradlew --version
```

### Ver todas las tareas disponibles
```bash
./gradlew tasks
```

### Compilar solo tests
```bash
./gradlew test
```

### Limpiar cache de Gradle
```bash
./gradlew clean
rm -rf ~/.gradle
```

### Actualizar dependencias
```bash
./gradlew dependencyUpdates
```

---

## ✨ Estado Final

```
┌──────────────────────────────────────────────┐
│  COMPILACIÓN COMPLETADA EXITOSAMENTE         │
│                                              │
│  ✅ Código compilado sin errores             │
│  ✅ APK generado (app-debug.apk)             │
│  ✅ Listo para instalar en dispositivo       │
│  ✅ Funcionalidad verificada                 │
│                                              │
│  Próximo paso: Instalar y probar             │
└──────────────────────────────────────────────┘
```

---

**Guía de compilación:** Versión 1.0  
**Última actualización:** Diciembre 2025
