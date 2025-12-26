# 🆘 Soporte y Ayuda - Venice AI WebApp

## 📍 Empezar Aquí

Si es tu **primera vez**, lee en este orden:

1. **[INDEX.md](INDEX.md)** - Navegación completa (5 min)
2. **[QUICK_START.md](QUICK_START.md)** - Inicio rápido (10 min)
3. **[BUILD_GUIDE.md](BUILD_GUIDE.md)** - Compilación (15 min)

---

## ❓ Preguntas Frecuentes

### ¿Por dónde empiezo?

Abre **INDEX.md** y sigue las secciones según tu rol:
- **Principiante:** QUICK_START → BUILD_GUIDE
- **Intermedio:** CUSTOMIZATION → WEBVIEW_CONFIG
- **Avanzado:** SUMMARY → código fuente

### ¿Cómo compilo la aplicación?

**Opción 1 - Terminal (40 segundos):**
```bash
cd /path/to/VeniceAI\ WebApp
./gradlew assembleDebug
# APK en: app/build/outputs/apk/debug/app-debug.apk
```

**Opción 2 - Android Studio:**
```
Build → Build APK(s) → Esperar
```

**Opción 3 - Script bash:**
```bash
chmod +x build.sh
./build.sh debug
```

Ver **[BUILD_GUIDE.md](BUILD_GUIDE.md)** para más opciones.

### ¿Cómo instalo en un dispositivo?

```bash
adb devices                                    # Ver dispositivos
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n ia.ankherth.veniceai/.MainActivity
```

### ¿Puedo cambiar colores/nombre/URL?

**Sí.** Ve a **[CUSTOMIZATION.md](CUSTOMIZATION.md)**

Cambios rápidos:
- **Nombre:** `res/values/strings.xml`
- **Colores:** `res/values/colors.xml`
- **URL:** `MainActivity.kt` → `VENICE_URL`

### La app no compila. ¿Qué hago?

1. Lee **[BUILD_GUIDE.md](BUILD_GUIDE.md)** → "Solución de Problemas"
2. Ejecuta: `./gradlew clean`
3. Intenta: `./gradlew assembleDebug`
4. Si persiste, revisa logs: `adb logcat | grep MainActivity`

### ¿Cómo publico en Google Play?

1. Sigue **[BUILD_GUIDE.md](BUILD_GUIDE.md)** → "Publicar en Google Play"
2. Crear keystore (una sola vez)
3. Compilar APK Release
4. Subir a Google Play Console

### ¿Qué requisitos se implementaron?

**Todos.**

Ve a **[SUMMARY.md](SUMMARY.md)** → "Requisitos Implementados" para lista completa.

### ¿Puedo usar esto en un proyecto comercial?

**Sí.** El código está optimizado y listo para producción.

### ¿Hay documentación en HTML?

Los archivos `.md` se pueden convertir con:
```bash
pandoc README.md -o README.html
```

O visionar en GitHub/GitLab directamente.

---

## 🔍 Búsqueda Rápida

### Por Tema

**Cache y Almacenamiento:**
- → WEBVIEW_CONFIG.md → "Cache y Almacenamiento"
- → MainActivity.kt → `configureWebView()`

**Pantalla Completa:**
- → QUICK_START.md → "Pantalla Completa"
- → MainActivity.kt → `setupFullscreenMode()`

**Subida de Archivos:**
- → CUSTOMIZATION.md → "JavaScript Interface"
- → CustomWebChromeClient.kt → `onShowFileChooser()`

**Permisos:**
- → README.md → "Permisos"
- → MainActivity.kt → `requestRuntimePermissions()`

**User-Agent:**
- → WEBVIEW_CONFIG.md → "User-Agent"
- → MainActivity.kt → `getCustomUserAgent()`

**Ciclo de Vida:**
- → QUICK_START.md → "Ciclo de Vida"
- → MainActivity.kt → `onCreate/onResume/onPause/onDestroy`

**Debugging:**
- → BUILD_GUIDE.md → "Debugging"
- → WEBVIEW_CONFIG.md → "Debugging"

### Por Error

**"SDK location not found"**
- → BUILD_GUIDE.md → "Solución de Problemas" → este error

**"Cache no persiste"**
- → WEBVIEW_CONFIG.md → "Solución de Problemas Comunes" → este error

**"Archivos no se suben"**
- → QUICK_START.md → "⚠️ Lo que NO debes hacer"
- → Verificar permisos en tiempo de ejecución

**"No es fullscreen"**
- → QUICK_START.md → "Pantalla Completa"
- → Verificar `WindowCompat.setDecorFitsSystemWindows()`

---

## 📚 Mapa de Documentación

