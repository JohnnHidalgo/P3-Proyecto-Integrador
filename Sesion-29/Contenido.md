# Contenido de la Sesion 29 - Tercer Momento de Evaluacion: Defensa final

## 1. Objetivo de la sesion

Que cada estudiante o equipo presente, demuestre en vivo y defienda oralmente su proyecto integrador final ante el docente (y eventualmente ante sus companeros), evidenciando el dominio tecnico adquirido durante el curso de Programacion III (Kotlin, Jetpack Compose y el ciclo completo de desarrollo Android) y respondiendo preguntas sobre las decisiones tomadas en su implementacion.

Nota: esta es una sesion especial de evaluacion sumativa (Tercer Momento de Evaluacion segun el silabo). No hay exposicion teorica nueva; la sesion completa se dedica a las presentaciones y defensas de los equipos.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Apertura (10 min)
- Bienvenida y recordatorio de la logistica de la defensa: orden de presentacion de los equipos, tiempo asignado a cada uno, y criterios de evaluacion (rubrica de la seccion 4).
- Se recuerda que se evalua tanto el producto (la app funcionando, documentada y empaquetada) como la capacidad de cada estudiante de explicar y defender las decisiones tecnicas tomadas.

### Bloque de defensas (aprox. 90 min, tiempo distribuido segun cantidad de equipos)
Para cada equipo, un turno estructurado de aproximadamente 10 a 15 minutos (ajustable segun el numero total de equipos del curso):

- **Presentacion breve (3-4 min)**: que problema resuelve la app, publico objetivo, alcance final del proyecto.
- **Demo en vivo (4-5 min)**: recorrido de las funcionalidades principales directamente sobre la app instalada (idealmente desde el AAB/APK generado en la Sesion 27, no desde el modo "Run" de Android Studio, para demostrar que el paquete final funciona de forma independiente).
- **Preguntas y defensa tecnica (4-6 min)**: el docente pregunta sobre decisiones de diseno, arquitectura, algun bug relevante que hayan resuelto (conectando con la Sesion 24), algun componente que hayan refactorizado (conectando con la Sesion 25), o algun aspecto de la documentacion o del empaquetado (conectando con las Sesiones 26 y 27). Se espera que cada integrante del equipo pueda responder al menos una pregunta técnica sobre una parte del codigo que trabajo directamente.
- Retroalimentacion muy breve del docente al equipo antes de pasar al siguiente turno.

### Cierre (20 min)
- Una vez terminadas todas las defensas, el docente comparte una retroalimentacion general al curso: fortalezas comunes observadas y aspectos a mejorar de cara a futuros proyectos.
- Se explica la actividad de cierre de curso (seccion 5): reflexion individual sobre el proceso y consolidacion del portafolio.
- Cierre del curso: agradecimientos y espacio abierto para comentarios finales de los estudiantes.

## 3. Explicacion teorica breve: el rol de esta sesion en el curso

Esta sesion es el **Tercer Momento de Evaluacion** del curso: una evaluacion sumativa final que integra, en un solo acto, todo lo trabajado durante el semestre. A diferencia de una evaluacion tradicional de conocimientos puntuales, la defensa de un proyecto integrador evalua la capacidad del estudiante de:

- Sostener un desarrollo de software completo de principio a fin (desde el diseno de la interfaz con Jetpack Compose hasta la generacion de un paquete de distribucion firmado).
- Explicar y justificar decisiones tecnicas propias, no solo ejecutar codigo que funciona.
- Comunicar de forma clara, tanto a publico tecnico como no tecnico, el valor y el funcionamiento de lo que construyo (habilidad reforzada explicitamente en la Sesion 26, documentacion tecnica y manual de usuario).

Este formato de evaluacion (demo en vivo mas defensa oral) es representativo de dinamicas profesionales reales, como la presentacion de un producto a un cliente o la revision de codigo (code review) frente a un equipo, y por eso cierra el curso: sintetiza no solo conocimiento tecnico sino tambien la capacidad de comunicarlo y defenderlo.

## 4. Guia de la actividad / evaluacion

### Que se evalua

La defensa final evalua de forma integral:

