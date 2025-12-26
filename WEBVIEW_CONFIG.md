# Configuración Avanzada de WebView - Venice AI

## 1. Cache y Almacenamiento

### Configuración de Cache
```kotlin
webView.settings.apply {
    cacheMode = WebSettings.LOAD_DEFAULT  // Usa cache si está disponible
    setAppCacheEnabled(true)               // Habilita app cache
    databaseEnabled = true                 // Habilita storage de datos
    domStorageEnabled = true               // Habilita DOM storage
    databasePath = filesDir.path           // Ruta de base de datos
}
```

### Comportamiento del Cache
- **Primera carga**: Descarga de la red y almacena en cache local
- **Cargas posteriores**: Usa cache local si está disponible
- **Sin conexión**: Muestra contenido del cache
- **Sesión persistente**: Los datos se mantienen entre reinicios

### Almacenamiento Persistente
```
/data/data/ia.ankherth.veniceai/
├── cache/
│   └── webview_cache/          # Cache de recursos web
├── databases/
│   └── webview.db              # Base de datos del WebView
└── shared_prefs/               # SharedPreferences de la app
```

## 2. Gestión de Cookies

### CookieManager Configuration
```kotlin
val cookieManager = CookieManager.getInstance()
cookieManager.apply {
    setAcceptCookie(true)                      // Aceptar cookies
    setAcceptThirdPartyCookies(webView, true)  // Cookies de terceros
}

// Sincronizar al terminar de cargar
CookieManager.getInstance().flush()
```

### Cookies Persistentes
- Almacenadas en `WebViewDatabase`
- Recuperadas automáticamente al reiniciar
- No se limpian a menos que se ejecute `clearCookies()`

## 3. Service Workers y Cache Avanzado

