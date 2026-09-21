# Contenido de la sesion - Sesion 11: Primer Momento de Evaluacion

**Tema del curso:** Evaluacion / Laboratorio integrador

---

## 1. Objetivo de la sesion

Que cada estudiante o equipo presente y defienda el primer avance de su proyecto integrador, demostrando el uso correcto de layouts, estado y navegacion en Jetpack Compose, asi como un flujo de trabajo colaborativo con Git, recibiendo retroalimentacion formal sobre su desempeno.

---

## 2. Guion / desarrollo de la clase

Duracion total estimada: 120 minutos. Esta sesion es una instancia de evaluacion mediante presentacion de proyecto, no una clase expositiva tradicional.

### Bloque 1 - Apertura y logistica (10 min)
- Bienvenida y explicacion del formato de la sesion: presentaciones individuales o por equipo, tiempo asignado y orden de participacion.
- Se recuerda la rubrica de evaluacion y los aspectos que se observaran durante cada presentacion.
- Se resuelven dudas logisticas de ultimo momento (orden, tiempo exacto por grupo, uso de proyector/pantalla compartida).

### Bloque 2 - Presentaciones (70-80 min, segun cantidad de estudiantes/equipos)
- Cada estudiante o equipo dispone de un tiempo acotado (por ejemplo 8-10 minutos: 5-6 minutos de presentacion/demo + 2-4 minutos de preguntas) para:
  - Explicar brevemente el problema o idea que resuelve su proyecto (Task Manager u otro proyecto propio).
  - Mostrar en vivo el funcionamiento de la app: navegacion entre pantallas, listas con estado, e interaccion basica.
  - Mostrar el repositorio: historial de commits, ramas utilizadas y al menos un Pull Request fusionado.
- El docente (y opcionalmente companeros, si se habilita evaluacion entre pares) realiza preguntas breves orientadas a verificar comprension real del codigo mostrado, no solo la funcionalidad visible.
- El docente registra observaciones y calificacion segun la rubrica mientras avanzan las presentaciones.

### Bloque 3 - Retroalimentacion grupal (20 min)
- Una vez terminadas las presentaciones, el docente comparte una retroalimentacion general (sin individualizar casos negativos) sobre fortalezas y aspectos a mejorar observados en el conjunto del curso.
- Se destacan buenas practicas vistas durante las presentaciones (por ejemplo, un manejo de estado particularmente limpio, o una descripcion de PR especialmente clara) para reforzarlas como modelo.

### Bloque 4 - Cierre (10-20 min)
- Se explica que la retroalimentacion detallada e individual (calificacion y comentarios especificos) sera comunicada por el canal correspondiente despues de la sesion.
- Se presenta brevemente el proximo tema del curso (arquitectura MVVM, Sesion 12) como el siguiente paso natural para mejorar la organizacion del codigo del proyecto.
- Cierre y agradecimiento por el trabajo presentado.

---

## 3. Explicacion teorica breve

El "Primer Momento de Evaluacion" cumple un rol de checkpoint sumativo dentro del curso: es la primera instancia en la que se califica formalmente el avance del proyecto integrador, luego de haber cubierto los fundamentos de interfaz (layouts y estado, Sesion 7), navegacion (Sesion 8) y trabajo colaborativo con Git (Sesion 9), y de haber recibido asesoria formativa en el Laboratorio Integrador I (Sesion 10).

Pedagogicamente, esta sesion busca:

1. **Evaluar la integracion real de conocimientos**, no conceptos aislados: se observa si el estudiante logra combinar interfaz, estado, navegacion y control de versiones en un unico producto funcional.
2. **Practicar la comunicacion tecnica.** Presentar un proyecto de software, explicar decisiones de diseno y responder preguntas tecnicas son habilidades tan importantes como escribir el codigo mismo, y se ejercitan deliberadamente en esta instancia.
3. **Generar un punto de referencia** sobre el cual construir el resto del semestre: los resultados y la retroalimentacion recibida aqui orientan los ajustes que cada estudiante debera incorporar antes de continuar con temas mas avanzados como MVVM (Sesion 12).

