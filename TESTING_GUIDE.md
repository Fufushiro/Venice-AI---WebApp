# Testing y Troubleshooting - WebView + Teclado

## ✅ Testing Checklist

### 1. Prueba Inicial (Fullscreen)
- [ ] Instalar la app
- [ ] Abrir app → La pantalla debe estar completamente fullscreen
- [ ] Verificar: **Sin status bar arriba, sin navigation bar abajo**
- [ ] Pantalla debe ocupar 100% del espacio disponible

### 2. Prueba del Teclado (Redimensionamiento)
- [ ] Tocar cualquier input de texto en Venice
- [ ] Verificar: Teclado aparece desde abajo
- [ ] Verificar: El input **NO está tapado por el teclado**
- [ ] Verificar: El WebView se redujo automáticamente
- [ ] Escribir algo en el input → Debe ser visible

### 3. Prueba de Cierre de Teclado
- [ ] Presionar botón "Atrás" o tocar fuera del teclado
- [ ] Verificar: Teclado desaparece
- [ ] Verificar: WebView vuelve a ocupar toda la pantalla
- [ ] Verificar: Fullscreen se reaplicó correctamente

### 4. Prueba de Rotación
- [ ] Con la app en fullscreen, rotar dispositivo (landscape)
- [ ] Verificar: Fullscreen se mantiene (sin barras)
- [ ] Rotar nuevamente a portrait
- [ ] Verificar: Fullscreen se mantiene

### 5. Prueba de Cambio de Foco
- [ ] Abrir app con Venice
- [ ] Presionar botón Home (sale de la app)
- [ ] Volver a abrir la app
- [ ] Verificar: Fullscreen se reaplicó automáticamente

### 6. Prueba de Funcionalidades Existentes
- [ ] **Subida de archivos:** Tocar botón de archivo en Venice → Debe abrir selector
- [ ] **Navegación atrás:** Navegar en Venice → Presionar atrás → Debe retroceder
- [ ] **Sesión:** Hacer scroll/escribir en Venice → Cerrar app → Abrir → Debe estar en el mismo lugar
- [ ] **Cache:** Desactivar WiFi → Recargar la página → Debe usar cache

## 🔍 Troubleshooting

### Problema: Teclado tapa el input
**Causa:** `windowSoftInputMode` no está configurado correctamente
**Solución:**
1. Ir a `AndroidManifest.xml`
2. Verificar que dice: `android:windowSoftInputMode="adjustResize|stateHidden"`
3. Limpiar y reconstruir el proyecto: `./gradlew clean build`

### Problema: Status bar visible
**Causa:** `WindowCompat.setDecorFitsSystemWindows()` no se ejecutó
**Solución:**
1. Verificar que `setupFullscreenMode()` se llama en `onCreate()`
2. Verificar que está en `onResume()` también
3. Logs: Buscar "Fullscreen" en Logcat para confirmar ejecución

### Problema: Navigation bar visible
**Causa:** Versión de Android antigua (< 5.0) sin soporte
**Solución:**
1. Verificar minSdk es 26 (Android 8.0+)
2. Para versiones > 30: Usa `WindowInsetsControllerCompat`
3. Para versiones < 30: Usa `systemUiVisibility` flags

### Problema: Padding del WebView no se ajusta
**Causa:** `ViewCompat.setOnApplyWindowInsetsListener()` no recibe eventos
**Solución:**
1. Verificar que `rootContainer` se obtiene correctamente:
   ```kotlin
   rootContainer = findViewById(R.id.rootContainer)
   ```
2. Verificar que `activity_main.xml` tiene `android:id="@+id/rootContainer"`
3. Verificar que `setupWindowInsetsListener()` se llama en `onCreate()`

### Problema: IME no se detecta (logs muestran altura 0)
**Causa:** Android < 30 puede no reportar altura del IME
**Solución:**
1. Esto es comportamiento esperado en Android 26-29
2. La api de WindowInsets es más completa en Android 30+
3. En versiones antiguas, `adjustResize` maneja automáticamente el redimensionamiento

### Problema: App se cierra al abrir teclado
**Causa:** OutOfMemoryError o crash en WebView
**Solución:**
1. Verificar logs en Logcat
2. Aumentar memoria en `build.gradle.kts`:
   ```gradle
   dexOptions {
       javaMaxHeapSize "2g"
   }
   ```
3. Reducir tamaño de imágenes en Venice

### Problema: Fullscreen desaparece después de girar dispositivo
**Causa:** `onResume()` no reaplicó fullscreen
**Solución:**
1. Verificar que `setupFullscreenMode()` está en `onResume()`
2. Aumentar logs en `onResume()` y revisar Logcat
3. Verificar que `configChanges` en AndroidManifest incluye `orientation|screenSize`

## 📊 Monitoreo con Logcat

### Filtrar logs específicos:
```bash
adb logcat | grep MainActivity
```

### Buscar líneas clave:
- "Cargando URL: https://venice.ai/chat" → WebView iniciado
- "IME Height:" → Teclado detectado
- "Window focus regained" → Foco recuperado
- "Fullscreen reapplied" → Fullscreen reaplicado
- "WebView resumed" → App en foreground

### Ejemplo de logs esperados:
```
MainActivity: Cargando URL: https://venice.ai/chat
MainActivity: IME Height: 630, Bottom: 630
MainActivity: Window focus regained, fullscreen reapplied
MainActivity: WebView resumed - Session maintained, fullscreen reapplied
```

## 🚀 Verificación Final

Ejecutar estos comandos en terminal:

```bash
# Compilar
./gradlew clean build

# Ver errores de compilación
./gradlew build --warning-mode=all

# Instalar en dispositivo
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Monitorear logs
adb logcat -c && adb logcat | grep MainActivity
```

## ⚙️ Configuración Recomendada del Dispositivo

### Para Testing Óptimo:
1. **Tamaño de fuente:** Normal (Configuración > Pantalla)
2. **Gesto de navegación:** Gestual (no botones)
3. **Densidad de pantalla:** Normal (no hacerla muy pequeña)
4. **Animaciones:** Reducidas para debugging más claro

### APIs Recomendadas:
- **Mínimo:** Android 8.0 (API 26)
- **Óptimo:** Android 10+ (API 29+)
- **Mejor:** Android 12+ (API 31+) o superior

---

**Última actualización:** Diciembre 2025
**Versión:** 1.0