### Service Workers (Android 7.0+)
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    ServiceWorkerController.getInstance().setServiceWorkerClient(null)
}
```

### Ventajas
- Cache inteligente de recursos
- Funcionamiento offline
- Sincronización en segundo plano
- Mejor rendimiento

## 4. User-Agent Personalizado

### User-Agent por defecto (Generado dinámicamente)
```
Mozilla/5.0 (Linux; Android 14; 34) AppleWebKit/537.36 
(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36
```

### Ventajas
- Simula Chrome móvil real
- Compatibilidad máxima con Venice AI
- Reconocimiento como navegador móvil
- Acceso a APIs específicas de móvil

## 5. Pantalla Completa Moderna

### APIs Utilizadas
```kotlin
// Android 5.0+
WindowCompat.setDecorFitsSystemWindows(window, false)

// Android 11+ (Recomendado)
val controller = WindowInsetsControllerCompat(window, window.decorView)
controller.hide(WindowInsetsCompat.Type.systemBars())
controller.systemBarsBehavior = 
    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
```

### Características
- Status bar oculto
- Navigation bar oculto
- WebView ocupa 100% de pantalla
- Barras reaparecen con swipe desde arriba/abajo

## 6. Manejo de Archivo y Permisos

### Selección de Archivos
```kotlin
override fun onShowFileChooser(
    webView: WebView?,
    filePathCallback: ValueCallback<Array<Uri>>?,
    fileChooserParams: FileChooserParams?
): Boolean {
    fileChooserLauncher.launch(
        fileChooserParams?.acceptTypes?.joinToString(",") ?: "*/*"
    )
    return true
}
```

### Tipos Soportados
- Imágenes: `image/*`
- Video: `video/*`
- Audio: `audio/*`
- Documentos: `application/*`
- Cualquier archivo: `*/*`

### Permisos Runtime (Android 6.0+)
```kotlin
val requiredPermissions = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

permissionLauncher.launch(requiredPermissions)
```

## 7. Ciclo de Vida y Gestión de Memoria

### onCreate()
- Configuración inicial
- Carga de URL (UNA SOLA VEZ)
- No recargar en rotación de pantalla

### onPause()
```kotlin
override fun onPause() {
    super.onPause()
    webView.onPause()  // Pausa ejecución de JS
}
```

### onResume()
```kotlin
override fun onResume() {
    super.onResume()
    webView.onResume()  // Reanuda, NO recarga URL
}
```

### onDestroy()
```kotlin
override fun onDestroy() {
    CookieManager.getInstance().flush()  // Sincronizar
    webView.apply {
        onPause()
        removeAllViews()
        destroy()  // Limpieza completa
    }
    super.onDestroy()
}
```

## 8. Rendimiento y Optimizaciones

### Carga Eficiente
- URL cargada **UNA SOLA VEZ** en `onCreate()`
- En `onResume()`: Solo `onResume()`, sin recargar
- Cache utilizado en cargas posteriores

### Gestión de Memoria
- WebView destruido completamente en `onDestroy()`
- No retención en memory leaks
- Garbage collection automático

### Configuraciones de Rendimiento
```kotlin
webView.settings.apply {
    builtInZoomControls = false      // Sin botones de zoom
    displayZoomControls = false      // Sin controles en pantalla
    mediaPlaybackRequiresUserGesture = false  // Video autoplay
}
```

## 9. Debugging

### Logs de la Aplicación
```bash
adb logcat | grep "MainActivity\|WebView\|WebChromeClient"
```

### Logs en Código
```kotlin
Log.d(TAG, "Cargando URL: $VENICE_URL")
Log.d(TAG, "WebView paused")
Log.d(TAG, "WebView resumed - Session maintained")
Log.e("WebViewError", "Error: $description (${error.errorCode})")
```

### Chrome DevTools (Depuración remota)
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    WebView.setWebContentsDebuggingEnabled(true)
}
```

Luego en Chrome:
1. Ir a `chrome://inspect/#devices`
2. Seleccionar el dispositivo/emulador
3. Inspeccionar la WebView

## 10. Seguridad

### Configuraciones de Seguridad
- ✅ `usesCleartextTraffic="false"` - Solo HTTPS
- ✅ `JavaScript habilitado` - Necesario para Venice
- ✅ Contenido mixto permitido - Para compatibilidad
- ✅ Sin acceso a archivos locales sensibles

### Best Practices
- No ejecutar código no verificado
- Validar URLs antes de cargar
- Usar HTTPS siempre que sea posible
- Mantener Android actualizado

## 11. Testing

### Unit Tests
```kotlin
// app/src/test/java/ia/ankherth/veniceai/
```

### Integration Tests
```kotlin
// app/src/androidTest/java/ia/ankherth/veniceai/
```

### Pruebas Manuales Recomendadas
1. ✓ Primera carga de la URL
2. ✓ Navegación atrás y adelante
3. ✓ Subida de archivos
4. ✓ Pausa y reanudación de la app
5. ✓ Rotación de pantalla
6. ✓ Reinicio de la app (sesión persistente)
7. ✓ Modo sin conexión (cache)
8. ✓ Conexión a WiFi/datos

## 12. Configuración de Aplicación

### strings.xml
```xml
<string name="app_name">Venice AI</string>
```

### themes.xml (Light)
```xml
<style name="Theme.VeniceAI" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryDark">@color/primaryDark</item>
    <item name="colorAccent">@color/accent</item>
</style>
```

### themes.xml (Dark)
```xml
<style name="Theme.VeniceAI" parent="Theme.AppCompat">
    <!-- Colores para modo oscuro -->
</style>
```

## 13. Solución de Problemas Comunes

### Problema: Cache no persiste
**Solución:**
- Verificar `cacheMode = WebSettings.LOAD_DEFAULT`
- No ejecutar `webView.clearCache(true)`
- Verificar permisos de almacenamiento

### Problema: Sesión se pierde
**Solución:**
- NO recargar URL en `onResume()`
- Asegurar `CookieManager.setAcceptCookie(true)`
- Sincronizar cookies en `onDestroy()`

### Problema: Archivos no se suben
**Solución:**
- Permisos concedidos en tiempo de ejecución
- `onShowFileChooser()` retorna `true`
- `ActivityResultLauncher` registrado correctamente

### Problema: Pantalla no es fullscreen
**Solución:**
- `WindowCompat.setDecorFitsSystemWindows(window, false)`
- `WindowInsetsControllerCompat` configurado correctamente
- Verificar tema sin ActionBar

---

**Última actualización:** Diciembre 2024

