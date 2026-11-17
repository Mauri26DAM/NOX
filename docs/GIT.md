# Guía de Git y GitHub para este proyecto

Esta guía recoge el flujo de trabajo Git del proyecto NOX/NOCTIS. El repositorio
local y su remoto ya están configurados. Registra cambios reales por historia y
etiqueta cada hito al completarlo; no inventes commits ni versiones anteriores.

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

## 2. Comprobar el repositorio

Abre una terminal en la carpeta raíz del proyecto (la que contiene
`settings.gradle.kts`) y comprueba la ubicación:

```bash
pwd
ls
```

En Windows puedes usar `cd` y `dir`. El repositorio existente usa la rama
`main` y tiene configurado `origin`. Comprueba la rama, el remoto y los cambios:

```bash
git status --short --branch
git remote -v
git log --oneline -5
```

No vuelvas a inicializar Git ni cambies el remoto si ya están configurados.
Antes de preparar cambios, comprueba que `local.properties`, `app/build/`,
`build/` y `.gradle/` no se vayan a subir:

```bash
git status --short
git diff
```

Añade solo los archivos de la historia terminada. No uses `git add .` a ciegas.

## 3. Registrar un cambio

Por cada cambio coherente, revisa lo preparado y usa un mensaje claro que
describa el trabajo real:

```bash
git add ARCHIVOS_RELACIONADOS
git diff --cached
git commit -m "feat: añadir el temporizador de adaptación ocular"
git status --short --branch
```

No hagas commits retrospectivos para aparentar que un requisito se completó en
otra fecha. Antes de publicar, confirma que no hay archivos privados,
generados o ajenos a la historia.

## 4. Publicar cambios

```bash
git fetch origin
git status --short --branch
git push origin main
```

Si la rama local y la remota han divergido, revisa el historial y conserva ambos
trabajos antes de publicar. No compartas tokens ni los pegues en archivos.

## 5. Forma de trabajo para cada historia o cambio

Para cada trabajo pequeño:

1. Comprueba la rama y actualiza la copia local:

   ```bash
   git status --short --branch
   git pull --ff-only
   ```

2. Implementa una historia acotada y ejecuta las pruebas disponibles.
3. Revisa cambios y archivos generados:

   ```bash
   git status --short
   git diff --check
   git diff
   ```

4. Añade solo los archivos pertinentes, revisa el contenido preparado, crea el
   commit y publícalo.

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

> El repositorio contiene un prototipo en desarrollo por hitos. La versión
> actual incluye Home, el temporizador de adaptación ocular y varias secciones
> experimentales. Todavía no cubre los requisitos completos del dossier; los
> siguientes cambios se registrarán con commits reales y verificables.

Antes de entregar, verifica la compilación, prueba en un dispositivo o emulador,
actualiza el README con lo que realmente funciona y comprueba que GitHub contiene
los últimos commits. No afirmes que una función está probada si no la has
ejecutado.
