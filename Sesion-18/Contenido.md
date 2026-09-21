# Contenido de la Sesion 18 - Laboratorio Integrador II

## 1. Objetivo de la sesion

Brindar un espacio de asesoria practica en el que los estudiantes integren, en su proyecto de la materia, los contenidos desarrollados hasta el momento (interfaz con Jetpack Compose, navegacion, estado, consumo de datos, etc.), resolviendo dudas puntuales antes del Segundo Momento de Evaluacion.

Esta es una sesion especial de tipo laboratorio integrador: no hay exposicion teorica nueva, sino trabajo guiado sobre el proyecto de cada estudiante o grupo.

## 2. Guion / desarrollo de la clase

Dado que esta sesion es un laboratorio de asesoria y no una clase expositiva, el guion se organiza como una dinamica de trabajo en el aula con acompanamiento del docente.

### Bloque 1: Apertura y organizacion (10 min)
- Bienvenida y explicacion del proposito de la sesion: es un espacio de trabajo autonomo con asesoria docente, previo al Segundo Momento de Evaluacion (Sesion 19).
- Recordatorio rapido de los criterios de evaluacion del Segundo Momento (los mismos que se pidio revisar como material previo).
- Cada estudiante o grupo abre su proyecto y comparte brevemente (1-2 min por grupo) en que estado se encuentra y que piensa resolver hoy.

### Bloque 2: Diagnostico individual/por grupo (15 min)
- Cada grupo hace una lista corta de pendientes tecnicos: pantallas que faltan, navegacion incompleta, estados que no se actualizan, errores de compilacion, falta de manejo de listas, etc.
- El docente recorre el aula y hace una revision rapida (triage) del estado de cada proyecto, priorizando que bloqueadores criticos hay que resolver primero.

### Bloque 3: Trabajo guiado / asesoria en banco (70 min)
- Los estudiantes trabajan en su proyecto de forma autonoma, en sus equipos.
- El docente circula por el aula ofreciendo asesoria personalizada: resolucion de errores de compilacion, dudas de Compose, ayuda para estructurar pantallas o el flujo de navegacion, revision de buenas practicas de Kotlin.
- Se sugiere formar "mesas de ayuda" tematicas si varios grupos comparten el mismo problema (por ejemplo: manejo de estado, listas con LazyColumn, navegacion con NavHost).
- Los grupos que ya tienen su avance funcional pueden usar el tiempo para pulir detalles de UI o preparar la presentacion del Segundo Momento.

### Bloque 4: Puesta en comun breve (15 min)
- 3 o 4 grupos (voluntarios o elegidos al azar) muestran en 2 minutos el estado actual de su proyecto al resto de la clase.
- Se recogen preguntas frecuentes que hayan surgido durante la asesoria y se responden en conjunto, para que todos se beneficien de las dudas resueltas.

### Bloque 5: Cierre y proximos pasos (10 min)
- El docente resume los pendientes generales detectados durante la sesion.
- Se recuerda la fecha, formato y criterios del Segundo Momento de Evaluacion (Sesion 19).
- Se indica que la tarea despues de esta clase es ajustar el proyecto en funcion de la retroalimentacion recibida hoy.

## 3. Explicacion teorica breve: el rol de esta sesion en el curso

Un laboratorio integrador no introduce conceptos nuevos; su funcion pedagogica es la de consolidacion. En el diseno curricular de la materia, estas sesiones se ubican estrategicamente antes de los momentos de evaluacion para que el estudiante:

- Aplique de manera conjunta conceptos que se ensenaron por separado en sesiones anteriores (por ejemplo: componentes de Compose, estado y recomposicion, navegacion entre pantallas, listas dinamicas).
- Identifique con apoyo docente los huecos de comprension que no son evidentes cuando se estudia un tema de forma aislada.
- Practique la integracion de piezas de software, una habilidad distinta a la de aprender una API o un concepto puntual: requiere decidir como conectan entre si Composables, estado, navegacion y logica de negocio dentro de un mismo proyecto.

Desde el punto de vista de arquitectura de software, este es tambien el momento en el que conviene revisar si el proyecto ya distingue capas basicas (UI, estado/logica, datos), aunque la persistencia con Room y el patron MVVM se formalizaran en sesiones posteriores (Sesion 20 en adelante). Un buen habito a reforzar aqui es que los Composables no contengan logica de negocio compleja, y que el estado se maneje de forma centralizada (por ejemplo, en un ViewModel simple o en una clase de estado), incluso antes de introducir Room.

