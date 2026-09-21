# Contenido de la sesion - Sesion 09: Trabajo colaborativo con Git: ramas y Pull Requests

**Tema del curso:** Tema 4 - Navegacion y ciclo de vida de la aplicacion

---

## 1. Objetivo de la sesion

Que el estudiante practique un flujo de trabajo colaborativo con Git y GitHub (ramas, merge y resolucion basica de conflictos) aplicandolo sobre el proyecto "Task Manager" u otro proyecto propio, integrando avances y consolidando el primer avance del proyecto integrador.

---

## 2. Guion / desarrollo de la clase

Duracion total estimada: 120 minutos.

### Bloque 1 - Apertura y repaso (10 min)
- Repaso rapido: hasta ahora el proyecto tiene layouts, estado y navegacion (Sesiones 7 y 8).
- Pregunta disparadora: "Si dos personas del equipo trabajan al mismo tiempo sobre el mismo proyecto, que problemas podrian aparecer?" Se recogen respuestas (sobrescritura de cambios, archivos en conflicto, etc.).
- Presentacion de la agenda: ramas, commits, Pull Requests y resolucion de conflictos.

### Bloque 2 - Exposicion de contenido: flujo de ramas (25 min)
- Repaso rapido de comandos base: `git status`, `git add`, `git commit`.
- Concepto de rama (`branch`) como una linea de trabajo independiente a partir de un punto del historial.
- Flujo tipico de trabajo en equipo:
  1. Crear una rama a partir de `main` (por ejemplo `feature/nueva-tarea-form`).
  2. Trabajar y hacer commits en esa rama.
  3. Subir la rama al repositorio remoto (`git push`).
  4. Abrir un Pull Request (PR) hacia `main`.
  5. Revision del PR (comentarios, cambios solicitados).
  6. Merge del PR una vez aprobado.
- Se explica la diferencia entre `merge` y conflicto de merge, y por que ocurren (dos ramas modificaron la misma linea del mismo archivo).

### Bloque 3 - Demo en vivo: rama, PR y conflicto controlado (30 min)
- El docente muestra en vivo, usando el proyecto Task Manager:
  - Crear una rama `feature/detalle-tarea` desde `main`.
  - Hacer un cambio (por ejemplo, agregar un campo a `PantallaDetalleTarea`) y confirmarlo con un commit descriptivo.
  - Subir la rama y abrir un Pull Request en GitHub, describiendo el cambio.
  - Simular un segundo colaborador que modifico la misma zona del archivo en otra rama, generando un conflicto de merge intencional.
  - Resolver el conflicto manualmente (identificar los marcadores `<<<<<<<`, `=======`, `>>>>>>>`, decidir que codigo conservar) y completar el merge.
- Se enfatiza la importancia de mensajes de commit claros y de PRs pequenos y enfocados en un solo cambio.

### Bloque 4 - Practica guiada (35 min)
- Los estudiantes, organizados en parejas (o simulando un companero con dos ramas propias si trabajan solos), practican:
  - Crear una rama nueva desde `main`.
  - Hacer al menos un commit con un cambio real sobre el proyecto Task Manager (por ejemplo, un ajuste visual o una validacion adicional).
  - Subir la rama y abrir un Pull Request en su repositorio de GitHub.
  - Si trabajan en pareja, uno de los dos revisa el PR del otro y deja al menos un comentario antes de aprobar el merge.
- El docente circula ayudando a resolver conflictos reales que puedan surgir y reforzando buenas practicas de mensajes de commit y descripciones de PR.

### Bloque 5 - Cierre y siguientes pasos (10 min)
- Puesta en comun: se revisan 1-2 Pull Requests ya fusionados.
- Resumen de conceptos: ramas, commits, push, Pull Request, revision, merge, resolucion de conflictos.
- Se explica la tarea de "Despues de la clase" (consolidar el primer avance del proyecto) y se conecta con el Laboratorio Integrador de la Sesion 10.

---

## 3. Explicacion teorica breve

**Ramas (branches).** Una rama es un puntero movil a una secuencia de commits. Permite que distintas lineas de trabajo (por ejemplo, una nueva funcionalidad y una correccion de errores) avancen en paralelo sin interferir entre si hasta que se decide integrarlas. La rama `main` suele representar el estado estable/entregable del proyecto.

