# 🎨 Guía de Personalización - Venice AI WebApp

## Personalización de la Interfaz

### 1. Cambiar Nombre de la Aplicación

**Archivo:** `app/src/main/res/values/strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Venice AI Chat</string>
    <!-- Puedes agregar más strings aquí -->
</resources>
```

### 2. Cambiar Esquema de Colores

**Archivo:** `app/src/main/res/values/colors.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">#6366F1</color>          <!-- Color principal -->
    <color name="primaryDark">#4F46E5</color>      <!-- Variante oscura -->
    <color name="accent">#A78BFA</color>           <!-- Color de énfasis -->
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>
</resources>
```

**Archivo:** `app/src/main/res/values/themes.xml` (Light Theme)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.VeniceAI" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryDark">@color/primaryDark</item>
        <item name="colorAccent">@color/accent</item>
    </style>
</resources>
```

**Archivo:** `app/src/main/res/values-night/themes.xml` (Dark Theme)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.VeniceAI" parent="Theme.AppCompat">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryDark">@color/primaryDark</item>
        <item name="colorAccent">@color/accent</item>
    </style>
</resources>
```

### 3. Cambiar Icono de la Aplicación

Reemplazar archivos en:
- `app/src/main/res/mipmap-hdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-mdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-xhdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-xxhdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp`

**Recomendaciones:**
- Formato: PNG o WebP
- Tamaño base: 192x192 px (para xxxhdpi)
- Sin transparencia necesaria (se genera automáticamente)

---

## Personalización de Comportamiento

### 1. Cambiar URL de Carga

**Archivo:** `MainActivity.kt`

```kotlin
// Buscar y cambiar esta línea:
private val VENICE_URL = "https://venice.ai/chat"

// Ejemplos:
private val VENICE_URL = "https://tu-sitio.com"
private val VENICE_URL = "https://app.example.com/dashboard"
private val VENICE_URL = "http://localhost:3000"  // Desarrollo
```

### 2. Personalizar User-Agent

**Archivo:** `MainActivity.kt` - Método `getCustomUserAgent()`

```kotlin
private fun getCustomUserAgent(): String {
    val androidVersion = Build.VERSION.RELEASE
    val apiLevel = Build.VERSION.SDK_INT
    
    // Opción 1: Custom completo
    return "MyApp/1.0 (Linux; Android $androidVersion; API $apiLevel)"
    
    // Opción 2: Simular navegador específico
    return "Mozilla/5.0 (iPad; CPU OS 13_0 like Mac OS X) AppleWebKit/605.1.15"
    
    // Opción 3: Por defecto (Chrome móvil)
    return "Mozilla/5.0 (Linux; Android $androidVersion; $apiLevel) AppleWebKit/537.36"
}
```

### 3. Habilitar/Deshabilitar Fullscreen

**Archivo:** `MainActivity.kt` - Método `setupFullscreenMode()`

```kotlin
private fun setupFullscreenMode() {
    // Opción 1: Fullscreen completo (por defecto)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    
    // Opción 2: Mostrar barras del sistema
    WindowCompat.setDecorFitsSystemWindows(window, true)
}
```

### 4. Personalizar Comportamiento del Cache

**Archivo:** `MainActivity.kt` - Método `configureWebView()`

```kotlin
webView.settings.apply {
    // Opción 1: Cache siempre (por defecto)
    cacheMode = WebSettings.LOAD_DEFAULT
    
    // Opción 2: Cache si está disponible, sino de red
    cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
    
    // Opción 3: Siempre usar cache
    cacheMode = WebSettings.LOAD_CACHE_ONLY
    
    // Opción 4: Siempre desde red (sin cache)
    cacheMode = WebSettings.LOAD_NO_CACHE
}
```

### 5. Personalizar Timeout de Carga

**Archivo:** `CustomWebViewClient.kt`

Agregar timeout personalizado:

```kotlin
class CustomWebViewClient : WebViewClient() {
    
    private val LOAD_TIMEOUT_MS = 30000  // 30 segundos
    
    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
        
        // Implementar timeout manual si es necesario
        view?.postDelayed({
            // Detener carga si no se completó
        }, LOAD_TIMEOUT_MS.toLong())
    }
}
```

---

## Personalización de JavaScript

### 1. Inyectar JavaScript Personalizado

**Archivo:** `MainActivity.kt` - En `configureWebView()`

```kotlin
webView.apply {
    settings.javaScriptEnabled = true
    
    // Agregar después de loadUrl()
    webView.evaluateJavascript("""
        console.log("Venice AI WebApp cargada");
        // Inyectar variables globales
        window.appVersion = "1.0.0";
        window.appName = "VeniceAI";
    """.trimIndent(), null)
}
```

### 2. Crear Interfaz Java-JavaScript

Crear nuevo archivo `JavaScriptInterface.kt`:

```kotlin
package ia.ankherth.veniceai

import android.webkit.JavascriptInterface
import android.content.Context

class JavaScriptInterface(private val context: Context) {
    
    @JavascriptInterface
    fun getAppVersion(): String = "1.0.0"
    
    @JavascriptInterface
    fun showToast(message: String) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
```