## 4. Guia de la actividad / evaluacion

Dado que esta sesion no tiene una exposicion de codigo nueva, este punto se reemplaza por la guia de la actividad de laboratorio.

### Que se revisa en esta sesion
- El avance funcional del proyecto integrador de cada estudiante o grupo (la app de gestion de tareas u otro proyecto equivalente elegido previamente).
- La correcta integracion de los temas vistos hasta la fecha: pantallas con Jetpack Compose, navegacion entre pantallas, manejo de estado y, si corresponde, consumo de datos.
- La disposicion de los estudiantes para identificar y resolver bloqueadores tecnicos con apoyo docente.

### Que debe presentar el estudiante durante la sesion
- Su repositorio de proyecto actualizado (por ejemplo en GitHub), con el codigo mas reciente subido antes de iniciar la clase.
- Una lista corta y concreta (fisica o digital) de los pendientes tecnicos que planea resolver durante la sesion.
- El proyecto compilando y ejecutandose (al menos en su version anterior) al inicio de la clase, para poder partir de una base funcional.

### Entregable al cierre de la sesion
- El avance de codigo actualizado en el repositorio del proyecto, reflejando las correcciones o mejoras trabajadas durante el laboratorio.
- Una nota breve (puede ser un comentario de commit o una entrada en un README del proyecto) que resuma que se corrigio o mejoro en esta sesion, a modo de bitacora.

### Criterios de evaluacion (rubrica breve, orientativa para la asesoria)

| Criterio | Nivel logrado | Nivel en proceso | Nivel inicial |
|---|---|---|---|
| Funcionalidad basica de navegacion | La app navega entre todas las pantallas previstas sin errores | Navega entre algunas pantallas, con errores menores | No hay navegacion funcional entre pantallas |
| Manejo de estado en Compose | El estado se actualiza y refleja correctamente en la UI | El estado se actualiza parcialmente o de forma inconsistente | No hay manejo de estado o la UI no reacciona a cambios |
| Organizacion del codigo | Composables, estado y logica estan razonablemente separados | Hay separacion parcial, con mezcla de responsabilidades | Todo el codigo esta mezclado en un solo archivo o Composable |
| Autonomia y resolucion de problemas | El equipo identifica y resuelve sus propios bloqueadores con poca ayuda | El equipo necesita guia frecuente para avanzar | El equipo no logra avanzar incluso con apoyo docente |

Nota: esta rubrica es orientativa para el acompanamiento del laboratorio; la evaluacion formal y calificada del proyecto ocurre en la Sesion 19 (Segundo Momento de Evaluacion), donde se detallan los criterios oficiales de calificacion.

## 5. Ejercicio practico para los estudiantes

Como cierre de la sesion y puente hacia la Sesion 19, cada estudiante o grupo debe:

1. Terminar de resolver, fuera de horario de clase, los pendientes tecnicos identificados durante el laboratorio (los que no se llegaron a resolver en el aula).
2. Revisar su proyecto contra los criterios de evaluacion del Segundo Momento, marcando explicitamente que criterios ya se cumplen y cuales todavia estan pendientes.
3. Dejar el repositorio del proyecto actualizado y en estado funcional (que compile y corra sin errores) antes de la Sesion 19.
4. Preparar una presentacion breve (3 a 5 minutos) del estado del proyecto, mostrando las pantallas principales y explicando las decisiones de diseno tomadas hasta el momento.

Este ejercicio corresponde directamente a la seccion "Despues de la clase" de esta sesion: ajustar el proyecto en funcion de la retroalimentacion recibida durante el laboratorio.

## 6. Material de apoyo / enlaces

- Guia de arquitectura de apps en Android (referencia general para organizar el codigo del proyecto): https://developer.android.com/topic/architecture
- Documentacion oficial de Jetpack Compose (para resolver dudas de UI durante la asesoria): https://developer.android.com/jetpack/compose
- Guia de manejo de estado en Compose: https://developer.android.com/jetpack/compose/state
- Guia de navegacion en Compose: https://developer.android.com/jetpack/compose/navigation
- Documentacion de Kotlin Coroutines (util si el proyecto ya maneja operaciones asincronas): https://kotlinlang.org/docs/coroutines-overview.html