**Pull Request (PR).** Es una solicitud formal para integrar los cambios de una rama dentro de otra (normalmente hacia `main`), que en plataformas como GitHub incluye:
- Una descripcion del cambio realizado y su motivo.
- La posibilidad de que otras personas revisen el codigo (code review) y dejen comentarios.
- Un historial de commits asociado, visible antes de aprobar el merge.

El PR es tambien una herramienta pedagogica: obliga a explicar por que se hizo un cambio, lo cual fortalece la comunicacion dentro de un equipo.

**Merge y conflictos.** Al fusionar (`merge`) una rama con otra, Git intenta combinar automaticamente los cambios. Cuando dos ramas modificaron las mismas lineas de un mismo archivo de forma distinta, Git no puede decidir por si solo y genera un conflicto de merge, marcando el archivo con:

```
<<<<<<< HEAD
version actual en la rama destino
=======
version que se intenta fusionar
>>>>>>> nombre-de-la-rama
```

Resolver el conflicto implica editar manualmente el archivo, decidir que contenido final debe quedar, eliminar los marcadores y crear un nuevo commit que registre la resolucion.

**Buenas practicas.** Ramas de corta duracion, con un proposito claro (una funcionalidad o correccion especifica), commits pequenos y descriptivos, y PRs faciles de revisar, son practicas estandar en la industria y sientan la base para el trabajo colaborativo que se evaluara en los siguientes hitos del curso.

---

## 4. Ejemplo de codigo en Kotlin

Aunque esta sesion es principalmente sobre Git/GitHub y no sobre nuevas funcionalidades de Compose, es util mostrar el tipo de cambio pequeno y enfocado que suele integrarse mediante un Pull Request, sobre el proyecto Task Manager:

```kotlin
// Cambio propuesto en la rama "feature/detalle-tarea":
// se agrega un campo de descripcion al detalle de la tarea.

data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String = "",
    val completada: Boolean = false
)

@Composable
fun PantallaDetalleTarea(tarea: Tarea, onVolver: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = tarea.titulo, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        // Nuevo bloque agregado en este Pull Request
        if (tarea.descripcion.isNotBlank()) {
            Text(
                text = tarea.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Text(
                text = "Sin descripcion",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolver) {
            Text("Volver a la lista")
        }
    }
}
```

Un ejemplo de mensaje de commit y de descripcion de PR asociados a este cambio:

```
git checkout -b feature/detalle-tarea
git add .
git commit -m "Agregar campo descripcion al detalle de tarea"
git push -u origin feature/detalle-tarea
```

Titulo del PR: "Agregar descripcion al detalle de tarea"
Descripcion del PR: "Se agrega el campo `descripcion` al modelo `Tarea` y se muestra en `PantallaDetalleTarea`. Si esta vacio, se muestra un texto por defecto."

---

## 5. Ejercicio practico para los estudiantes

1. Crear al menos una rama nueva a partir de `main` en el repositorio del proyecto Task Manager (o el proyecto propio).
2. Realizar un cambio funcional real (no trivial) y confirmarlo con uno o mas commits bien descritos.
3. Subir la rama al repositorio remoto y abrir un Pull Request describiendo claramente el cambio.
4. Si es posible, solicitar revision a un companero y resolver al menos un comentario antes de fusionar.
5. Provocar y resolver deliberadamente un conflicto de merge sencillo (por ejemplo, modificando la misma linea desde dos ramas distintas) para practicar la resolucion manual.
6. Dejar el proyecto integrado y estable en `main`, listo para la revision del Laboratorio Integrador I (Sesion 10).

Este ejercicio corresponde a la actividad de "Despues de la clase": consolidar el primer avance del proyecto.

---

## 6. Material de apoyo / enlaces

- Documentacion de GitHub sobre ramas: https://docs.github.com/es/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-branches
- Documentacion de GitHub sobre Pull Requests: https://docs.github.com/es/pull-requests
- Documentacion general de GitHub: https://docs.github.com/
- Resolucion de conflictos de merge (GitHub): https://docs.github.com/es/pull-requests/collaborating-with-pull-requests/addressing-merge-conflicts/resolving-a-merge-conflict-on-github
- Documentacion general de Kotlin (referencia del proyecto): https://kotlinlang.org/docs/home.html
- Documentacion oficial de Android: https://developer.android.com/docs
