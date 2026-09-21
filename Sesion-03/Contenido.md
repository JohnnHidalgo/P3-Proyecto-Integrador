# Contenido de la Sesion 03 - Programacion orientada a objetos en Kotlin

**Tema del curso:** Tema 2 - Fundamentos de Kotlin para Android

## 1. Objetivo de la sesion

Aplicar los principios de la programacion orientada a objetos (clases, objetos, encapsulamiento,
colecciones y null safety) en Kotlin, modelando las primeras entidades del proyecto integrador de
gestion de tareas.

## 2. Guion / desarrollo de la clase

Bloque de aproximadamente 120 minutos.

### Bloque 1 (0:00 - 0:10) Apertura
- Revision breve de los ejercicios de Kotlin de la tarea anterior y de los commits subidos a GitHub.
- Presentacion del objetivo de la sesion: pasar de funciones sueltas a un modelo de objetos.

### Bloque 2 (0:10 - 0:35) Clases y objetos en Kotlin
- Definicion de una clase con `class`, propiedades y el constructor primario.
- Diferencia entre una clase (el molde) y un objeto/instancia (el elemento concreto creado a partir de
  ese molde).
- Metodos dentro de una clase.

### Bloque 3 (0:35 - 0:55) Encapsulamiento
- Modificadores de visibilidad: `public`, `private`, `protected`, `internal`.
- Uso de propiedades con `val`/`var` dentro de una clase y por que preferir la inmutabilidad cuando sea
  posible.
- Aplicacion practica: proteger el estado interno de una tarea (por ejemplo, que su identificador no se
  pueda modificar desde fuera de la clase).

### Bloque 4 (0:55 - 1:20) Colecciones en Kotlin
- Listas (`List`, `MutableList`), su creacion con `listOf` y `mutableListOf`.
- Operaciones comunes: `add`, `remove`, `filter`, `map`, `forEach`.
- Aplicacion al proyecto integrador: una `MutableList<Tarea>` que representa la lista de tareas del
  usuario.

### Bloque 5 (1:20 - 1:45) Null safety
- El problema de los valores nulos y como Kotlin lo previene en tiempo de compilacion.
- Tipos anulables (`String?`) frente a tipos no anulables (`String`).
- Operadores `?.`, `?:` (Elvis) y `!!`, y cuando conviene (o no) usar cada uno.

### Bloque 6 (1:45 - 2:00) Aplicacion al proyecto integrador y cierre
- Disenar en conjunto (docente-estudiantes) la clase `Tarea` que se usara desde ahora en el curso.
- Indicaciones para la tarea: modelar las entidades principales del proyecto.

## 3. Explicacion teorica breve

La **programacion orientada a objetos (POO)** organiza el codigo alrededor de "objetos" que combinan
datos (propiedades) y comportamiento (metodos). Una **clase** es la plantilla que describe que
propiedades y metodos tendran sus objetos; un **objeto** es una instancia concreta de esa clase, creada
con el operador de construccion (por ejemplo, `Tarea(...)`).

El **encapsulamiento** consiste en restringir el acceso directo a los datos internos de un objeto,
exponiendo solo lo necesario a traves de metodos o propiedades controladas. En Kotlin esto se logra con
modificadores de visibilidad como `private`, que impide el acceso desde fuera de la clase.

Las **colecciones** permiten agrupar varios elementos del mismo tipo. Una `List` es de solo lectura una
vez creada, mientras que una `MutableList` permite agregar y eliminar elementos. Muchas operaciones sobre
colecciones (`filter`, `map`, `forEach`) se expresan de forma funcional, sin necesidad de bucles
explicitos, aunque estos tambien estan disponibles con `for`.

**Null safety** es una caracteristica central de Kotlin: por defecto, ningun tipo puede contener `null`
(por ejemplo, `String` nunca es nulo). Para permitir valores nulos, el tipo debe declararse explicitamente
como anulable agregando un signo de interrogacion (`String?`). Esto evita en tiempo de compilacion una
gran cantidad de errores conocidos como `NullPointerException`, muy comunes en otros lenguajes.