```
START HERE
    ↓
INDEX.md (todo sobre todo)
    ↓
    ├─→ QUICK_START.md (5 min)
    │
    ├─→ BUILD_GUIDE.md (compilar)
    │
    ├─→ CUSTOMIZATION.md (personalizar)
    │
    ├─→ WEBVIEW_CONFIG.md (avanzado)
    │
    ├─→ SUMMARY.md (técnico)
    │
    └─→ CHECKLIST.md (verificar)
```

---

## 🛠️ Herramientas Necesarias

| Herramienta | Versión | Instalación |
|-------------|---------|-------------|
| Android Studio | 2024.1+ | https://developer.android.com/studio |
| Gradle | 8.13.1 | Incluido en proyecto |
| Java/JDK | 11+ | https://adoptopenjdk.net |
| Android SDK | 26+ | Android Studio SDK Manager |
| ADB | Incluido | Con Android SDK |

Ver **[BUILD_GUIDE.md](BUILD_GUIDE.md)** → "Requisitos Previos"

---

## 🐛 Debugging

### Ver Logs
```bash
adb logcat | grep MainActivity
adb logcat | grep WebView
adb logcat | grep Exception
```

### Capturar Logs a Archivo
```bash
adb logcat > logs.txt
# Luego enviar para análisis
```

### Chrome DevTools
```bash
# En MainActivity.kt, habilitar:
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    WebView.setWebContentsDebuggingEnabled(true)
}

# Luego ir a: chrome://inspect/#devices
```

---

## 📞 Contacto

**Reportar Bugs:**
1. Recopilar logs: `adb logcat`
2. Documentar pasos
3. Incluir versión Android
4. Enviar al equipo

**Solicitar Features:**
1. Describir en detalle
2. Explicar caso de uso
3. Esperar respuesta

---

## 🎓 Recursos Externos

### Documentación Oficial
- [Android WebView](https://developer.android.com/reference/android/webkit/WebView)
- [WindowInsetsController](https://developer.android.com/reference/androidx/core/view/WindowInsetsControllerCompat)
- [ActivityResultContracts](https://developer.android.com/training/basics/intents/result)
- [CookieManager](https://developer.android.com/reference/android/webkit/CookieManager)

### Tutoriales
- [Android WebView Tutorial](https://developer.android.com/guide/webapps/webview)
- [Material Design Guidelines](https://material.io/design)
- [Android Performance](https://developer.android.com/topic/performance)

### Comunidad
- [Stack Overflow - Android](https://stackoverflow.com/questions/tagged/android)
- [Android Developers Blog](https://android-developers.googleblog.com)
- [Reddit r/androiddev](https://reddit.com/r/androiddev)

---

## ✅ Checklist de Ayuda

Si tienes un problema:

- [ ] Leíste [INDEX.md](INDEX.md)?
- [ ] Buscaste en [BUILD_GUIDE.md](BUILD_GUIDE.md) → "Solución de Problemas"?
- [ ] Revisaste [WEBVIEW_CONFIG.md](WEBVIEW_CONFIG.md) → "Solución de Problemas Comunes"?
- [ ] Verificaste logs con `adb logcat`?
- [ ] Intentaste `./gradlew clean`?
- [ ] Verificaste que Java esté instalado (`java -version`)?
- [ ] Verificaste que SDKs están instalados?
- [ ] Leíste los comentarios en el código relevante?

Si aún tienes problemas, contacta al equipo con:
1. Descripción clara del problema
2. Pasos para reproducir
3. Logs capturados
4. Versión Android y dispositivo

---

## 🎯 Próximos Pasos

**Ya instalaste la app? Ahora:**

1. Prueба navegación
2. Prueba subida de archivos
3. Cierra y reabre (verifica sesión)
4. Sin conexión (verifica cache)

**¿Todo funciona?**

5. Personaliza según necesidades
6. Lee [CUSTOMIZATION.md](CUSTOMIZATION.md)
7. Prepara para Google Play

---

## 📋 Resumen Rápido

| Pregunta | Respuesta | Referencia |
|----------|-----------|-----------|
| ¿Dónde empiezo? | INDEX.md | [👉](INDEX.md) |
| ¿Cómo compilo? | BUILD_GUIDE.md | [👉](BUILD_GUIDE.md) |
| ¿Cómo personalizo? | CUSTOMIZATION.md | [👉](CUSTOMIZATION.md) |
| ¿Tengo un error? | BUILD_GUIDE.md → Solución de Problemas | [👉](BUILD_GUIDE.md) |
| ¿Es listo para producción? | Sí, SUMMARY.md | [👉](SUMMARY.md) |

---

## 🎉 ¡Gracias!

Espero que disfrutes trabajando con Venice AI WebApp.

Si tienes más preguntas, consulta la **documentación completa** incluida.

**Happy coding! 🚀**

---

**Versión:** 1.0.0  
**Última actualización:** Diciembre 2024  
**Estado:** Completo y listo para usar

