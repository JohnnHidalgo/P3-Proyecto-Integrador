# Contenido de la Sesion 26 - Documentacion tecnica del proyecto

## 1. Objetivo de la sesion

Que el estudiante elabore documentacion tecnica clara y completa de su proyecto integrador (README del repositorio y manual de usuario), de modo que cualquier persona pueda entender, instalar, usar y mantener la aplicacion sin depender de explicaciones verbales del equipo que la construyo.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Apertura (10 min)
- Se recuerda que el proyecto ya fue depurado (Sesion 24) y refactorizado (Sesion 25); ahora corresponde documentarlo antes de generar el paquete final (Sesion 27).
- Pregunta disparadora: "si tuvieran que entregarle su proyecto a otro desarrollador sin hablar con el, que necesitaria leer para entenderlo y poder ejecutarlo?".

### Exposicion de contenido (30 min)
- Por que documentar: la documentacion tecnica no es un tramite, es lo que permite que el conocimiento sobrevida al equipo original (mantenimiento, incorporacion de nuevos desarrolladores, evaluacion externa del proyecto).
- Tipos de documentacion que se trabajan en esta sesion:
  - **README.md del repositorio**: primera puerta de entrada tecnica al proyecto. Debe responder: que es el proyecto, que problema resuelve, como se instala/ejecuta, que tecnologias usa, como esta organizado, y como contribuir o reportar problemas.
  - **Manual de usuario**: dirigido a la persona que usara la app final, no a quien programa. Explica, con lenguaje simple y capturas de pantalla, como instalar la app y como realizar cada funcion principal.
  - **Comentarios de codigo / KDoc**: documentacion a nivel de funcion o clase, util para quien mantiene el codigo (no reemplaza al README, lo complementa).
- Estructura recomendada de un buen README (se detalla y se muestra una plantilla en la seccion 4): titulo y descripcion breve, capturas de pantalla, tecnologias usadas, requisitos previos, instrucciones de instalacion/ejecucion, estructura de carpetas, funcionalidades principales, autor(es) y licencia (si aplica).
- Convencion KDoc en Kotlin: bloques `/** ... */` con etiquetas como `@param`, `@return`, `@throws`, que Android Studio interpreta para mostrar ayuda contextual y que herramientas como Dokka pueden usar para generar documentacion HTML navegable.

### Demo en vivo (30 min)
- El docente redacta en vivo, sobre el repositorio de la app "Task Manager", un README completo siguiendo la plantilla de la seccion 4: titulo, descripcion, capturas (se explica donde se colocarian), tecnologias, instalacion, estructura de carpetas y funcionalidades.
- Se agregan comentarios KDoc a una clase clave del proyecto (por ejemplo el `ViewModel` de tareas), mostrando como Android Studio muestra esa documentacion al pasar el mouse sobre la funcion (Quick Documentation).
- Se muestra un fragmento breve de manual de usuario (una funcionalidad, con pasos numerados) para diferenciarlo claramente del README tecnico.

### Practica guiada (35 min)
- Cada equipo trabaja sobre el README.md de su propio proyecto integrador, completando o mejorando cada seccion de la plantilla.
- En paralelo, cada equipo redacta el borrador de al menos una seccion del manual de usuario (la funcionalidad principal de su app).
- El docente circula revisando que el README sea comprensible para alguien externo al equipo (prueba de "lectura ingenua": si un companero de otro equipo lo lee, entiende como ejecutar el proyecto sin preguntar nada).

### Cierre (15 min)
- Intercambio entre equipos: cada equipo revisa brevemente el README de otro equipo y da una sugerencia de mejora (feedback de pares).
- Se explica la tarea para despues de la clase (seccion 5): completar toda la documentacion del proyecto.

## 3. Explicacion teorica breve

La documentacion tecnica de un proyecto de software cumple un rol distinto segun su publico objetivo:

- El **README.md** es documentacion orientada a desarrolladores (incluyendo al propio equipo en el futuro, a evaluadores del curso, o a quien tenga que dar mantenimiento). Su funcion es reducir el tiempo que le toma a alguien nuevo entender de que trata el proyecto y ponerlo en funcionamiento. Un buen README es conciso, esta actualizado (refleja el estado real del codigo, no una version antigua) y prioriza la informacion practica (como instalar y ejecutar) por sobre la descripcion extensa de decisiones internas.
- El **manual de usuario** esta orientado a la persona final que usara la aplicacion, que no necesariamente sabe programar. Usa lenguaje simple, evita jerga tecnica, y se apoya en capturas de pantalla o descripciones visuales paso a paso de cada funcionalidad ("para crear una tarea, toque el boton + en la esquina inferior derecha, escriba el titulo y presione Guardar").
- Los **comentarios KDoc** documentan el codigo a nivel de funcion, clase o archivo. En Kotlin se escriben con bloques `/** ... */` justo antes del elemento que documentan, y admiten etiquetas estandar como `@param` (describe un parametro), `@return` (describe el valor de retorno) y `@throws` (describe una excepcion que la funcion puede lanzar). Android Studio usa estos comentarios para mostrar ayuda contextual (Quick Documentation, `Ctrl+Q` o `F1` segun el sistema operativo), y herramientas como Dokka pueden generarlos como sitio HTML navegable, de forma analoga a Javadoc en Java.

