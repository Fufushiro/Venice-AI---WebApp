# Arquitectura Visual - Solución WebView + IME

## 📐 Diagrama de Flujo de la Solución

```
┌─────────────────────────────────────────────────────────────────┐
│                     PANTALLA FÍSICA DEL DISPOSITIVO             │
│                       (Display 1080x2400)                       │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                        ESTADO 1: FULLSCREEN                      │
│                      (Sin teclado visible)                      │
├─────────────────────────────────────────────────────────────────┤
│                    ← setDecorFitsSystemWindows(false) →         │
│                                                                 │
│   ┌───────────────────────────────────────────────────────┐   │
│   │            Window Layout (match_parent)                │   │
│   │   ┌─────────────────────────────────────────────────┐ │   │
│   │   │      FrameLayout (rootContainer)                │ │   │
│   │   │   ┌─────────────────────────────────────────┐   │ │   │
│   │   │   │    WebView (100% de altura)             │   │ │   │
│   │   │   │                                         │   │ │   │
│   │   │   │   ┌─────────────────────────────────┐  │   │ │   │
│   │   │   │   │ Venice AI Chat Interface       │  │   │ │   │
│   │   │   │   │ - Texto                        │  │   │ │   │
│   │   │   │   │ - Inputs                       │  │   │ │   │
│   │   │   │   │ - Botones                      │  │   │ │   │
│   │   │   │   └─────────────────────────────────┘  │   │ │   │
│   │   │   │                                         │   │ │   │
│   │   │   │ Padding = 0 (sin teclado)             │   │ │   │
│   │   │   │                                         │   │ │   │
│   │   │   └─────────────────────────────────────────┘   │ │   │
│   │   │                                                 │ │   │
│   │   └─────────────────────────────────────────────────┘ │   │
│   └───────────────────────────────────────────────────────┘   │
│                                                                 │
│   setDecorFitsSystemWindows(false)                             │
│   → Status bar OCULTA (height = 0)                            │
│   → Navigation bar OCULTA (height = 0)                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                     ESTADO 2: TECLADO VISIBLE                    │
│         (Usuario tocó input, IME aparece desde abajo)           │
├─────────────────────────────────────────────────────────────────┤
│                  ← WindowInsetsCompat.Type.ime() →              │
│                                                                 │
│   ┌───────────────────────────────────────────────────────┐   │
│   │            Window Layout (match_parent)                │   │
│   │   ┌─────────────────────────────────────────────────┐ │   │
│   │   │      FrameLayout (rootContainer)                │ │   │
│   │   │   ┌─────────────────────────────────────────┐   │ │   │
│   │   │   │    WebView (height reducida)            │   │ │   │
│   │   │   │                                         │   │ │   │
│   │   │   │   ┌─────────────────────────────────┐  │   │ │   │
│   │   │   │   │ Venice AI Chat Interface       │  │   │ │   │
│   │   │   │   │ - Texto visible                │  │   │ │   │
│   │   │   │   │ - Input VISIBLE ✅             │  │   │ │   │
│   │   │   │   │ - Botones                      │  │   │ │   │
│   │   │   │   └─────────────────────────────────┘  │   │ │   │
│   │   │   │                                         │   │ │   │
│   │   │   └─────────────────────────────────────────┘   │ │   │
│   │   │                                                 │ │   │
│   │   │ Padding bottom = imeHeight (630 px)           │ │   │
│   │   │ → Espacio reservado para teclado             │ │   │
│   │   │                                                 │ │   │
│   │   └─────────────────────────────────────────────────┘ │   │
│   │                                                       │   │
│   │ ┌───────────────────────────────────────────────────┐   │   │
│   │ │          Input Method Editor (IME)               │   │   │
│   │ │  ┌──────┬──────┬──────┬──────┬──────┐             │   │   │
│   │ │  │ Q    │ W    │ E    │ R    │ T    │             │   │   │
│   │ │  ├──────┼──────┼──────┼──────┼──────┤             │   │   │
│   │ │  │ A    │ S    │ D    │ F    │ G    │             │   │   │
│   │ │  └──────┴──────┴──────┴──────┴──────┘             │   │   │
│   │ └───────────────────────────────────────────────────┘   │   │
│   │                                                           │   │
│   └───────────────────────────────────────────────────────────┘   │
│                                                                 │
│   setDecorFitsSystemWindows(false)                             │
│   → Status bar OCULTA (height = 0)                            │
│   → Navigation bar OCULTA (height = 0)                        │
│   → Teclado aparece, WebView se redimensiona                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                     ESTADO 3: TECLADO CERRADO                    │
│              (Usuario presionó Atrás o tapó fuera)              │
├─────────────────────────────────────────────────────────────────┤
│                  ← onWindowFocusChanged(true) →                 │
│                                                                 │
│   [VUELVE A ESTADO 1: FULLSCREEN]                             │
│   - WebView vuelve a 100% de altura                            │
│   - setupFullscreenMode() reaplicada                           │
│   - Padding = 0                                                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 Ciclo de Vida y Manejo de Eventos

```
CREATE APP
    │
    ├→ onCreate()
    │   ├→ setupFullscreenMode()  [Ocultar barras]
    │   ├→ setupWindowInsetsListener()  [Escuchar IME]
    │   └→ webView.loadUrl(VENICE_URL)
    │
    └→ Escucha de Eventos
        │
        ├→ Usuario toca input
        │   └→ IME aparece (teclado)
        │       └→ setupWindowInsetsListener() recibe evento
        │           └→ getInsets(WindowInsetsCompat.Type.ime())
        │               └→ setPadding(0, 0, 0, imeHeight)
        │                   └→ WebView se redimensiona ✅
        │
        ├→ Usuario presiona Atrás o cierra teclado
        │   └→ onWindowFocusChanged(true) se ejecuta
        │       └→ setupFullscreenMode()
        │           └→ WebView vuelve a fullscreen ✅
        │
        ├→ Usuario gira dispositivo
        │   └→ onResume() se ejecuta
        │       └→ setupFullscreenMode()
        │           └→ Fullscreen se reaplicó ✅
        │
        ├→ App va a background
        │   └→ onPause()
        │
        └→ Usuario vuelve a app
            └→ onResume()
                └→ setupFullscreenMode()
                    └→ Fullscreen reaplicado ✅
