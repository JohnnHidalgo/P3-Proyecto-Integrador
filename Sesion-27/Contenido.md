# Contenido de la Sesion 27 - Generacion de APK/AAB

## 1. Objetivo de la sesion

Que el estudiante comprenda el proceso de generacion y firma de paquetes de distribucion de una aplicacion Android (APK y AAB) usando Android Studio y Gradle, y sea capaz de generar una version candidata firmada de su proyecto integrador lista para presentar y, potencialmente, publicar.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Apertura (10 min)
- Se recuerda el recorrido del Tema 8: se depuro (Sesion 24), se refactorizo (Sesion 25) y se documento (Sesion 26) el proyecto; hoy corresponde empaquetarlo para distribucion.
- Se explica que este es un paso previo directo al Laboratorio Integrador Final (Sesion 28) y a la defensa (Sesion 29): sin un paquete generado correctamente, no hay nada que presentar.

### Exposicion de contenido (30 min)
- Diferencia entre **APK** (Android Package, el formato instalable directamente en un dispositivo) y **AAB** (Android App Bundle, el formato que exige actualmente Google Play, que permite que Play Store genere APKs optimizados por dispositivo).
- Diferencia entre build de **debug** y build de **release**: el build de debug incluye simbolos de depuracion y esta firmado con una clave automatica solo valida para desarrollo; el build de release esta optimizado (puede incluir minificacion/ofuscacion con R8) y debe firmarse con una clave propia antes de poder instalarse fuera del entorno de desarrollo o publicarse.
- Firma digital de aplicaciones: por que Android exige que todo paquete de release este firmado (garantiza la identidad del autor y permite verificar actualizaciones futuras de la misma app), que es un **keystore** (archivo que contiene la clave privada de firma) y por que debe protegerse y respaldarse (perderlo impide publicar actualizaciones futuras con la misma identidad de app).
- El rol de `build.gradle.kts` en este proceso: bloque `signingConfigs` (donde se declaran las credenciales de firma) y bloque `buildTypes` (donde se asocia cada tipo de build, por ejemplo `release`, con su configuracion de firma y con opciones como minificacion).
- Pasos generales en Android Studio: `Build > Generate Signed App Bundle / APK`, crear o seleccionar un keystore, completar alias y contrasenas, elegir el tipo de build (release) y generar el archivo final.

### Demo en vivo (30 min)
- El docente genera en vivo un AAB firmado de la app "Task Manager": crea un nuevo keystore de ejemplo desde el asistente de Android Studio, configura `signingConfigs` y `buildTypes` en `build.gradle.kts` (ver seccion 4), y ejecuta `Build > Generate Signed App Bundle / APK` seleccionando AAB y el build type `release`.
- Se muestra donde queda el archivo generado (`app/release/app-release.aab`) y se explica brevemente como se instalaria un APK equivalente en un dispositivo de prueba usando `adb install` o arrastrandolo a un emulador.
- Se enfatiza la practica de no subir el keystore ni las contrasenas al repositorio (uso de variables de entorno o de un archivo `keystore.properties` excluido via `.gitignore`).

### Practica guiada (35 min)
- Cada equipo genera su propio keystore (si aun no tienen uno) y configura la firma de release en su proyecto integrador siguiendo el mismo patron de la demo.
- Generan un AAB (y opcionalmente un APK de prueba) de su proyecto y verifican que el archivo se genero correctamente revisando la carpeta de salida.
- El docente circula resolviendo errores comunes: contrasenas mal escritas, rutas de keystore incorrectas, o problemas de minificacion (reglas de ProGuard/R8) que rompen la app en release aunque funcionaba bien en debug.

### Cierre (15 min)
- Se recalca que la version generada hoy es la "version candidata" mencionada en el silabo: no es necesariamente la version final, pero debe ser una version estable que ya podria mostrarse.
- Se explica la tarea para despues de la clase (seccion 5) y se anticipa que en la Sesion 28 se hara una revision general de todo el proyecto integrador antes de la defensa final.

## 3. Explicacion teorica breve

Cuando un proyecto Android esta listo para distribuirse fuera del entorno de desarrollo, debe compilarse en un **build de release** en lugar de un build de debug. Este proceso produce un paquete en uno de dos formatos:

- **APK (Android Package)**: el formato tradicional, instalable directamente en un dispositivo (por ejemplo, transfiriendolo manualmente o via `adb install`). Sirve para pruebas directas o distribucion fuera de una tienda de aplicaciones.
- **AAB (Android App Bundle)**: el formato que Google Play requiere para publicar nuevas aplicaciones. A diferencia del APK, el AAB no contiene un unico paquete instalable, sino todos los recursos necesarios para que Google Play genere, para cada dispositivo, un APK optimizado (con solo los recursos, idiomas y configuraciones de CPU que ese dispositivo necesita), reduciendo el tamano de descarga.

