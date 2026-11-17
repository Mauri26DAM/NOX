# Informe de entorno y pruebas · NOCTIS 0.1

## Entorno comprobado

| Elemento | Versión o configuración |
|---|---|
| Sistema de desarrollo | Linux x86_64 |
| JVM usada por Gradle | Oracle JDK 17.0.6 |
| Gradle Wrapper | 9.6.0 |
| Kotlin | 2.3.21 |
| Android SDK compilación | API 37 |
| Emulador | Medium Phone API 37.0 |
| Dispositivo virtual | `sdk_gphone64_x86_64` |
| Android del emulador | Android 17, API 37 |
| Resolución / densidad del emulador | 1080 × 2400 px / 420 dpi |
| `minSdk` / `targetSdk` | 23 / 37 |
| Android Studio | Pendiente de anotar desde **Help > About** |
| Dispositivo físico | Pendiente de probar |

## Pruebas ejecutadas

El 17 de noviembre de 2026 se ejecutó correctamente:

```bash
./gradlew assembleDebug testDebugUnitTest connectedDebugAndroidTest
```

La suite instrumentada se ejecutó en `Medium_Phone_API_37.0(AVD) - 17` e
incluye comprobaciones de:

- Visibilidad de las tres acciones de Home.
- Apertura del temporizador y retorno a Home al cancelar.
- Conservación del tiempo restante y reanudación del temporizador al recrear
  AdaptationActivity (caso de recreación por cambio de configuración, como la
  rotación).

La pantalla «Mi noche» también se abrió en el emulador tras corregir su fondo; se
comprobó visualmente que ya no aparece el bloque blanco al final.

## Evidencias pendientes para cerrar el hito

- Anotar la versión exacta de Android Studio desde **Help > About**.
- Probar instalación, navegación y rotación en un dispositivo Android físico.
- Guardar capturas del emulador/dispositivo junto a la entrega. Las capturas
  compartidas durante la conversación muestran Home, el temporizador y «Mi
  noche»; para «Mi noche», usar una nueva captura posterior a la corrección del
  fondo.
