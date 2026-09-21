# Contenido de la Sesion 28 - Laboratorio Integrador Final

## 1. Objetivo de la sesion

Que cada equipo realice una revision general de su proyecto integrador (funcionalidad, estabilidad, documentacion y paquete de distribucion) con asesoria personalizada del docente, dejando identificadas y priorizadas las correcciones pendientes antes de la defensa final de la Sesion 29.

Nota: esta es una sesion especial de tipo laboratorio/asesoria, no una clase expositiva. El foco esta en el trabajo autonomo de cada equipo sobre su propio proyecto, con el docente actuando como guia y evaluador de avance.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Apertura (10 min)
- Encuadre de la sesion: se aclara que hoy no hay exposicion teorica nueva; es un espacio de trabajo dirigido para cerrar el proyecto integrador antes de la defensa.
- Se recuerda la rubrica del Proyecto Integrador Final (revisada como material previo a la clase) y se listan en la pizarra los aspectos que se van a revisar hoy: funcionalidad, estabilidad (sin crashes conocidos), documentacion (README y manual de usuario) y paquete generado (APK/AAB firmado).
- Cada equipo hace una autoevaluacion rapida (5 minutos) marcando en una lista de verificacion que aspectos considera completos y cuales pendientes.

### Bloque 1: Revision general y pruebas (40 min)
- Cada equipo ejecuta su app de punta a punta probando todos los flujos principales (crear, editar, eliminar, buscar, navegar entre pantallas) buscando errores no detectados previamente.
- Se revisa que el README.md y el manual de usuario (Sesion 26) reflejen el estado actual real del proyecto.
- Se confirma que el AAB/APK firmado (Sesion 27) se genera sin errores y se instala correctamente en un dispositivo o emulador limpio (sin el entorno de desarrollo).

### Bloque 2: Asesoria personalizada por equipo (50 min)
- El docente pasa por cada equipo (o los recibe en turnos breves de 8 a 10 minutos) para:
  - Revisar el estado general del proyecto contra la rubrica de evaluacion final.
  - Dar retroalimentacion puntual sobre bugs, estructura de codigo, documentacion o el paquete generado.
  - Priorizar junto al equipo que corregir antes de la Sesion 29, dado el tiempo disponible.
- Mientras esperan su turno, los equipos continuan trabajando de forma autonoma en las correcciones ya identificadas.

### Cierre (20 min)
- Cada equipo escribe (en una hoja de ruta corta, puede ser en el propio repositorio) su lista de pendientes priorizados para antes de la defensa final.
- El docente aclara formato, duracion y logistica de la defensa de la Sesion 29 (quien presenta, cuanto dura cada exposicion, que se debe traer preparado).
- Se resuelven dudas finales sobre requisitos de entrega.

## 3. Explicacion teorica breve: el rol de esta sesion en el curso

Esta sesion no introduce contenido teorico nuevo; su funcion dentro del curso es la de **laboratorio integrador**: un espacio de sintesis donde convergen todos los temas trabajados en las sesiones previas del Tema 8 (depuracion, refactorizacion, documentacion y generacion de paquetes) aplicados de forma conjunta sobre un unico proyecto real, el proyecto integrador de cada equipo.

Pedagogicamente, el laboratorio integrador cumple el papel de "ensayo general" antes de una evaluacion sumativa (la defensa de la Sesion 29). Permite que los errores, omisiones o inconsistencias se detecten y corrijan en un entorno de bajo riesgo, con acompanamiento docente, en lugar de descubrirse recien durante la evaluacion final. Ademas, fuerza a cada equipo a mirar su proyecto de forma integral (no solo el codigo, sino tambien la documentacion y el paquete de distribucion), que es exactamente la perspectiva con la que sera evaluado en la defensa.

## 4. Guia de la actividad / evaluacion

### Que se revisa en esta sesion

Esta sesion en si misma no tiene una calificacion sumativa formal, pero el docente registra observaciones de avance por equipo que se usaran como referencia en la Sesion 29. Se revisa:

- **Funcionalidad**: que las funciones principales de la app operen correctamente de inicio a fin.
- **Estabilidad**: ausencia de crashes conocidos en los flujos principales (resultado directo del trabajo de la Sesion 24).
- **Calidad de codigo**: evidencia de refactorizacion y reutilizacion de componentes (resultado directo del trabajo de la Sesion 25).
- **Documentacion**: README y manual de usuario actualizados y completos (resultado directo del trabajo de la Sesion 26).
- **Empaquetado**: AAB/APK firmado, generado sin errores e instalable fuera del entorno de desarrollo (resultado directo del trabajo de la Sesion 27).

### Que debe presentar cada equipo al finalizar la sesion

1. Una version del proyecto en su repositorio con los ultimos ajustes subidos (commit reciente).
2. El README.md y el manual de usuario actualizados al estado real del proyecto.
3. Un AAB o APK firmado, generado y verificado (instalado y probado al menos una vez fuera de Android Studio).
4. Una lista corta y priorizada de pendientes para resolver antes de la Sesion 29 (maximo 5 items, ordenados por importancia).

### Criterios de revision usados por el docente durante la asesoria

| Aspecto | Que se observa |
|---|---|
| Funcionalidad | Los flujos principales de la app (altas, bajas, modificaciones, busqueda/filtrado) funcionan sin errores visibles |
| Estabilidad | La app no presenta crashes al usar los flujos principales con datos normales y con casos limite (listas vacias, valores extremos) |
| Codigo | Se observan componentes reutilizables (Sesion 25) y ausencia de codigo duplicado evidente |
| Documentacion | El README permite instalar y ejecutar el proyecto sin preguntas adicionales; el manual de usuario es comprensible para alguien sin conocimientos tecnicos |
| Empaquetado | El AAB/APK se genera correctamente, esta firmado, y se instala y ejecuta en un dispositivo o emulador distinto al usado durante el desarrollo |
| Autonomia del equipo | El equipo identifica por si mismo sus pendientes y prioriza de forma razonable el tiempo restante antes de la defensa |

Esta revision es formativa: su proposito es que cada equipo llegue a la Sesion 29 con el menor numero posible de sorpresas.

## 5. Ejercicio practico para los estudiantes

Antes de la Sesion 29, cada equipo debe:

1. Resolver, en orden de prioridad, los pendientes identificados durante la asesoria de esta sesion.
2. Generar (o regenerar, si hubo cambios) la version definitiva del AAB/APK firmado.
3. Revisar una ultima vez que el README y el manual de usuario coincidan exactamente con el estado final del proyecto.
4. Preparar el material de la defensa: una presentacion breve (diapositivas o guion) y una demo en vivo de la app que se mostrara en la Sesion 29.
5. Asignar roles dentro del equipo para la defensa (quien presenta cada parte, quien hace la demo, quien responde preguntas tecnicas).

Esta actividad corresponde a la seccion "Despues de la clase" del silabo: preparar la version definitiva y la defensa del proyecto.

## 6. Material de apoyo / enlaces

- Depuracion en Android Studio (repaso): https://developer.android.com/studio/debug
- Documentacion oficial de Jetpack Compose (repaso): https://developer.android.com/jetpack/compose
- Guia de Kotlin sobre documentacion de codigo (repaso): https://kotlinlang.org/docs/kotlin-doc.html
- Compilacion y generacion de paquetes (repaso): https://developer.android.com/studio/build
- Firma de aplicaciones Android (repaso): https://developer.android.com/studio/publish/app-signing