```

## 🧩 Componentes Clave

### 1. WindowCompat.setDecorFitsSystemWindows()
```
┌────────────────────────────────────────┐
│ setDecorFitsSystemWindows(false)       │
├────────────────────────────────────────┤
│ ✅ App ocupa 100% de pantalla          │
│ ✅ DrawerLayout puede ir bajo barras   │
│ ✅ WindowInsets se pueden manejar      │
│ ✅ Permite fullscreen real              │
└────────────────────────────────────────┘
```

### 2. WindowInsetsControllerCompat (Android 30+)
```
┌────────────────────────────────────────┐
│ WindowInsetsControllerCompat            │
├────────────────────────────────────────┤
│ ✅ hide(Type.systemBars())              │
│ ✅ BEHAVIOR_SHOW_TRANSIENT_BARS_BY...  │
│ ✅ User swipe revela barras             │
│ ✅ Desaparecen automáticamente          │
└────────────────────────────────────────┘
```

### 3. ViewCompat.setOnApplyWindowInsetsListener()
```
┌────────────────────────────────────────┐
│ setOnApplyWindowInsetsListener()        │
├────────────────────────────────────────┤
│ 🔊 Escucha cambios de insets            │
│ 📱 Detecta aparición de IME             │
│ 📏 Obtiene altura exacta: imeHeight    │
│ 🎯 Ajusta padding dinámicamente         │
└────────────────────────────────────────┘
```

### 4. windowSoftInputMode
```
┌──────────────────────────────────────────────────┐
│ android:windowSoftInputMode                      │
│ = "adjustResize|stateHidden"                    │
├──────────────────────────────────────────────────┤
│ adjustResize:                                    │
│ ✅ Window se redimensiona cuando IME aparece    │
│ ✅ Contenido NO se mueve bajo teclado           │
│                                                  │
│ stateHidden:                                     │
│ ✅ Teclado empieza oculto                       │
│ ✅ User debe tocar para mostrar                 │
└──────────────────────────────────────────────────┘
```

## 📊 Comparación: Antes vs Después

### ANTES (Problema)
```
Usuario toca input
    ↓
Teclado aparece (630 px)
    ↓
WebView NO se redimensiona
    ↓
Input QUEDA OCULTO bajo teclado ❌
    ↓
Usuario NO PUEDE escribir ❌
```

### DESPUÉS (Solución)
```
Usuario toca input
    ↓
Teclado aparece (630 px)
    ↓
setupWindowInsetsListener() detecta evento
    ↓
getInsets(ime()) → 630 px
    ↓
setPadding(0, 0, 0, 630)
    ↓
WebView se redimensiona automáticamente
    ↓
Input VISIBLE por encima del teclado ✅
    ↓
Usuario PUEDE escribir normalmente ✅
```

## 🎬 Timeline de Ejecución

```
[00:00] onCreate()
        ├─ setupFullscreenMode()           [0 ms]
        ├─ setupWindowInsetsListener()     [0 ms]
        └─ webView.loadUrl()               [0 ms]

[00:50] onResume()
        └─ setupFullscreenMode()           [0 ms]

[01:00] Usuario toca input text field
        └─ IME aparece                     [300 ms animación]
            └─ setOnApplyWindowInsetsListener callback
                └─ getInsets(ime())        [detectado en 301 ms]
                └─ setPadding()             [ajustado en 302 ms]
                └─ WebView redimensionado  [visible en 303 ms] ✅

[01:05] Usuario escribe en input
        └─ Texto visible                   [input accesible] ✅

[01:10] Usuario presiona Atrás
        └─ IME desaparece                  [300 ms animación]
            └─ setPadding(0, 0, 0, 0)      [ajustado en 301 ms]
            └─ WebView vuelve a fullscreen [302 ms]

[01:15] onWindowFocusChanged(true)
        └─ setupFullscreenMode()           [reaplicado]

[01:20] App en fullscreen normal           ✅
```

---

**Diagrama actualizado:** Diciembre 2025  
**Versión:** 1.0
