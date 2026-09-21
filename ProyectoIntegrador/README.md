# Task Manager UPB - Proyecto Integrador

Proyecto Android (Kotlin + Jetpack Compose) usado como ejemplo en vivo durante
la materia Programacion III. El historial de commits de este proyecto avanza
sesion a sesion junto con el silabo: cada commit relevante esta etiquetado con
el numero de sesion en su mensaje, para poder revisar `git log` y ver
exactamente que codigo se agrego en cada clase.

## Como abrirlo

1. Abrir Android Studio (Hedgehog o superior) y seleccionar "Open" sobre esta
   carpeta (`ProyectoIntegrador`).
2. Si Android Studio pide generar el Gradle Wrapper, aceptar (este repositorio
   no incluye el binario `gradle-wrapper.jar`; Android Studio lo genera al
   sincronizar el proyecto).
3. Esperar la sincronizacion de Gradle y ejecutar la app en un emulador o
   dispositivo con API 24 o superior.

## Version del entorno usada

- Android Gradle Plugin 8.0.1
- Kotlin 1.8.21
- Gradle 8.1.1
- compileSdk / targetSdk 33, minSdk 24
- Jetpack Compose BOM 2023.05.01

## Como recorrer la evolucion del proyecto

```bash
git log --oneline -- ProyectoIntegrador
```

Cada entrada corresponde a los contenidos de una sesion del curso; el detalle
teorico de cada sesion esta en `../Sesion-XX/Contenido.md`.
