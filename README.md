# NOX / NOCTIS

Aplicación Android educativa para explorar el cielo nocturno. Este repositorio
contiene un **prototipo inicial**, no la versión final descrita en el dossier
del proyecto NOCTIS.

## Estado actual

La aplicación tiene una pantalla de inicio y una pantalla de secciones. El
prototipo incluye:

- Lectura del sensor de luz para mostrar una orientación sobre la iluminación
  ambiental. No es una medición científica del brillo del cielo.
- Brújula basada en el sensor de rotación, si el dispositivo dispone de él.
- Descripciones de seis constelaciones.
- Diario de texto guardado localmente con `SharedPreferences`.
- Minijuego sencillo controlado mediante el acelerómetro.

La aplicación está desarrollada actualmente en **Java y XML**. El dossier
propone Kotlin y XML. Conviene presentar este repositorio como prototipo de
trabajo y acordar con la profesora si se continúa sobre esta base o se adapta
al lenguaje y a las fases requeridas.

## Lo que todavía no está implementado

Entre otros requisitos del dossier, faltan la adaptación ocular, el diario con
fotografía y valoración, recursos para español/inglés/alemán/árabe/japonés,
modo de visión nocturna rojo configurable, catálogo multimedia, ajustes, cuenta
de usuario, API REST, servidor, comunidad, mapa, ranking, pruebas y
documentación de entrega.

No se debe etiquetar este prototipo como NOCTIS 1.0 ni afirmar que esos módulos
están terminados. La lista sirve como punto de partida para planificar historias
de usuario y sus evidencias.

## Requisitos para ejecutar

- Android Studio con el SDK configurado.
- Emulador o dispositivo Android. Los sensores pueden no estar disponibles o
  comportarse de forma distinta en un emulador.
- Abrir el proyecto desde Android Studio y esperar a que Gradle termine la
  sincronización.

Para compilar desde una terminal en la raíz del proyecto:

```bash
./gradlew assembleDebug
```

En Windows:

```bat
gradlew.bat assembleDebug
```

## Estructura principal

```text
app/
  src/main/
    java/com/example/nox/   Código de las actividades
    res/layout/             Layouts XML
    res/values/              Recursos y tema
```

## Git y colaboración

Consulta [docs/GIT.md](docs/GIT.md) para inicializar el repositorio, subirlo a
GitHub con seguridad y trabajar con commits pequeños y descriptivos.

## Aviso de datos

El índice basado en el sensor de luz es orientativo: los sensores de los móviles
no están calibrados como medidores astronómicos del brillo del cielo.