---

## 4. Guia de la actividad / evaluacion

Nota: en esta sesion especial, este punto reemplaza al "Ejemplo de codigo en Kotlin", dado que se trata de una instancia formal de evaluacion mediante presentacion de proyecto.

### Que se evalua
- Funcionamiento del proyecto integrador: la app debe ejecutarse sin errores criticos en un emulador o dispositivo.
- Uso correcto de los conceptos de las Sesiones 7 a 9: layouts y estado, navegacion entre pantallas con paso de parametros, y evidencia de trabajo colaborativo con Git (ramas y al menos un Pull Request fusionado).
- Capacidad del estudiante de explicar su propio codigo y responder preguntas basicas sobre las decisiones tomadas.
- Claridad y orden de la presentacion dentro del tiempo asignado.

### Que debe presentar el estudiante (entregables concretos)
- Acceso al repositorio del proyecto (por ejemplo en GitHub), con historial de commits visible y al menos un Pull Request fusionado hacia `main`.
- La aplicacion corriendo en vivo en un emulador o dispositivo fisico durante la presentacion.
- Una breve explicacion oral (guiada, no necesariamente con diapositivas) de: el problema que resuelve el proyecto, las pantallas implementadas, y como se maneja el estado y la navegacion.

### Criterios de evaluacion (rubrica breve)

| Criterio | Descripcion | Ponderacion sugerida |
|---|---|---|
| Interfaz y estado (Sesion 7) | Uso adecuado de layouts (`Row`, `Column`, `Box`, `LazyColumn`) y de `State` para reflejar cambios en la UI | 25% |
| Navegacion (Sesion 8) | Al menos dos pantallas conectadas con Navigation Compose y paso de parametros funcionando correctamente | 25% |
| Control de versiones (Sesion 9) | Evidencia de uso de ramas y al menos un Pull Request fusionado, con commits descriptivos | 20% |
| Funcionamiento general | La app corre sin errores criticos y cumple el objetivo funcional planteado | 15% |
| Comunicacion y defensa | Claridad de la explicacion y capacidad de responder preguntas sobre el propio codigo | 15% |

La ponderacion exacta puede ajustarse segun los criterios oficiales de evaluacion definidos por el docente en el silabo del curso; esta tabla es una guia de referencia para estructurar la sesion.

---

## 5. Ejercicio practico para los estudiantes

Como preparacion para la siguiente etapa del proyecto integrador, luego de recibir la retroalimentacion de esta evaluacion:

1. Registrar por escrito (por ejemplo en un archivo de notas del propio repositorio o en el portafolio personal) los comentarios y observaciones recibidos durante la presentacion.
2. Priorizar los ajustes senalados como mas importantes por el docente, distinguiendo entre correcciones urgentes y mejoras deseables.
3. Planificar como se incorporaran esos ajustes en las siguientes sesiones, especialmente considerando que la Sesion 12 introducira la arquitectura MVVM, que probablemente implique reorganizar parte del codigo actual.
4. Mantener el repositorio actualizado y en un estado funcional, listo para continuar el desarrollo sin retrabajo innecesario.

Este ejercicio corresponde a la actividad de "Despues de la clase": incorporar la retroalimentacion recibida.

---

## 6. Material de apoyo / enlaces

- Documentacion oficial de Android: https://developer.android.com/docs
- Jetpack Compose (guia general): https://developer.android.com/jetpack/compose
- Navigation Compose: https://developer.android.com/jetpack/compose/navigation
- Documentacion de GitHub sobre Pull Requests: https://docs.github.com/es/pull-requests
- Arquitectura de aplicaciones Android (referencia para los proximos pasos): https://developer.android.com/topic/architecture
