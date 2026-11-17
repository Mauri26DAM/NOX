# NOX / NOCTIS

Aplicación Android educativa para explorar el cielo nocturno. Este repositorio
está en desarrollo por hitos; el código actual es un prototipo y no representa
NOCTIS 1.0.

## Estado del proyecto

El trabajo actual cubre buena parte del hito **NOCTIS 0.1 «Primera luz»**:

- Home con acceso a «Mi noche», «Adaptación ocular» y «Acerca de».
- Temporizador de adaptación configurable, con iniciar, pausar y reanudar.
- El temporizador conserva su estado al recrearse la actividad y devuelve a
  Home si se completa o se cancela.
- Registro del ciclo de vida en Logcat e Intent implícito para abrir el enlace
  del proyecto.
- «Mi noche» ofrece prototipos del sensor de luz, brújula, seis constelaciones,
  diario local de texto y Cazaestrellas.

La app usa **Kotlin y layouts XML**. La compilación y las pruebas unitarias se
pueden ejecutar desde la raíz:

El identificador Android es `0.1-dev`; las etiquetas NOCTIS (`v0.1`, `v0.2`,
etc.) se crearán al cerrar los hitos correspondientes.

```bash
./gradlew assembleDebug testDebugUnitTest
```

Las pruebas instrumentadas requieren un emulador o dispositivo Android:

```bash
./gradlew connectedDebugAndroidTest
```

No se ha verificado aquí la ejecución en un dispositivo físico ni en emulador.
Para cerrar NOCTIS 0.1 aún hay que aportar esa evidencia y completar las
comprobaciones de entorno e historial previstas en el dossier.

## Próximos hitos

Quedan por desarrollar los hitos 0.2 a 1.0 descritos en el dossier: interfaz
completa, accesibilidad, cinco idiomas y tema nocturno rojo; diario multimedia y
persistencia SQLite; API y servidor, cuenta, comunidad, mapa y ranking;
catálogo multimedia, sensores avanzados, pruebas finales y documentación de
entrega. La lectura del sensor de luz es orientativa y no equivale a una medida
científica del brillo del cielo.

## Requisitos para ejecutar

- Android Studio con el SDK configurado.
- Emulador o dispositivo Android. Los sensores pueden no estar disponibles o
  comportarse de forma distinta en un emulador.
- Abrir el proyecto desde Android Studio y esperar a que Gradle termine la
  sincronización.

## Estructura principal

```text
app/
  src/main/java/com/example/nox/   Actividades Kotlin
  src/main/res/layout/             Layouts XML
  src/main/res/values/              Recursos de texto y tema
```

## Git y colaboración

Consulta [docs/GIT.md](docs/GIT.md) para el flujo Git de este repositorio.