1. El **producto final**: la aplicacion Android (Kotlin + Jetpack Compose) desarrollada durante el curso, en su version empaquetada (AAB/APK firmado, generado en la Sesion 27).
2. La **documentacion tecnica**: README del repositorio y manual de usuario (Sesion 26).
3. El **proceso de desarrollo**: evidencia de depuracion (Sesion 24) y de refactorizacion/reutilizacion de componentes (Sesion 25), visible en el codigo, en la bitacora de depuracion y en el historial de commits.
4. La **defensa oral**: la capacidad de cada estudiante de explicar y justificar su trabajo y de responder preguntas tecnicas sobre el.

### Que debe presentar y defender el estudiante (entregables concretos)

- Repositorio final del proyecto integrador, con historial de commits que evidencie el proceso (no solo un commit final).
- Archivo AAB (o APK) firmado, correspondiente a la version definitiva del proyecto.
- README.md completo y actualizado.
- Manual de usuario completo y actualizado.
- Demostracion en vivo de la app instalada, mostrando las funcionalidades principales.
- Participacion oral de cada integrante del equipo respondiendo al menos una pregunta tecnica.

### Rubrica de evaluacion (Tercer Momento de Evaluacion)

| Criterio | Descripcion | Ponderacion sugerida |
|---|---|---|
| Funcionalidad completa de la app | La app cumple con las funcionalidades planteadas en el alcance del proyecto, sin errores criticos durante la demo | 25% |
| Calidad tecnica del codigo | Evidencia de buenas practicas: componentes reutilizables (Compose), separacion de responsabilidades (UI / logica / datos), manejo correcto de estado | 20% |
| Documentacion tecnica | README y manual de usuario completos, claros y coherentes con el estado real del proyecto | 15% |
| Empaquetado y firma | AAB/APK generado correctamente, firmado, instalable y funcional fuera del entorno de desarrollo | 15% |
| Defensa oral y dominio tecnico | Cada integrante explica con claridad decisiones tecnicas propias y responde preguntas del docente de forma solida | 20% |
| Presentacion y comunicacion | Claridad de la exposicion, uso adecuado del tiempo asignado, calidad de la demo en vivo | 5% |

(Los porcentajes son una referencia orientativa para la defensa; el docente puede ajustarlos segun la ponderacion oficial establecida en el silabo o en el instrumento de evaluacion institucional del curso.)

### Criterios adicionales de aprobacion minima

- La app debe ejecutarse sin crashes durante la demo en los flujos principales presentados.
- Debe existir al menos un AAB o APK firmado entregado, no solo el codigo fuente.
- Todos los integrantes del equipo deben participar activamente en la defensa oral (no se acepta que un solo integrante responda por todo el equipo).

## 5. Ejercicio practico para los estudiantes (cierre de curso)

Despues de la defensa, cada estudiante debe:

1. Reflexionar por escrito (una pagina o menos) sobre el proceso completo de construccion del proyecto integrador: que fue lo mas dificil, que aprendio sobre depuracion, refactorizacion, documentacion y despliegue, y que haria distinto en un proximo proyecto.
2. Consolidar su portafolio del curso: verificar que las carpetas de sesiones (Sesion-01 a Sesion-29) reflejen evidencias reales del propio proceso de aprendizaje (codigo, capturas, documentacion, bitacoras).
3. Archivar (por ejemplo, como release o tag en el repositorio) la version final presentada en la defensa, de modo que quede identificada de forma permanente como "version entregada" del proyecto integrador.

Esta actividad corresponde a la seccion "Despues de la clase" del silabo: reflexionar sobre los resultados obtenidos y consolidar el portafolio del curso.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Jetpack Compose (repaso general del curso): https://developer.android.com/jetpack/compose
- Depuracion en Android Studio (repaso): https://developer.android.com/studio/debug
- Compilacion y generacion de paquetes (repaso): https://developer.android.com/studio/build
- Firma de aplicaciones Android (repaso): https://developer.android.com/studio/publish/app-signing
- Guia de Kotlin sobre documentacion de codigo (repaso): https://kotlinlang.org/docs/kotlin-doc.html
- Publicacion de apps en Google Play (para quienes deseen continuar el proyecto mas alla del curso): https://developer.android.com/distribute
