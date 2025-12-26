package ia.ankherth.veniceai

import android.content.Context
import android.webkit.WebView
import java.io.File

/**
 * Gestor centralizado de configuración de WebView.
 * Maneja cache, cookies, almacenamiento y otras configuraciones.
 */
object WebViewConfigManager {

    /**
     * Configura el almacenamiento de la aplicación para WebView
     */
    fun setupWebViewStorage(context: Context) {
        try {
            // Crear directorios necesarios si no existen
            val cacheDir = File(context.cacheDir, "webview_cache")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val dataDir = File(context.filesDir, "webview_data")
            if (!dataDir.exists()) {
                dataDir.mkdirs()
            }

            // Las rutas se utilizarán en WebSettings durante la configuración
            android.util.Log.d("WebViewConfig", "Storage paths created: $cacheDir, $dataDir")
        } catch (e: Exception) {
            android.util.Log.e("WebViewConfig", "Error setting up storage", e)
        }
    }

    /**
     * Limpia el cache SOLO si es necesario (no se recomienda durante sesiones activas)
     * Esta función es defensiva y NO se usa en la app normal
     */
    @Deprecated("No usar en producción - mantiene el cache persistente")
    fun clearCache(webView: WebView) {
        webView.clearCache(true)
        android.util.Log.d("WebViewConfig", "Cache cleared (not recommended)")
    }
}

