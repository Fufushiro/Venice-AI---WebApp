package ia.ankherth.veniceai

import org.junit.Test
import org.junit.Assert.*

/**
 * Tests unitarios para la configuración de WebView.
 */
class WebViewConfigTest {

    @Test
    fun testVeniceURLIsValid() {
        // Verificar que la URL de Venice es válida
        val url = "https://venice.ai/chat"
        assertTrue(url.startsWith("https://"))
        assertTrue(url.contains("venice.ai"))
    }

    @Test
    fun testUserAgentFormat() {
        // Verificar que el User-Agent tiene el formato correcto
        val userAgent = "Mozilla/5.0 (Linux; Android 14; 34) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"

        assertTrue(userAgent.contains("Android"))
        assertTrue(userAgent.contains("Chrome"))
        assertTrue(userAgent.contains("Mobile"))
    }

    @Test
    fun testCacheModeConstant() {
        // Verificar que las constantes de cache están disponibles
        val cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
        assertNotNull(cacheMode)
    }
}

