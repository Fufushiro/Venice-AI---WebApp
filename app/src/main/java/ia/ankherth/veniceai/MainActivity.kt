package ia.ankherth.veniceai

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.util.Log

/**
 * MainActivity - Actividad principal que implementa WebView optimizada para Venice AI Chat.
 *
 * Características:
 * - Cache persistente en disco
 * - Sesión mantiene entre reinicios
 * - Pantalla completa con APIs modernas
 * - Manejo correcto de permisos en tiempo de ejecución
 * - Service Workers habilitados
 * - Subida de archivos completamente funcional
 * - Navegación inteligente con manejo del botón atrás
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var customWebChromeClient: CustomWebChromeClient
    private val TAG = "MainActivity"

    // URL de Venice AI
    private val VENICE_URL = "https://venice.ai/chat"
    
    // Variables para manejo de IME
    private var rootContainer: ViewGroup? = null

    // Launcher para selección de archivos
    private val fileChooserLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        customWebChromeClient.onFileSelected(uri)
    }

    // Launcher para permisos en tiempo de ejecución (Android 6.0+)
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            Log.d(TAG, "Todos los permisos fueron concedidos")
        } else {
            Log.d(TAG, "Algunos permisos fueron denegados")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer layout
        setContentView(R.layout.activity_main)

        // Configurar almacenamiento de WebView
        WebViewConfigManager.setupWebViewStorage(this)

        // Configurar pantalla completa
        setupFullscreenMode()
        
        // Obtener referencia al contenedor raíz
        rootContainer = findViewById(R.id.rootContainer)

        // Solicitar permisos necesarios
        requestRuntimePermissions()

        // Inicializar WebView
        webView = findViewById(R.id.webView)
        configureWebView()
        
        // Configurar escucha de insets del IME
        setupWindowInsetsListener()

        // Manejar botón atrás
        setupBackPressedCallback()

        // Cargar la URL SOLO una vez en onCreate
        if (savedInstanceState == null) {
            webView.loadUrl(VENICE_URL)
            Log.d(TAG, "Cargando URL: $VENICE_URL")
        }
    }

    /**
     * Configura el WebView con todas las optimizaciones necesarias
     */
    private fun configureWebView() {
        // Inicializar WebSettings
        webView.settings.apply {
            // JavaScript habilitado - REQUERIDO para Venice AI
            javaScriptEnabled = true

            // Storage habilitado
            domStorageEnabled = true
            databaseEnabled = true

            // Cache en disco persistente (API moderno)
            cacheMode = WebSettings.LOAD_DEFAULT

            // User-Agent personalizado para simular Chrome móvil real
            userAgentString = getCustomUserAgent()

            // Configuración de rendimiento
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // Permitir acceso a archivos locales (para cache y datos)
            allowFileAccess = true
            allowContentAccess = true

            // Media playback sin sonido por defecto
            mediaPlaybackRequiresUserGesture = false

            // Configuración de zoom
            builtInZoomControls = false
            displayZoomControls = false
        }

        // Configurar CookieManager para persistencia
        val cookieManager = CookieManager.getInstance()
        cookieManager.apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }

        // Asignar clientes personalizados
        webView.webViewClient = CustomWebViewClient()
        customWebChromeClient = CustomWebChromeClient(fileChooserLauncher)
        webView.webChromeClient = customWebChromeClient
    }

    /**
     * Obtiene un User-Agent personalizado para simular Chrome móvil en Android 13+
     */
    private fun getCustomUserAgent(): String {
        val androidVersion = Build.VERSION.RELEASE
        val apiLevel = Build.VERSION.SDK_INT

        // User-Agent basado en Chrome real para Android
        return "Mozilla/5.0 (Linux; Android $androidVersion; $apiLevel) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
    }

    /**
     * Configura el modo pantalla completa usando APIs modernas
     */
    private fun setupFullscreenMode() {
        // Usar WindowCompat para máxima compatibilidad
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30+ - Usar WindowInsetsControllerCompat
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            // API 26-29 - Usar flags modernos (sin SYSTEM_UI_FLAG_FULLSCREEN)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }

    /**
     * Configura escucha de WindowInsets para manejar el IME (teclado)
     * Ajusta el padding del WebView cuando el teclado aparece/desaparece
     */
    private fun setupWindowInsetsListener() {
        rootContainer?.let { container ->
            ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
                // Obtener el alto del IME (teclado)
                val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
                val imeHeight = imeInsets.bottom
                
                // Aplicar padding inferior al contenedor
                // Esto hace que el WebView se reduzca cuando el teclado aparece
                view.setPadding(0, 0, 0, imeHeight)
                
                Log.d(TAG, "IME Height: $imeHeight, Bottom: ${imeInsets.bottom}")
                
                // Retornar insets consumidos para que no se propaguen
                insets
            }
        }
    }

    /**
     * Maneja cuando la ventana obtiene/pierde foco
     * Reaplicar fullscreen cuando la ventana recupera foco (ej: después de mostrar el teclado)
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // Reaplicar configuración fullscreen cuando la ventana recupera foco
            setupFullscreenMode()
            Log.d(TAG, "Window focus regained, fullscreen reapplied")
        }
    }

    /**
     * Solicita permisos en tiempo de ejecución necesarios
     */
    private fun requestRuntimePermissions() {
        val requiredPermissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Agregar permisos de almacenamiento en Android 12 o anterior
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // Filtrar solo permisos que no hayan sido concedidos
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    /**
     * Configura el manejo del botón atrás
     */
    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Si el WebView puede retroceder, ir atrás
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    // Si no puede retroceder, cerrar la app
                    finish()
                }
            }
        })
    }

    /**
     * Manejo del ciclo de vida - Pausar WebView
     */
    override fun onPause() {
        super.onPause()
        webView.onPause()
        Log.d(TAG, "WebView paused")
    }

    /**
     * Manejo del ciclo de vida - Reanudar WebView
     * NO recargamos la URL aquí para mantener la sesión
     */
    override fun onResume() {
        super.onResume()
        webView.onResume()
        // Reaplicar fullscreen en onResume para garantizar que se mantenga
        setupFullscreenMode()
        Log.d(TAG, "WebView resumed - Session maintained, fullscreen reapplied")
    }

    /**
     * Manejo del ciclo de vida - Destruir WebView limpiar memoria
     */
    override fun onDestroy() {
        // Sincronizar cookies antes de destruir
        CookieManager.getInstance().flush()

        // Pausar y destruir WebView
        webView.apply {
            onPause()
            removeAllViews()
            destroy()
        }

        super.onDestroy()
        Log.d(TAG, "WebView destroyed and cleaned up")
    }
}

