#!/bin/bash
# Venice AI WebApp - Script de Compilación
# Archivo: build.sh (opcional)
# Uso: chmod +x build.sh && ./build.sh

set -e

echo "╔════════════════════════════════════════════════════════════╗"
echo "║    Venice AI WebApp - Build Script                        ║"
echo "║    Versión 1.0.0                                          ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Variables
PROJECT_DIR=$(pwd)
BUILD_OUTPUTS="app/build/outputs"
APK_DEBUG="$BUILD_OUTPUTS/apk/debug/app-debug.apk"
APK_RELEASE="$BUILD_OUTPUTS/apk/release/app-release.apk"

# Colores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para imprimir con color
print_step() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

# Verificar herramientas
check_tools() {
    echo ""
    echo "Verificando herramientas necesarias..."

    if ! command -v java &> /dev/null; then
        print_error "Java no instalado"
        exit 1
    fi
    java -version 2>&1 | head -1
    print_step "Java encontrado"

    if [ ! -f "gradlew" ]; then
        print_error "gradlew no encontrado en $PROJECT_DIR"
        exit 1
    fi
    print_step "Gradle wrapper encontrado"
}

# Limpiar compilaciones previas
clean_build() {
    echo ""
    echo "Limpiando compilaciones previas..."
    ./gradlew clean
    print_step "Directorio build limpiado"
}

# Compilar APK Debug
build_debug() {
    echo ""
    echo "Compilando APK Debug..."
    echo "(Esta operación puede tomar 40-60 segundos)"
    echo ""

    ./gradlew assembleDebug

    if [ -f "$APK_DEBUG" ]; then
        SIZE=$(du -h "$APK_DEBUG" | cut -f1)
        print_step "APK Debug compilado exitosamente"
        echo "  Ubicación: $APK_DEBUG"
        echo "  Tamaño: $SIZE"
    else
        print_error "Error: APK Debug no se generó"
        exit 1
    fi
}

# Compilar APK Release
build_release() {
    echo ""
    echo "Compilando APK Release..."
    echo "(Nota: Requiere keystore configurado)"
    echo ""

    ./gradlew assembleRelease

    if [ -f "$APK_RELEASE" ]; then
        SIZE=$(du -h "$APK_RELEASE" | cut -f1)
        print_step "APK Release compilado exitosamente"
        echo "  Ubicación: $APK_RELEASE"
        echo "  Tamaño: $SIZE"
    else
        print_warning "APK Release no se generó (¿keystore configurado?)"
    fi
}

# Instalar en dispositivo
install_debug() {
    echo ""
    echo "Instalando en dispositivo..."

    if ! adb devices | grep -q "device"; then
        print_warning "No se encontró dispositivo conectado"
        echo "  Conecta un dispositivo USB con depuración habilitada"
        return
    fi

    adb install -r "$APK_DEBUG"
    print_step "Aplicación instalada"
}

# Ejecutar en dispositivo
run_app() {
    echo ""
    echo "Ejecutando aplicación..."

    adb shell am start -n ia.ankherth.veniceai/.MainActivity
    print_step "Aplicación iniciada"

    echo ""
    echo "Ver logs en tiempo real:"
    echo "  adb logcat | grep MainActivity"
}

# Ejecutar tests
run_tests() {
    echo ""
    echo "Ejecutando tests unitarios..."

    ./gradlew test
    print_step "Tests completados"
}

# Mostrar opciones
show_help() {
    echo ""
    echo "Uso: ./build.sh [opción]"
    echo ""
    echo "Opciones disponibles:"
    echo "  debug       Compilar APK Debug"
    echo "  release     Compilar APK Release"
    echo "  clean       Limpiar compilaciones previas"
    echo "  install     Compilar e instalar en dispositivo"
    echo "  run         Ejecutar aplicación en dispositivo"
    echo "  test        Ejecutar tests"
    echo "  all         Compilar debug, limpiar y test"
    echo "  help        Mostrar esta ayuda"
    echo ""
}

# Opción por defecto
OPTION="${1:-debug}"

case $OPTION in
    debug)
        check_tools
        build_debug
        ;;
    release)
        check_tools
        build_release
        ;;
    clean)
        clean_build
        ;;
    install)
        check_tools
        build_debug
        install_debug
        ;;
    run)
        run_app
        ;;
    test)
        check_tools
        run_tests
        ;;
    all)
        check_tools
        clean_build
        build_debug
        run_tests
        echo ""
        print_step "Compilación completa exitosa"
        ;;
    help)
        show_help
        ;;
    *)
        print_error "Opción no reconocida: $OPTION"
        show_help
        exit 1
        ;;
esac

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║              Compilación completada                        ║"
echo "╚════════════════════════════════════════════════════════════╝"

