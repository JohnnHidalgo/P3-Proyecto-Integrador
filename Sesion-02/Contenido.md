# Contenido de la Sesion 02 - Introduccion a Git/GitHub y fundamentos de Kotlin

**Tema del curso:** Tema 2 - Fundamentos de Kotlin para Android

## 1. Objetivo de la sesion

Introducir el control de versiones con Git y GitHub como herramienta de trabajo del curso, y sentar las
bases del lenguaje Kotlin (variables, tipos de datos y funciones) que se usaran durante todo el
desarrollo del proyecto integrador.

## 2. Guion / desarrollo de la clase

Bloque de aproximadamente 120 minutos.

### Bloque 1 (0:00 - 0:10) Apertura
- Recordatorio de la tarea de la sesion anterior (entorno instalado, cuenta de GitHub).
- Verificacion rapida en el aula: quienes lograron crear su cuenta de GitHub.

### Bloque 2 (0:10 - 0:40) Introduccion a Git y GitHub
- Que es un sistema de control de versiones y por que se usa en el desarrollo de software.
- Conceptos clave: repositorio, commit, rama (branch), remoto.
- Flujo de trabajo basico: `clone`, `add`, `commit`, `push`, `pull`.
- Demostracion en vivo: crear un repositorio en GitHub y clonarlo en la maquina local (o vincularlo
  desde Android Studio usando su integracion con Git).

### Bloque 3 (0:40 - 0:55) Practica guiada de Git
- Cada estudiante crea su primer repositorio personal para el curso.
- Realizan un primer commit con un archivo `README.md` simple.
- Suben (push) el cambio a GitHub y verifican el resultado en el navegador.

### Bloque 4 (0:55 - 1:20) Fundamentos de Kotlin: variables y tipos de datos
- Declaracion de variables con `val` (inmutable) y `var` (mutable).
- Tipos de datos basicos: `Int`, `Double`, `Boolean`, `String`, `Char`.
- Inferencia de tipos y tipado explicito.
- Cadenas de texto con plantillas (`string templates`).

### Bloque 5 (1:20 - 1:45) Fundamentos de Kotlin: funciones
- Declaracion de funciones con `fun`, parametros y tipo de retorno.
- Funciones de una sola expresion.
- Valores por defecto y parametros con nombre.
- Aplicacion al proyecto integrador: primeras funciones utilitarias para el modulo de tareas.

### Bloque 6 (1:45 - 2:00) Practica guiada y cierre
- Ejercicios cortos de variables y funciones resueltos en clase.
- Indicaciones para la tarea: resolver ejercicios de Kotlin y subirlos mediante commits a GitHub.

## 3. Explicacion teorica breve

**Git** es un sistema de control de versiones distribuido que permite registrar el historial de cambios
de un proyecto de software, volver a versiones anteriores y colaborar con otras personas sin
sobrescribir el trabajo de nadie. **GitHub** es una plataforma en la nube que hospeda repositorios Git y
agrega funciones de colaboracion (issues, pull requests, revision de codigo).

Los comandos basicos del flujo de trabajo son:
- `git init`: inicializa un repositorio local.
- `git add`: prepara los cambios para el commit (area de staging).
- `git commit`: guarda una version del proyecto en el historial local.
- `git push`: envia los commits locales al repositorio remoto (GitHub).
- `git pull`: trae los cambios del remoto al repositorio local.

**Kotlin** es un lenguaje de programacion moderno, conciso y con seguridad de tipos, oficialmente
soportado por Google para el desarrollo de Android. Una variable declarada con `val` es de solo lectura
(no se puede reasignar despues de inicializarse), mientras que una declarada con `var` puede cambiar de
valor. Kotlin infiere el tipo automaticamente a partir del valor asignado, aunque tambien permite
declararlo explicitamente. Una **funcion** en Kotlin se define con la palabra clave `fun`, puede recibir
parametros (incluso con valores por defecto) y puede retornar un valor con la flecha `: Tipo`.

## 4. Ejemplo de codigo en Kotlin

Primeras funciones de Kotlin puro para el proyecto integrador Task Manager, sin usar todavia clases
propias (eso se vera en la Sesion 3):

```kotlin
fun main() {
    // Variables y tipos de datos
    val nombreApp: String = "Task Manager"
    var totalTareas: Int = 0
    val version: Double = 1.0
    var cursoAprobado: Boolean = false

    println("Bienvenido a $nombreApp version $version")

    // Uso de una funcion utilitaria
    val descripcion = crearDescripcionTarea("Estudiar Kotlin", prioridad = "alta")
    println(descripcion)

    totalTareas = agregarTarea(totalTareas)
    totalTareas = agregarTarea(totalTareas)
    println("Total de tareas registradas: $totalTareas")

    cursoAprobado = totalTareas >= 2
    println("Curso en buen camino: $cursoAprobado")
}

// Funcion con parametro por defecto
fun crearDescripcionTarea(titulo: String, prioridad: String = "media"): String {
    return "Tarea: '$titulo' - Prioridad: $prioridad"
}

// Funcion de una sola expresion
fun agregarTarea(totalActual: Int): Int = totalActual + 1
```

Salida esperada al ejecutar `main()`:

```
Bienvenido a Task Manager version 1.0
Tarea: 'Estudiar Kotlin' - Prioridad: alta
Total de tareas registradas: 2
Curso en buen camino: true
```

## 5. Ejercicio practico para los estudiantes

1. En el repositorio personal de GitHub creado en clase, agregar un archivo Kotlin llamado
   `Ejercicios.kt`.
2. Resolver los siguientes ejercicios usando solo variables y funciones (sin clases todavia):
   - Una funcion `esNumeroPar(numero: Int): Boolean` que indique si un numero es par.
   - Una funcion `calcularPromedio(a: Double, b: Double, c: Double): Double` que retorne el promedio de
     tres notas.
   - Una funcion `saludarEstudiante(nombre: String, curso: String = "Programacion III"): String` que
     retorne un saludo usando plantillas de texto.
3. Probar cada funcion desde `main()` imprimiendo los resultados con `println`.
4. Registrar el avance en al menos dos commits distintos (por ejemplo, uno por cada grupo de ejercicios
   resuelto) y subirlos con `git push` a GitHub.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Kotlin - sintaxis basica: https://kotlinlang.org/docs/basic-syntax.html
- Kotlin: variables y tipos: https://kotlinlang.org/docs/basic-types.html
- Kotlin: funciones: https://kotlinlang.org/docs/functions.html
- Guia de inicio con Git y GitHub: https://docs.github.com/es/get-started
- Sobre el flujo de trabajo con Git: https://docs.github.com/es/get-started/using-git/about-git