Una buena practica es mantener sincronizados estos tres niveles de documentacion con el codigo real: un README desactualizado (que describe funcionalidades que ya no existen, o que omite pasos de instalacion necesarios) es peor que no tener README, porque genera confusion y desconfianza en quien lo lee.

## 4. Ejemplo de plantilla de README y de comentarios KDoc

### Plantilla de README.md

```markdown
# Task Manager

Aplicacion Android de gestion de tareas personales, desarrollada en Kotlin
con Jetpack Compose como parte del curso Programacion III (UPB).

## Capturas de pantalla

(Agregar aqui 2 o 3 imagenes: pantalla principal, detalle de tarea, creacion de tarea)

## Tecnologias utilizadas

- Kotlin
- Jetpack Compose
- Arquitectura MVVM (ViewModel + State)
- Coroutines para operaciones asincronas

## Requisitos previos

- Android Studio (version recomendada: la mas reciente estable)
- JDK 17 o superior
- Un emulador o dispositivo fisico con Android 8.0 (API 26) o superior

## Instalacion y ejecucion

1. Clonar este repositorio: `git clone <url-del-repositorio>`
2. Abrir la carpeta del proyecto en Android Studio.
3. Esperar a que Gradle sincronice las dependencias.
4. Ejecutar la app con el boton "Run" sobre un emulador o dispositivo conectado.

## Estructura de carpetas

- `app/src/main/java/.../ui/` : composables de las pantallas.
- `app/src/main/java/.../viewmodel/` : logica de presentacion (ViewModel).
- `app/src/main/java/.../data/` : modelos y repositorios de datos.

## Funcionalidades principales

- Crear, editar y eliminar tareas.
- Marcar tareas como completadas.
- Buscar tareas por titulo.

## Autores

- Nombre del/los estudiante(s), curso Programacion III, gestion 2026.

## Licencia

Proyecto academico, uso educativo.
```

### Comentarios KDoc en el codigo

```kotlin
/**
 * Administra el estado de la lista de tareas y expone las operaciones
 * disponibles para la interfaz (crear, completar, eliminar, buscar).
 *
 * @property repositorio fuente de datos desde donde se obtienen y
 *   persisten las tareas (por ejemplo, una base de datos local o una API).
 */
class TareasViewModel(
    private val repositorio: TareasRepositorio
) : ViewModel() {

    /**
     * Marca una tarea como completada.
     *
     * @param id identificador unico de la tarea a marcar.
     * @throws IllegalArgumentException si no existe ninguna tarea con ese [id].
     */
    fun marcarComoCompletada(id: Int) {
        val existe = _tareas.value.any { it.id == id }
        require(existe) { "No existe una tarea con id=$id" }
        _tareas.value = _tareas.value.map { tarea ->
            if (tarea.id == id) tarea.copy(completada = true) else tarea
        }
    }
}
```

## 5. Ejercicio practico para los estudiantes

Sobre el proyecto integrador:

1. Redactar (o completar, si ya existe) el README.md del repositorio siguiendo la plantilla vista en clase: descripcion, capturas de pantalla, tecnologias, requisitos, instalacion, estructura de carpetas, funcionalidades y autores.
2. Redactar un manual de usuario breve (puede ser un archivo `MANUAL_USUARIO.md` o una seccion separada) que explique, con lenguaje simple y pasos numerados, como usar cada funcionalidad principal de la app desde la perspectiva de un usuario final sin conocimientos tecnicos.
3. Agregar comentarios KDoc al menos a las clases y funciones publicas mas importantes del proyecto (ViewModels, repositorios, casos de uso principales).
4. Pedir a un companero de otro equipo (o a una persona fuera del curso) que lea unicamente el README y trate de explicar, sin ayuda adicional, que hace la app y como se instala; anotar que dudas le quedaron y ajustar el README en consecuencia.
5. Subir toda la documentacion al repositorio del proyecto.

Esta actividad corresponde a la seccion "Despues de la clase" del silabo: completar la documentacion del proyecto.

## 6. Material de apoyo / enlaces

- Guia de documentacion de codigo Kotlin (KDoc): https://kotlinlang.org/docs/kotlin-doc.html
- Dokka, generador de documentacion para proyectos Kotlin: https://kotlinlang.org/docs/dokka-introduction.html
- Documentacion oficial de Jetpack Compose (referencia para enlazar en el README): https://developer.android.com/jetpack/compose
- Guia de GitHub sobre como redactar un buen README: https://docs.github.com/es/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-readmes
- Guia general de Android Studio: https://developer.android.com/studio/intro