Todo build de release debe estar **firmado digitalmente**. La firma se realiza con una clave privada almacenada en un archivo llamado **keystore**. Esta firma cumple dos funciones: certifica que el paquete proviene de un autor identificable, y permite que el sistema (o Google Play) verifique que una actualizacion futura de la misma app proviene del mismo autor (dos versiones de una app firmadas con distintas claves no se consideran la misma app a efectos de actualizacion). Por esta razon, perder el keystore original de una app publicada impide subir actualizaciones futuras con esa misma identidad, y es una practica critica respaldarlo en un lugar seguro y no compartirlo publicamente (por ejemplo, nunca debe subirse a un repositorio de codigo publico).

En Gradle (con Kotlin DSL, `build.gradle.kts`), la configuracion de firma se declara en un bloque `signingConfigs`, donde se especifican la ruta al archivo keystore, su contrasena, el alias de la clave y la contrasena de la clave. Luego, dentro de `buildTypes`, el tipo de build `release` se asocia con esa configuracion de firma mediante la propiedad `signingConfig`, y ademas suele activar opciones como `isMinifyEnabled` (que habilita R8 para reducir y ofuscar el codigo) y reglas de ProGuard/R8 para mantener el comportamiento correcto de librerias que dependen de reflexion.

## 4. Fragmentos de configuracion de build.gradle.kts para firmar y generar el AAB

```kotlin
// app/build.gradle.kts

android {
    namespace = "com.upb.taskmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.upb.taskmanager"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        create("release") {
            // Es preferible leer estos valores desde variables de entorno
            // o desde un archivo keystore.properties que NO se sube al
            // repositorio (agregado a .gitignore), en vez de escribirlos
            // aqui en texto plano.
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "keystore/release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }
}
```

Ejemplo de `keystore.properties` (archivo local, excluido del control de versiones):

```properties
storePassword=una-contrasena-segura
keyPassword=otra-contrasena-segura
keyAlias=taskmanager-release
storeFile=../keystore/release.jks
```

Y la linea correspondiente que se agrega a `.gitignore` del proyecto (no confundir con el `.gitignore` raiz del portafolio del curso, que no debe modificarse):

```
keystore.properties
*.jks
```

Con esta configuracion, generar el paquete final se hace desde Android Studio con `Build > Generate Signed App Bundle / APK`, eligiendo "Android App Bundle", el build type `release`, y el keystore configurado; alternativamente, desde la linea de comandos, con la tarea de Gradle `./gradlew bundleRelease` (para AAB) o `./gradlew assembleRelease` (para APK).

## 5. Ejercicio practico para los estudiantes

Sobre el proyecto integrador:

1. Crear un keystore propio para el proyecto (si el equipo aun no tiene uno) usando el asistente de Android Studio (`Build > Generate Signed App Bundle / APK > Create new...`).
2. Configurar `signingConfigs` y `buildTypes` en el `build.gradle.kts` del modulo `app`, evitando escribir contrasenas directamente en el archivo (usar variables de entorno o un `keystore.properties` excluido del repositorio con `.gitignore`).
3. Generar un AAB firmado en modo `release` del proyecto integrador.
4. Generar tambien un APK de prueba (puede ser en modo debug o release) e instalarlo en un emulador o dispositivo fisico para confirmar que la app arranca y funciona correctamente fuera del entorno de "Run" de Android Studio.
5. Verificar que el keystore y las contrasenas NO queden versionados en el repositorio git del proyecto.
6. Guardar el AAB/APK generado (la "version candidata") en un lugar accesible para la Sesion 28, junto con una nota de que version (`versionName`/`versionCode`) representa.

Esta actividad corresponde a la seccion "Despues de la clase" del silabo: publicar la version candidata del proyecto.

## 6. Material de apoyo / enlaces

- Compilar tu app (build variants, build types): https://developer.android.com/studio/build
- Firma de aplicaciones Android (app signing): https://developer.android.com/studio/publish/app-signing
- Generar un app bundle o APK firmado desde Android Studio: https://developer.android.com/studio/publish/app-signing#generate-key
- Android App Bundle, informacion general: https://developer.android.com/guide/app-bundle
- Reducir y ofuscar codigo con R8: https://developer.android.com/build/shrink-code
