package ia.ankherth.veniceai

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.ValueCallback
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import android.util.Log

/**
 * Cliente Chrome personalizado para manejar:
 * - Selección de archivos (input type="file")
 * - Acceso a cámara
 * - Progreso de carga
 * - Solicitudes de permisos
 */
class CustomWebChromeClient(
    private val fileChooserLauncher: ActivityResultLauncher<String>
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    /**
     * Maneja la selección de archivos cuando la página web solicita <input type="file">
     */
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        // Cancelar cualquier solicitud anterior pendiente
        this.filePathCallback?.onReceiveValue(null)
        this.filePathCallback = filePathCallback

        try {
            fileChooserLauncher.launch(
                fileChooserParams?.acceptTypes?.joinToString(",") ?: "*/*"
            )
            return true
        } catch (e: Exception) {
            Log.e("WebChromeClient", "Error launching file chooser", e)
            filePathCallback?.onReceiveValue(null)
            this.filePathCallback = null
            return false
        }
    }

    /**
     * Maneja cambios en el progreso de carga
     */
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        // El progreso puede usarse para mostrar un indicador de carga si es necesario
        Log.d("WebChromeClient", "Progress: $newProgress%")
    }

    /**
     * Maneja títulos dinámicos de la página
     */
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        // El título puede actualizarse en la barra de la aplicación si es necesario
    }

    /**
     * Completa la solicitud de archivo con el URI seleccionado
     */
    fun onFileSelected(uri: Uri?) {
        if (uri != null) {
            filePathCallback?.onReceiveValue(arrayOf(uri))
        } else {
            filePathCallback?.onReceiveValue(null)
        }
        filePathCallback = null
    }

    /**
     * Completa la solicitud de archivo cuando se cancela
     */
    fun onFileSelectionCancelled() {
        filePathCallback?.onReceiveValue(null)
        filePathCallback = null
    }
}