## 4. Ejemplo de codigo en Kotlin

Modelado inicial de las entidades del proyecto integrador Task Manager usando clases, encapsulamiento,
colecciones y null safety:

```kotlin
enum class Prioridad {
    BAJA, MEDIA, ALTA
}

class Tarea(
    val id: Int,
    val titulo: String,
    val prioridad: Prioridad = Prioridad.MEDIA,
    descripcion: String? = null
) {
    // Propiedad privada: no puede modificarse directamente desde fuera de la clase
    private var completada: Boolean = false

    // Propiedad anulable: una tarea puede no tener descripcion
    val descripcion: String? = descripcion

    fun marcarComoCompletada() {
        completada = true
    }

    fun estaCompletada(): Boolean = completada

    fun resumen(): String {
        // Uso del operador Elvis (?:) para dar un valor por defecto cuando la descripcion es nula
        val textoDescripcion = descripcion ?: "Sin descripcion"
        val estado = if (completada) "Completada" else "Pendiente"
        return "[$id] $titulo ($prioridad) - $estado - $textoDescripcion"
    }
}

class GestorDeTareas {
    private val tareas: MutableList<Tarea> = mutableListOf()
    private var siguienteId: Int = 1

    fun agregarTarea(titulo: String, prioridad: Prioridad = Prioridad.MEDIA, descripcion: String? = null) {
        val nuevaTarea = Tarea(siguienteId, titulo, prioridad, descripcion)
        tareas.add(nuevaTarea)
        siguienteId++
    }

    fun tareasPendientes(): List<Tarea> = tareas.filter { !it.estaCompletada() }

    fun completarTarea(id: Int) {
        val tarea = tareas.find { it.id == id }
        tarea?.marcarComoCompletada()
    }

    fun mostrarTodas() {
        tareas.forEach { tarea -> println(tarea.resumen()) }
    }
}

fun main() {
    val gestor = GestorDeTareas()
    gestor.agregarTarea("Estudiar POO en Kotlin", Prioridad.ALTA)
    gestor.agregarTarea("Configurar repositorio", Prioridad.MEDIA, "Crear README inicial")
    gestor.agregarTarea("Repasar null safety")

    gestor.completarTarea(1)
    gestor.mostrarTodas()

    println("\nTareas pendientes:")
    gestor.tareasPendientes().forEach { tarea -> println(tarea.resumen()) }
}
```

## 5. Ejercicio practico para los estudiantes

1. A partir del ejemplo visto en clase, modelar en un archivo Kotlin las entidades principales del
   proyecto integrador de gestion de tareas. Como minimo se debe incluir:
   - Una clase `Tarea` con encapsulamiento (al menos una propiedad privada).
   - Un `enum class` para representar la prioridad o categoria de la tarea.
   - Una clase `GestorDeTareas` (o similar) que mantenga una `MutableList<Tarea>` y ofrezca metodos para
     agregar, listar y completar tareas.
   - Al menos una propiedad anulable (`String?` o similar) manejada correctamente con `?:` o `?.`.
2. Probar el modelo desde una funcion `main()`, agregando al menos cuatro tareas de ejemplo, marcando
   alguna como completada y mostrando la lista de tareas pendientes.
3. Subir el avance al repositorio de GitHub del curso mediante un commit descriptivo.

## 6. Material de apoyo / enlaces

- Kotlin: clases y objetos: https://kotlinlang.org/docs/classes.html
- Kotlin: visibilidad y modificadores: https://kotlinlang.org/docs/visibility-modifiers.html
- Kotlin: colecciones: https://kotlinlang.org/docs/collections-overview.html
- Kotlin: seguridad ante valores nulos (null safety): https://kotlinlang.org/docs/null-safety.html
- Documentacion general de Kotlin: https://kotlinlang.org/docs/home.html
