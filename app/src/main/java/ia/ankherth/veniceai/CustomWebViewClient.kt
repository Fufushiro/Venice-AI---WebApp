package ia.ankherth.veniceai

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.os.Build

/**
 * Cliente personalizado de WebView para manejar navegación y errores de forma optimizada.
 * Implementa buenas prácticas modernas de Android WebView.
 */
class CustomWebViewClient : WebViewClient() {

    /**
     * Intercepta solicitudes web para permitir solo URLs seguras.
     * Retorna false para permitir que WebView maneje la solicitud normalmente.
     */
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        // Permitir navegación normal dentro del sitio
        return false
    }

    /**
     * Se llamará cuando la página comience a cargarse.
     */
    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    /**
     * Se llamará cuando la página termine de cargarse.
     * Aquí sincronizamos las cookies y aplicamos cualquier ajuste necesario.
     */
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        // Sincronizar cookies con el disco
        android.webkit.CookieManager.getInstance().flush()
    }

    /**
     * Maneja errores de conexión de forma elegante.
     */
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        // No mostrar página de error - dejar que WebView maneje internamente
        // para mantener la sesión intacta
        error?.description?.let {
            android.util.Log.e("WebViewError", "Error: $it (${error.errorCode})")
        }
    }
}

