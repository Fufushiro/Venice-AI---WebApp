# Venice AI - Aplicación Android WebView Nativa

## Descripción

Aplicación Android nativa en Kotlin que funciona como un WebView optimizado para Venice AI Chat (https://venice.ai/chat). Implementa todas las mejores prácticas modernas de Android WebView con enfoque en:

- **Cache persistente en disco**
- **Sesión persistente entre reinicios**
- **Pantalla completa moderna**
- **Subida de archivos funcional**
- **Rendimiento optimizado**

## Requisitos de Compilación

- **Android Studio**: 2024.1 o superior
- **Android SDK**: Mínimo API 26 (Android 8.0), Target API 36 (Android 15)
- **Java/Kotlin**: JVM 11 o superior
- **Gradle**: 8.13.1 (incluido en el proyecto)

## Estructura del Proyecto

```
app/src/main/
├── java/ia/ankherth/veniceai/
│   ├── MainActivity.kt                    # Actividad principal
│   ├── CustomWebViewClient.kt             # Cliente de navegación
│   ├── CustomWebChromeClient.kt           # Cliente para archivos/cámara
│   └── WebViewConfigManager.kt            # Gestor de configuración
├── res/
│   ├── layout/
│   │   └── activity_main.xml              # Layout principal
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   └── themes.xml
│   └── mipmap-*/
│       └── ic_launcher.*
└── AndroidManifest.xml                    # Configuración de app
```

## Compilar la Aplicación

### Opción 1: Android Studio
1. Abrir el proyecto en Android Studio
2. Esperar a que Gradle se sincronice
3. Seleccionar "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
4. El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### Opción 2: Terminal (Gradle)

```bash
# Compilar APK de debug
./gradlew assembleDebug

# Compilar APK de release
./gradlew assembleRelease

# Instalar en dispositivo conectado (debug)
./gradlew installDebug

# Compilar y ejecutar en dispositivo
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Características Implementadas

### 1. **WebView Optimizado**
- ✅ Cache en disco persistente (`LOAD_DEFAULT`)
- ✅ Storage DOM y Database habilitados
- ✅ CookieManager configurado para persistencia
- ✅ ServiceWorkers habilitados (Android 7.0+)
- ✅ User-Agent personalizado (Chrome móvil Android 13+)
- ✅ JavaScript habilitado y optimizado

### 2. **Pantalla Completa**
- ✅ APIs modernas `WindowInsetsControllerCompat` (Android 5.0+)
- ✅ Status bar y navigation bar ocultados completamente
- ✅ WebView ocupa 100% de la pantalla
- ✅ Manejo correcto del teclado (IME)

### 3. **Manejo de Archivos**
- ✅ Selección de archivos mediante `onShowFileChooser`
- ✅ Soporte para imágenes, videos y documentos
- ✅ Acceso a cámara si la web lo solicita
- ✅ `ActivityResultLauncher` para selección segura

### 4. **Navegación**
- ✅ Botón atrás: retrocede si es posible, cierra si no
- ✅ Manejo correcto del ciclo de vida
- ✅ NO recarga en `onResume()` (sesión persistente)

### 5. **Permisos**
- ✅ Permisos en manifiesto correctamente declarados
- ✅ Manejo de permisos en tiempo de ejecución (Runtime Permissions)
- ✅ Compatible con Android 8.0 a 15.0

### 6. **Rendimiento**
- ✅ URL cargada UNA SOLA VEZ en `onCreate()`
- ✅ `onPause()` y `onResume()` optimizados
- ✅ Destrucción limpia en `onDestroy()`
- ✅ Sin retención en memoria innecesaria

## Configuración de Permisos

Los siguientes permisos están declarados en `AndroidManifest.xml`:

- **INTERNET** - Acceso a red (requerido)
- **ACCESS_NETWORK_STATE** - Estado de red
- **CAMERA** - Cámara para capturas de video
- **RECORD_AUDIO** - Micrófono para videollamadas
- **ACCESS_COARSE_LOCATION** - Ubicación aproximada
- **ACCESS_FINE_LOCATION** - GPS
- **READ_EXTERNAL_STORAGE** - Leer archivos (Android ≤ 12)
- **WRITE_EXTERNAL_STORAGE** - Escribir archivos (Android ≤ 12)

**Nota:** A partir de Android 13, los permisos de almacenamiento se manejan automáticamente.

## Instalación en Dispositivo

### Dispositivo físico vía USB

```bash
# Habilitar depuración en el dispositivo (Configuración → Opciones de desarrollo)
adb devices  # Verificar que el dispositivo aparezca

# Instalar la aplicación
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Ejecutar la aplicación
adb shell am start -n ia.ankherth.veniceai/.MainActivity
```

### Emulador

```bash
# Ver emuladores disponibles
emulator -list-avds

# Iniciar emulador
emulator -avd <nombre_emulador>

# Instalar APK
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Ciclo de Vida de WebView

### `onCreate()`
1. Configura almacenamiento de WebView
2. Activa pantalla completa
3. Solicita permisos en tiempo de ejecución
4. Configura WebView (cache, cookies, JS, etc.)
5. **Carga la URL SOLO aquí** - Primera y única vez

### `onResume()`
- Reanuda el WebView (sin recargar URL)
- Mantiene sesión activa

### `onPause()`
- Pausa el WebView
- Conserva estado sin destruir

### `onDestroy()`
- Sincroniza cookies
- Destruye WebView completamente
- Libera memoria

## Troubleshooting

### La app no carga la página
- Verificar conexión a internet
- Comprobar permisos en `AndroidManifest.xml`
- Revisar logs: `adb logcat | grep MainActivity`

### Archivos no se suben
- Asegurar que los permisos estén concedidos en tiempo de ejecución
- En Android 13+, la app automáticamente solicita permisos al usar el file chooser

### La sesión se pierde al pausar
- Verificar que `onResume()` NO recarga la URL
- Verificar que `CookieManager` está configurado correctamente

### Cache no persiste
- Verificar que `cacheMode = WebSettings.LOAD_DEFAULT`
- Verificar que NO se ejecuta `clearCache()`
- Revisar permisos de almacenamiento en el dispositivo

## Código Limpio y Comentado

Cada clase contiene:
- ✅ Documentación con KDoc
- ✅ Comentarios explicativos
- ✅ Buenas prácticas de Android
- ✅ Sin dependencias innecesarias
- ✅ Listo para producción

## Versiones Soportadas

| Versión | API | Soporte |
|---------|-----|---------|
| Android 8.0 | 26 | Mínimo (requiere) |
| Android 8.1 | 27 | Soportado |
| Android 9.0 | 28 | Soportado |
| Android 10 | 29 | Soportado |
| Android 11 | 30 | Soportado |
| Android 12 | 31 | Soportado |
| Android 13 | 33 | Soportado |
| Android 14 | 34 | Soportado |
| Android 15 | 35+ | Soportado (Target) |

## Publicar en Google Play

Para publicar en Google Play:

1. **Generar APK Release firmado:**
   ```bash
   ./gradlew assembleRelease
   ```

2. **Firmar el APK** (si no está configurado automáticamente):
   - Seguir las instrucciones de Android Studio para crear una keystore

3. **Subir a Google Play Console**:
   - Crear cuenta de desarrollador
   - Crear aplicación
   - Subir APK signed release
   - Completar ficha de la app (descripción, screenshots, etc.)

## Licencia

Copyright © 2024. Todos los derechos reservados.

## Contacto y Soporte

Para reportar problemas o sugerencias, contactar al equipo de desarrollo.

---

**Última actualización:** Diciembre 2024
**Versión de la app:** 1.0
**Estado:** Producción

# Venice-AI---WebApp
# Venice-AI---WebApp
