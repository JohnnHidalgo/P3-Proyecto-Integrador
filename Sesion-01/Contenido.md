# Contenido de la Sesion 01 - Presentacion de la asignatura y configuracion del entorno

**Tema del curso:** Tema 1 - Conociendo el desarrollo de aplicaciones moviles

## 1. Objetivo de la sesion

Presentar la asignatura Programacion III (silabo, metodologia, evaluacion y proyecto integrador) y dejar
el entorno de desarrollo Android (Android Studio, SDK, emulador) instalado y funcionando, culminando con
la creacion del primer proyecto de Jetpack Compose ejecutado con exito.

## 2. Guion / desarrollo de la clase

La sesion esta pensada para un bloque de aproximadamente 120 minutos.

### Bloque 1 (0:00 - 0:10) Apertura y bienvenida
- Presentacion del docente y de los estudiantes.
- Breve encuadre de la asignatura dentro de la carrera y su relacion con Programacion I y II.
- Expectativas del curso y forma de trabajo (clases teorico-practicas, proyecto integrador progresivo).

### Bloque 2 (0:10 - 0:35) Revision del silabo y metodologia
- Recorrido por las unidades tematicas del silabo oficial.
- Explicacion de la metodologia: clases con teoria breve + demo en vivo + practica guiada.
- Criterios de evaluacion (examenes, practicas, proyecto integrador, portafolio de evidencias).
- Presentacion del proyecto integrador: una aplicacion de gestion de tareas (Task Manager) que se
  construira de forma incremental sesion a sesion, iniciando en Kotlin puro y evolucionando hacia una
  interfaz completa en Jetpack Compose con Material Design 3.

### Bloque 3 (0:35 - 0:45) Introduccion al desarrollo de apps moviles
- Panorama general: nativo vs. multiplataforma, por que Android y Kotlin.
- Rol de Android Studio, el SDK y el emulador dentro del flujo de desarrollo.

### Bloque 4 (0:45 - 1:15) Configuracion del entorno de desarrollo
- Instalacion y verificacion de Android Studio.
- Instalacion de componentes del SDK (Android SDK Platform, herramientas de build).
- Creacion y configuracion de un dispositivo virtual (AVD) para el emulador.
- Resolucion de problemas comunes de instalacion (memoria RAM, virtualizacion, versiones de Java/JDK).

### Bloque 5 (1:15 - 1:45) Creacion del primer proyecto con Jetpack Compose
- Demostracion en vivo: `File > New Project > Empty Activity (Compose)`.
- Recorrido por la estructura generada: `MainActivity.kt`, carpeta `ui.theme`, `build.gradle.kts`.
- Ejecucion del proyecto en el emulador y en un dispositivo fisico (si aplica).
- Primera modificacion trivial del texto mostrado para confirmar el ciclo de edicion-ejecucion.

### Bloque 6 (1:45 - 2:00) Cierre
- Recapitulacion de lo realizado.
- Indicaciones para la tarea: completar instalacion pendiente y crear cuenta de GitHub.
- Espacio de preguntas.

## 3. Explicacion teorica breve

**Desarrollo de aplicaciones moviles.** Una aplicacion movil nativa se ejecuta directamente sobre el
sistema operativo del dispositivo (Android o iOS), lo que le permite acceder a las capacidades del
hardware y ofrecer mejor rendimiento que soluciones puramente web. Android es el sistema operativo movil
mas usado en el mundo y su desarrollo oficial se realiza con Kotlin como lenguaje principal.

**Android Studio** es el entorno de desarrollo integrado (IDE) oficial para Android, basado en IntelliJ
IDEA. Incluye editor de codigo, depurador, un sistema de compilacion basado en Gradle y herramientas de
diseno de interfaces.

**El SDK (Software Development Kit)** de Android es el conjunto de librerias, herramientas de compilacion
y APIs necesarias para construir, probar y ejecutar aplicaciones para una version especifica de Android.

**El emulador** permite ejecutar y probar una aplicacion en una maquina virtual que simula un dispositivo
Android real, sin necesidad de contar con un telefono fisico conectado.

**Jetpack Compose** es el toolkit moderno de Google para construir interfaces de usuario en Android de
forma declarativa usando Kotlin, en lugar del sistema tradicional basado en archivos XML de layout. En
Compose, la interfaz se describe mediante funciones marcadas con `@Composable` que "componen" la pantalla
a partir de piezas mas pequenas.

## 4. Ejemplo de codigo en Kotlin

A continuacion, el codigo minimo que genera Android Studio al crear un proyecto Compose, ligeramente
adaptado para dar la bienvenida al proyecto integrador del curso (Task Manager):

```kotlin
package com.upb.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BienvenidaTaskManager(nombre = "Programacion III")
                }
            }
        }
    }
}

@Composable
fun BienvenidaTaskManager(nombre: String) {
    Text(text = "Hola, $nombre! Aqui construiremos nuestra app de tareas.")
}

@Preview(showBackground = true)
@Composable
fun BienvenidaPreview() {
    MaterialTheme {
        BienvenidaTaskManager(nombre = "Programacion III")
    }
}
```

Puntos a resaltar en la demo:
- `setContent { ... }` define el contenido de la pantalla usando funciones `@Composable`.
- `@Preview` permite visualizar el componente sin ejecutar la app completa en el emulador.
- El proyecto compila con Gradle; la primera compilacion puede tardar varios minutos.

## 5. Ejercicio practico para los estudiantes

1. Si la instalacion de Android Studio, el SDK o el emulador quedo pendiente o con errores durante la
   clase, completarla en casa siguiendo la guia oficial de instalacion.
2. Verificar que el proyecto Compose creado en clase compile y ejecute correctamente en el emulador.
3. Crear una cuenta personal en GitHub (si aun no se tiene una) usando un nombre de usuario profesional,
   ya que sera la cuenta que se usara durante todo el curso para el control de versiones del proyecto
   integrador.
4. Tomar una captura de pantalla del proyecto ejecutandose como evidencia para el portafolio.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Android Studio: https://developer.android.com/studio/intro
- Guia de instalacion de Android Studio: https://developer.android.com/studio/install
- Documentacion general de Android: https://developer.android.com/docs
- Introduccion a Jetpack Compose: https://developer.android.com/jetpack/compose
- Documentacion oficial de Kotlin: https://kotlinlang.org/docs/home.html
- Crear una cuenta en GitHub: https://docs.github.com/es/get-started/signing-up-for-github/signing-up-for-a-new-github-account
