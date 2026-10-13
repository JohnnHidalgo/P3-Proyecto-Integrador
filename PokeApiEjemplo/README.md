# Pokedex UPB - Consumo de la PokeAPI con MVVM

Ejemplo de la materia Programacion III (Kotlin + Jetpack Compose) para aprender a
**consumir una API REST** usando **Retrofit + Coroutines** y organizando el codigo con
la arquitectura **MVVM**.

API usada (publica, gratuita y sin clave): <https://pokeapi.co/docs/v2>

## Que hace la app

- **Lista** de pokemones en una cuadricula de 2 columnas, con imagen, numero y nombre.
- **Paginacion**: carga 20 pokemones y un boton "Cargar mas" trae los siguientes.
- **Detalle** al tocar una tarjeta: imagen grande, tipos, altura, peso y estadisticas base.
- Estados de **cargando**, **error** (sin internet, error del servidor) y boton **Reintentar**.

## Endpoints que se consumen

| Pantalla | Peticion | Respuesta (resumen) |
|---|---|---|
| Lista | `GET /pokemon?limit=20&offset=0` | `count`, `next`, `results: [{ name, url }]` |
| Detalle | `GET /pokemon/{nombre}` | `id`, `name`, `height`, `weight`, `types`, `stats` |

Detalles didacticos:

- El listado **no trae la imagen**. El `id` va al final de la `url` (`.../pokemon/25/`) y
  con ese `id` se arma la URL de la imagen (ver `data/Mapeadores.kt`).
- `height` viene en decimetros y `weight` en hectogramos: el mapeador los convierte a metros y kilos.

## Arquitectura MVVM

```
Vista (Compose)          ViewModel                    Modelo (datos)
PokemonListScreen  --->  PokemonListViewModel  --->   PokemonRepository --> PokeApiService (Retrofit) --> PokeAPI
PokemonDetailScreen --->  PokemonDetailViewModel --->        |
        ^                        |                           +--> Mapeadores: DTO -> modelo del dominio
        +---- uiState (StateFlow) +
```

| Capa | Archivos | Responsabilidad |
|---|---|---|
| **Vista** | `ui/list`, `ui/detail`, `ui/components`, `ui/navigation`, `ui/theme` | Dibujar el `uiState` y avisar de los eventos (clic, reintentar). No sabe de Retrofit. |
| **ViewModel** | `PokemonListViewModel`, `PokemonDetailViewModel` | Guardar el estado (`StateFlow<...UiState>`), lanzar corrutinas con `viewModelScope`, sobrevivir a la rotacion. No sabe de Compose. |
| **Modelo** | `data/PokemonRepository`, `data/remote/*`, `data/Mapeadores`, `model/Pokemon` | Hablar con la API, convertir DTOs en modelos del dominio y traducir excepciones en `Resultado`. |

Flujo de una carga (lista):

1. `PokemonListViewModel` arranca (`init`) y llama a `cargarMas()`.
2. Marca `cargando = true` -> la pantalla muestra el indicador de progreso.
3. `viewModelScope.launch` pide los datos al repositorio (`suspend`, fuera del hilo principal).
4. El repositorio devuelve `Resultado.Exito(lista)` o `Resultado.Error(mensaje)`.
5. El ViewModel actualiza el `uiState` y Compose se **recompone** solo (`collectAsState()`).

## Estructura del proyecto

```
app/src/main/java/com/upb/pokeapi/
  MainActivity.kt
  model/Pokemon.kt                  Modelo del dominio (PokemonResumen, PokemonDetalle, Estadistica)
  data/
    PokemonRepository.kt            Unico acceso a la API; devuelve Resultado<T>
    Mapeadores.kt                   DTO -> modelo del dominio
    remote/
      PokemonDtos.kt                Clases que reflejan el JSON de la API
      PokeApiService.kt             Interface Retrofit (@GET, @Query, @Path)
      RetrofitClient.kt             Instancia unica de Retrofit + log HTTP
  ui/
    list/                           PokemonListViewModel + PokemonListScreen
    detail/                         PokemonDetailViewModel + PokemonDetailScreen
    components/                     PokemonCard, TipoChip, MensajeDeError
    navigation/AppNavigation.kt     NavHost: lista -> detalle/{nombre}
    theme/Theme.kt                  Tema Material 3
  util/                             Resultado (Exito/Error) y funciones de formato
```

## Versiones (mismas que el proyecto Task Manager UPB)

| Componente | Version |
|---|---|
| Gradle | 8.9 |
| Android Gradle Plugin | 8.7.0 |
| Kotlin / Compose Compiler | 1.9.24 / 1.5.14 |
| compileSdk / targetSdk / minSdk | 34 / 34 / 26 |
| Compose BOM | 2023.05.01 |
| Navigation Compose | 2.6.0 |
| Lifecycle ViewModel Compose | 2.6.1 |
| Retrofit + converter-gson | 2.9.0 |
| OkHttp logging-interceptor | 4.11.0 |
| Coroutines Android | 1.7.1 |
| Coil Compose | 2.4.0 |

Requisitos: **JDK 17 o superior**, **Android SDK Platform 34** instalada, internet en el
primer sync de Gradle y en el emulador/dispositivo (la app usa el permiso `INTERNET`).

## Como ejecutarlo

1. Abrir esta carpeta (`PokeApiEjemplo`) con Android Studio y esperar el sync de Gradle.
2. Ejecutar (Run) en un emulador o dispositivo con API 26 (Android 8.0) o superior.
3. Para ver las peticiones HTTP: Logcat, filtro `OkHttp`.

## Ideas para practicar

1. **Buscador**: un `TextField` que llame a `obtenerPokemon(nombre)` y navegue al detalle.
2. **Scroll infinito**: llamar a `cargarMas()` automaticamente al llegar al final de la lista.
3. **Favoritos**: guardar los pokemones favoritos con Room (sesiones 20 y 21).
4. **Mas datos**: mostrar las habilidades (`abilities`) en el detalle (nuevo DTO + mapeador).
5. **Pruebas**: probar `Mapeadores.kt` y `Formato.kt` con tests unitarios (son Kotlin puro).