Luego en `MainActivity.kt`:

```kotlin
webView.addJavascriptInterface(JavaScriptInterface(this), "Android")
```

Desde JavaScript en la página web:

```javascript
// Llamar método Java desde JavaScript
Android.showToast("Mensaje desde web");
console.log("Versión: " + Android.getAppVersion());
```

---

## Personalización de Permisos

### 1. Agregar Permisos Adicionales

**Archivo:** `AndroidManifest.xml`

```xml
<!-- Agregar dentro de <manifest> antes de </manifest> -->

<!-- Permisos personalizados -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.NFC" />
<uses-permission android:name="android.permission.VIBRATE" />

<!-- O remover permisos no necesarios -->
<!-- <uses-permission android:name="android.permission.CAMERA" /> -->
```

### 2. Personalizar Runtime Permissions

**Archivo:** `MainActivity.kt` - Método `requestRuntimePermissions()`

```kotlin
private fun requestRuntimePermissions() {
    val requiredPermissions = mutableListOf(
        Manifest.permission.INTERNET,
        // Agregar solo los que necesites
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    
    // Remover permisos no necesarios
    // requiredPermissions.remove(Manifest.permission.CAMERA)
    
    val permissionsToRequest = requiredPermissions.filter {
        ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    }
    
    if (permissionsToRequest.isNotEmpty()) {
        permissionLauncher.launch(permissionsToRequest.toTypedArray())
    }
}
```

---

## Personalización de Tipografía

**Archivo:** `app/src/main/res/values/styles.xml`

Agregar custom TextAppearance:

```xml
<style name="TextAppearance.Custom.Title" parent="TextAppearance.AppCompat.Headline">
    <item name="android:fontFamily">@font/custom_font</item>
    <item name="android:textSize">24sp</item>
    <item name="android:textColor">@color/primary</item>
</style>
```

---

## Personalización Avanzada

### 1. Agregar Splash Screen (Android 12+)

Crear `SplashActivity.kt`:

```kotlin
package ia.ankherth.veniceai

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Mostrar splash screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        
        // Iniciar MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
```

### 2. Agregar Notificaciones Push

Integrar Firebase Cloud Messaging:

```bash
# En build.gradle.kts
implementation("com.google.firebase:firebase-messaging:23.2.1")
```

Crear `MyFirebaseMessagingService.kt`:

```kotlin
package ia.ankherth.veniceai

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Manejar notificaciones
    }
}
```

### 3. Agregar Análiticas (Google Analytics)

```bash
# En build.gradle.kts
implementation("com.google.firebase:firebase-analytics-ktx:21.2.2")
```

En `MainActivity.kt`:

```kotlin
import com.google.firebase.analytics.FirebaseAnalytics

val analytics = FirebaseAnalytics.getInstance(this)
val bundle = Bundle().apply {
    putString("url_loaded", VENICE_URL)
}
analytics.logEvent("app_opened", bundle)
```

---

## Variables de Entorno

### Crear `BuildConfig` Personalizado

**Archivo:** `build.gradle.kts`

```kotlin
android {
    // ...
    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"https://dev.example.com\"")
            buildConfigField("Boolean", "DEBUG_LOGS", "true")
        }
        release {
            buildConfigField("String", "API_URL", "\"https://api.example.com\"")
            buildConfigField("Boolean", "DEBUG_LOGS", "false")
        }
    }
}
```

Usar en código:

```kotlin
val apiUrl = BuildConfig.API_URL
val debugEnabled = BuildConfig.DEBUG_LOGS
```

---

## Testing Personalizado

### Crear Tests Personalizados

**Archivo:** `app/src/test/java/.../CustomTest.kt`

```kotlin
package ia.ankherth.veniceai

import org.junit.Test
import org.junit.Assert.*

class CustomTest {
    
    @Test
    fun testCustomBehavior() {
        // Tus tests aquí
        assertTrue(true)
    }
}
```

Ejecutar:

```bash
./gradlew test
```

---

## Compilación Personalizada

### Crear Múltiples Sabores (Flavors)

**Archivo:** `build.gradle.kts`

```kotlin
android {
    // ...
    flavorDimensions += "environment"
    
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("prod") {
            dimension = "environment"
            applicationIdSuffix = ""
        }
    }
}
```

Compilar variantes:

```bash
# Debug development
./gradlew assembleDevDebug

# Release production
./gradlew assembleProdRelease
```

---

## Checklist de Personalización

- ✓ Nombre de aplicación
- ✓ Colores y temas
- ✓ Icono de aplicación
- ✓ URL de carga
- ✓ User-Agent personalizado
- ✓ Modo fullscreen
- ✓ Configuración de cache
- ✓ Permisos requeridos
- ✓ JavaScript personalizado
- ✓ Tipografía custom

---

**Última actualización:** Diciembre 2024  
**Versión:** 1.0

