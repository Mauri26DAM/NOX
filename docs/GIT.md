# Guía de Git y GitHub para este proyecto

Esta guía documenta cómo empezar a llevar el prototipo NOX/NOCTIS con Git. Está
pensada para trabajar paso a paso y poder explicar el historial a la profesora.

## 1. Qué debes subir

Sube el código fuente, los recursos, los archivos Gradle y esta documentación.
No subas:

- `local.properties`: suele contener una ruta local del SDK que solo sirve en tu
  ordenador.
- `build/`, `.gradle/`, APK generados, cachés ni archivos temporales del IDE.
- Contraseñas, tokens, claves privadas ni datos personales.

El `.gitignore` del proyecto excluye los archivos locales y las carpetas de
compilación. Antes de cada commit comprueba lo que vas a incluir:

```bash
git status
git diff
git diff --cached
```

Si aparece algo que no quieres subir, no lo añadas. Si un archivo ya estaba
seguido por Git antes de incorporarlo al `.gitignore`, ignorarlo no lo quita
automáticamente del historial.

## 2. Crear el repositorio local

Abre una terminal en la carpeta raíz del proyecto (la que contiene
`settings.gradle.kts`) y comprueba la ubicación:

```bash
pwd
ls
```

En Windows puedes usar `cd` y `dir` en la terminal correspondiente. Confirma que
ves los archivos Gradle y el directorio `app/`. En este momento la carpeta del
proyecto no tiene un repositorio Git inicializado.

Inicializa Git y revisa el estado antes de añadir archivos:

```bash
git init
git branch -M main
git status --short
```

Después de revisar que `local.properties`, `app/build/`, `build/` y `.gradle/`
no aparezcan como archivos para subir:

```bash
git add .gitignore README.md docs/GIT.md
git add app build.gradle.kts settings.gradle.kts gradle gradle.properties gradlew gradlew.bat
git status
```

Si `git status` muestra archivos privados o generados, detente y aclara por qué
aparecen antes de hacer commit. No uses `git add .` a ciegas.

## 3. Primer commit

El primer commit debe describir honestamente lo que existe ahora. No lo llames
“NOCTIS final” ni marques como completados requisitos que aún faltan.

```bash
git commit -m "docs: documentar el prototipo inicial de NOX"
```

Ese mensaje solo sería adecuado si todo lo preparado en el primer commit es
documentación. Para el primer commit que incluya también el código, usa, por
ejemplo:

```bash
git commit -m "feat: añadir prototipo inicial de la app NOX"
```

Después comprueba el historial:

```bash
git log --oneline
git status
```

## 4. Crear el repositorio en GitHub y subirlo

1. En GitHub, crea un repositorio nuevo. Puedes llamarlo `NOX` o `NOCTIS`.
2. Si ya tienes los archivos localmente, crea el repositorio remoto vacío: no
   selecciones la opción de añadir README, `.gitignore` o licencia, para evitar
   conflictos con los archivos locales.
3. Copia la URL del repositorio. Usa HTTPS o SSH según lo que tengas configurado.
4. Desde la raíz del proyecto, conecta el remoto y publica `main`:

```bash
git remote add origin URL_DEL_REPOSITORIO
git remote -v
git push -u origin main
```

Sustituye `URL_DEL_REPOSITORIO` por la URL copiada de GitHub, por ejemplo
`https://github.com/tu-usuario/NOX.git`. No compartas tokens ni los pegues en
archivos del proyecto.

Si Git responde que `origin` ya existe, no repitas el comando a ciegas:
comprueba primero la URL con `git remote -v`.

## 5. Forma de trabajo para cada historia o cambio

Para cada trabajo pequeño:

1. Asegúrate de estar en `main` y de tener los últimos cambios:

   ```bash
   git status
   git pull
   ```

2. Haz un cambio acotado y ejecútalo en Android Studio o compílalo.
3. Revisa qué cambió y que no haya archivos generados:

   ```bash
   git status
   git diff
   ```

4. Añade solo los archivos relacionados. Por ejemplo:

   ```bash
   git add app/src/main/java/com/example/nox/SectionActivity.java
   git diff --cached
   ```

5. Crea un commit que explique el cambio y publícalo:

   ```bash
   git commit -m "feat: guardar las observaciones localmente"
   git push
   ```

Usa un commit por cambio coherente, no un commit por cada línea ni un único
commit gigante al final. Para cambios que tardan varios días, puedes crear una
rama desde GitHub Desktop o desde terminal, hacer commits en ella y abrir un
Pull Request; pregunta a la profesora si espera ese flujo o trabajo directo en
`main`.

## 6. Mensajes de commit recomendados

Empieza con un tipo y escribe una descripción breve en infinitivo o como acción:

- `docs: documentar el estado inicial del proyecto`
- `feat: añadir el temporizador de adaptación ocular`
- `fix: conservar el temporizador al girar la pantalla`
- `ui: adaptar la pantalla de inicio a móviles pequeños`
- `test: comprobar el guardado del diario`
- `chore: actualizar configuración de Gradle`

No inventes commits retrospectivos para aparentar que el trabajo se hizo en
varias fases. A partir de ahora, registra cada cambio real. Si la profesora
solicita una entrega por historias, enlaza cada commit con la historia o criterio
correspondiente en la descripción del Pull Request o en la entrega.

## 7. Comandos útiles

```bash
git status --short       # resumen de archivos modificados
git diff                # cambios aún no preparados
git diff --cached       # cambios preparados para commit
git log --oneline       # historial breve
git remote -v           # URL del repositorio remoto
git pull                # descargar e integrar cambios del remoto
git push                # publicar commits
```

No uses `git reset --hard` ni borres archivos para resolver un conflicto sin
entender antes qué cambios se perderían. Ante un conflicto, conserva una copia
del trabajo y pide ayuda si no puedes identificar qué versión mantener.

## 8. Cómo presentar el repositorio a la profesora

Puedes describirlo así, adaptándolo a lo que hayas comprobado:

> He subido el estado actual como prototipo inicial. Incluye una pantalla de
> inicio, varias secciones de prueba, lectura de algunos sensores y un diario
> local. Todavía no cubre todos los requisitos del dossier. Voy a registrar los
> siguientes cambios con commits pequeños y relacionarlos con las historias y
> criterios de evaluación que acordemos.

Antes de entregar, verifica la compilación, prueba en un dispositivo o emulador,
actualiza el README con lo que realmente funciona y comprueba que GitHub contiene
los últimos commits. No afirmes que una función está probada si no la has
ejecutado.
