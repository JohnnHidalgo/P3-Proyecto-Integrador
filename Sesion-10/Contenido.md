# Contenido de la sesion - Sesion 10: Laboratorio Integrador I

**Tema del curso:** Evaluacion / Laboratorio integrador

---

## 1. Objetivo de la sesion

Brindar un espacio de asesoria personalizada y revision del avance del proyecto integrador de cada estudiante o equipo, identificando dudas, riesgos y ajustes pendientes antes del Primer Momento de Evaluacion (Sesion 11).

---

## 2. Guion / desarrollo de la clase

Duracion total estimada: 120 minutos. Esta sesion no es una clase expositiva tradicional: es un laboratorio de asesoria y revision de proyecto, con el docente circulando entre estudiantes/equipos.

### Bloque 1 - Apertura y organizacion (10 min)
- Bienvenida y explicacion de la dinamica de la sesion: no habra exposicion de contenido nuevo, sino trabajo practico con asesoria directa.
- Se solicita a cada estudiante/equipo tener a mano: el repositorio del proyecto actualizado en `main`, el emulador o dispositivo listo, y una lista breve de dudas o bloqueos actuales.
- Se explica el criterio de turnos de asesoria (por ejemplo, orden de lista, o turnos por bloques de tiempo fijos de 10-15 minutos por equipo).

### Bloque 2 - Revision rapida grupal (15 min)
- El docente hace una revision rapida y grupal de los elementos minimos que deberia tener el proyecto a esta altura del curso:
  - Layouts y estado (Sesion 7).
  - Navegacion entre al menos dos pantallas con paso de parametros (Sesion 8).
  - Historial de commits y al menos un Pull Request fusionado en el repositorio (Sesion 9).
- Se aclara que estos elementos seran parte de lo que se observara en el Primer Momento de Evaluacion (Sesion 11).

### Bloque 3 - Asesoria personalizada por equipos/estudiantes (70 min)
- El docente recorre los puestos de trabajo (o abre salas/turnos virtuales) atendiendo a cada estudiante o equipo de forma individual.
- En cada turno se revisa:
  - El estado actual del repositorio (rama `main`, ramas abiertas, Pull Requests pendientes).
  - El funcionamiento de la app en el emulador/dispositivo (navegacion, listas, estado).
  - Dudas puntuales de codigo (errores de compilacion, comportamiento inesperado, dudas conceptuales).
- El docente deja anotadas recomendaciones concretas y accionables para cada estudiante/equipo (que corregir, que simplificar, que agregar antes de la evaluacion).
- Mientras esperan su turno, los demas estudiantes continuan trabajando de forma autonoma en los ajustes ya identificados.

### Bloque 4 - Cierre y balance (25 min)
- Ronda breve de cierre: cada equipo/estudiante comparte en una frase el principal ajuste que hara antes de la Sesion 11.
- El docente resume los problemas mas comunes detectados durante la asesoria (sin senalar a estudiantes especificos) para que sirvan de aprendizaje general.
- Se recuerda la logistica del Primer Momento de Evaluacion: tiempo de presentacion, formato esperado, y que se debe llevar preparado.

---

## 3. Explicacion teorica breve

Esta sesion no introduce contenido tecnico nuevo; su rol dentro del curso es de consolidacion y control de calidad intermedio. Pedagogicamente, un "laboratorio integrador" cumple tres funciones:

1. **Diagnostico formativo.** Permite detectar, antes de una instancia evaluada, que conceptos no quedaron suficientemente claros (layouts, estado, navegacion, o el flujo de Git colaborativo) y corregir el rumbo a tiempo.
2. **Integracion de contenidos.** Obliga a que los conceptos vistos por separado en las Sesiones 7, 8 y 9 (interfaz, navegacion, control de versiones colaborativo) converjan en un unico proyecto funcional y coherente.
3. **Reduccion de la ansiedad evaluativa.** Al ofrecer asesoria personalizada previa a la evaluacion, se espera que los estudiantes lleguen al Primer Momento de Evaluacion con mayor seguridad sobre el estado de su proyecto y con expectativas claras sobre los criterios que se aplicaran.

El rol del docente en esta sesion es principalmente de guia y revisor, no de expositor: escucha, diagnostica y orienta, priorizando que cada estudiante o equipo identifique con claridad sus proximos pasos concretos.

---

## 4. Guia de la actividad / evaluacion

Nota: en esta sesion especial, este punto reemplaza al "Ejemplo de codigo en Kotlin", dado que se trata de un laboratorio de asesoria, no de una clase expositiva.

### Que se revisa
- Estado general del repositorio del proyecto integrador (rama `main` actualizada, historial de commits legible, al menos un Pull Request fusionado correctamente).
- Funcionamiento basico de la interfaz: layouts (`Row`, `Column`, `Box`), listas con `LazyColumn` y manejo de estado (`State`/`remember`).
- Navegacion entre pantallas con Navigation Compose, incluyendo paso de al menos un parametro.
- Coherencia general del proyecto respecto a la idea o problema que el estudiante/equipo eligio resolver (Task Manager u otro proyecto propio equivalente en alcance).

### Que debe presentar el estudiante en esta sesion
- El repositorio del proyecto, accesible y actualizado (idealmente en GitHub), con el historial de commits visible.
- El proyecto corriendo en un emulador o dispositivo fisico, listo para mostrar su funcionamiento en pocos minutos.
- Una lista breve (escrita o mental) de dudas, bloqueos o decisiones de diseno sobre las que quiere orientacion puntual.

### Criterios de evaluacion (formativa, no calificada numericamente en esta sesion)

| Criterio | Que se observa |
|---|---|
| Repositorio y control de versiones | Existencia de ramas, commits descriptivos, al menos un Pull Request fusionado |
| Interfaz y estado | Uso correcto de layouts y de `State` para reflejar cambios en la UI |
| Navegacion | Al menos dos pantallas conectadas, con paso de parametros funcionando |
| Claridad de proximos pasos | El estudiante puede explicar que le falta y como planea resolverlo antes de la Sesion 11 |

Esta revision es formativa: su proposito es identificar ajustes antes de la evaluacion real (Sesion 11), no calificar el proyecto en este momento.

---

## 5. Ejercicio practico para los estudiantes

Como preparacion para la siguiente etapa del proyecto integrador (Primer Momento de Evaluacion, Sesion 11):

1. Aplicar durante la semana los ajustes concretos identificados en la asesoria de esta sesion (correcciones de layout, estado, navegacion o control de versiones).
2. Asegurar que el repositorio quede en un estado limpio y funcional en `main`, sin Pull Requests abiertos sin resolver.
3. Preparar una presentacion breve (5-10 minutos aproximadamente) del proyecto: que problema resuelve, que pantallas tiene, y una demostracion en vivo de su funcionamiento.
4. Revisar que la app corra sin errores en un emulador o dispositivo antes del dia de la evaluacion, evitando dejar esta verificacion para el ultimo momento.

Este ejercicio corresponde a la actividad de "Despues de la clase": realizar los ajustes finales del proyecto.

---

## 6. Material de apoyo / enlaces

- Documentacion oficial de Android: https://developer.android.com/docs
- Jetpack Compose (guia general): https://developer.android.com/jetpack/compose
- Navigation Compose: https://developer.android.com/jetpack/compose/navigation
- Documentacion de GitHub sobre Pull Requests: https://docs.github.com/es/pull-requests
- Buenas practicas de arquitectura de apps Android: https://developer.android.com/topic/architecture
