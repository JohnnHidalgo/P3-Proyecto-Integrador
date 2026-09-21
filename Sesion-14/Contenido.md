# Contenido de la Sesion 14 - HTTP, JSON y Retrofit

**Tema del curso:** Tema 6 - Comunicacion e integracion de datos

## 1. Objetivo de la sesion

Que el estudiante comprenda los fundamentos del protocolo HTTP y el formato JSON, y sea capaz de configurar un cliente HTTP con Retrofit en una aplicacion Android para preparar el consumo de servicios REST en las siguientes sesiones.

## 2. Guion / desarrollo de la clase (aprox. 2 horas)

### Bloque 1 - Apertura (10 min)
- Bienvenida y enlace con la sesion anterior: se recuerda el `TareasViewModel` construido en la Sesion 13, que hasta ahora trabaja con datos en memoria.
- Pregunta disparadora: "De donde deberian venir realmente los datos de una aplicacion de tareas en produccion?"
- Se plantea el objetivo: dotar a la aplicacion de la capacidad de hablar con un servidor.

### Bloque 2 - Exposicion de contenido: HTTP y JSON (25 min)
- Repaso del protocolo HTTP: metodos (`GET`, `POST`, `PUT`, `DELETE`), codigos de estado (200, 201, 400, 404, 500), cabeceras y cuerpo de la peticion/respuesta.
- El formato JSON como estandar de intercambio de datos: objetos, arreglos, tipos primitivos y su equivalencia con clases de Kotlin (`data class`).
- Concepto de API REST: recursos, endpoints y verbos HTTP mapeados a operaciones CRUD.
- Se presenta como referencia conceptual el servicio publico JSONPlaceholder (https://jsonplaceholder.typicode.com) como ejemplo de API REST gratuita para practicar.

### Bloque 3 - Exposicion de contenido: Retrofit (25 min)
- Que es Retrofit y por que se usa en Android en lugar de manejar `HttpURLConnection` manualmente.
- Componentes principales: interfaz de servicio (endpoints), `Retrofit.Builder`, convertidor JSON (Gson o Kotlinx Serialization) y cliente HTTP subyacente (OkHttp).
- Anotaciones basicas: `@GET`, `@POST`, `@Path`, `@Body`, `@Query`.
- Buenas practicas: centralizar la configuracion del cliente en un objeto unico (patron singleton / `object`).

### Bloque 4 - Demostracion en vivo (30 min)
- El docente agrega las dependencias de Retrofit y del convertidor JSON al archivo `build.gradle.kts` del proyecto de ejemplo.
- Se define el modelo `Tarea` con anotaciones de serializacion.
- Se crea la interfaz `TareaApiService` con un metodo `GET` para obtener la lista de tareas.
- Se construye el objeto `RetrofitClient` que expone una instancia unica del cliente HTTP configurado con la URL base.
- Se enfatiza que en esta sesion solo se configura el cliente; la llamada real y asincrona se hara en la Sesion 15 con Coroutines.

### Bloque 5 - Practica guiada (20 min)
- Los estudiantes agregan Retrofit y el convertidor JSON a su propio proyecto integrador.
- Definen su propio `ApiService` para el recurso principal de su aplicacion (tareas, productos, u otro segun el proyecto).
- Configuran el objeto cliente Retrofit con la URL base correspondiente (real o ficticia, por ejemplo `https://api.ejemplo.com/`).

### Bloque 6 - Cierre (10 min)
- Se resumen los conceptos clave: HTTP, JSON, Retrofit y la configuracion del cliente.
- Se explica la actividad para despues de la clase.
- Se adelanta que en la Sesion 15 se realizara la llamada real usando Coroutines dentro del `ViewModel`.

## 3. Explicacion teorica breve

### 3.1 HTTP como base de la comunicacion cliente-servidor
HTTP (HyperText Transfer Protocol) es el protocolo que permite que una aplicacion Android, actuando como cliente, solicite datos a un servidor remoto. Cada peticion HTTP tiene un metodo (verbo), una URL, cabeceras opcionales y, en algunos casos, un cuerpo. Cada respuesta tiene un codigo de estado que indica si la operacion fue exitosa (2xx), si hubo un error del cliente (4xx) o del servidor (5xx). Este conocimiento sera indispensable en la Sesion 16 al momento de manejar errores de red.

### 3.2 JSON como formato de intercambio
JSON (JavaScript Object Notation) es un formato de texto ligero para representar datos estructurados. En Kotlin, una respuesta JSON tipicamente se mapea a una `data class` cuyos nombres de propiedad coinciden (o se anotan para coincidir) con las claves del JSON. Los convertidores de Retrofit (como Gson o Kotlinx Serialization) se encargan de transformar automaticamente el JSON recibido en instancias de estas clases.

### 3.3 Retrofit como cliente HTTP
Retrofit es una biblioteca de tipo "type-safe HTTP client" para Android y Java/Kotlin. En lugar de construir manualmente las peticiones HTTP, el desarrollador define una interfaz que describe los endpoints disponibles, y Retrofit genera automaticamente el codigo necesario para realizar las llamadas. Internamente usa OkHttp como motor de red y delega en un convertidor la tarea de transformar JSON en objetos Kotlin y viceversa.

### 3.4 Configuracion centralizada del cliente
Es una buena practica de arquitectura tener un unico punto de configuracion del cliente Retrofit (por ejemplo, un `object` en Kotlin, que es inherentemente un singleton). Esto evita crear multiples instancias del cliente HTTP, que consumen recursos y pueden generar comportamientos inconsistentes.

## 4. Ejemplo de codigo en Kotlin

```kotlin
// Dependencias necesarias en build.gradle.kts (modulo app):
// implementation("com.squareup.retrofit2:retrofit:2.11.0")
// implementation("com.squareup.retrofit2:converter-gson:2.11.0")
// implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
```

```kotlin
// Modelo de datos que representa una tarea recibida desde la API
data class TareaDto(
    val id: Int,
    val titulo: String,
    val completada: Boolean
)
```

```kotlin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Interfaz que describe los endpoints disponibles del servicio de tareas
interface TareaApiService {

    @GET("tareas")
    fun obtenerTareas(): Call<List<TareaDto>>

    @GET("tareas/{id}")
    fun obtenerTareaPorId(@Path("id") id: Int): Call<TareaDto>
}
```

```kotlin
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Objeto unico (singleton) responsable de configurar y exponer el cliente Retrofit
object RetrofitClient {

    private const val BASE_URL = "https://api.ejemplo.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tareaApiService: TareaApiService by lazy {
        retrofit.create(TareaApiService::class.java)
    }
}
```

## 5. Ejercicio practico para los estudiantes

Conectar la aplicacion del proyecto integrador con un servicio REST, cumpliendo los siguientes puntos:

1. Agregar las dependencias de Retrofit, el convertidor JSON elegido (Gson o Kotlinx Serialization) y el interceptor de logging de OkHttp.
2. Definir uno o mas modelos de datos (`data class`) que representen los recursos principales de la aplicacion (tareas, productos, usuarios, etc.), segun el proyecto de cada estudiante.
3. Crear una interfaz de servicio con al menos un endpoint `GET` que describa como obtener la lista de recursos desde la API (puede usarse JSONPlaceholder como servicio de practica si el proyecto aun no tiene backend propio).
4. Implementar un objeto `RetrofitClient` que centralice la configuracion del cliente HTTP, incluyendo la URL base y el convertidor JSON.
5. Verificar, usando los logs del interceptor, que la configuracion compila correctamente (la llamada efectiva a la red se realizara en la siguiente sesion).

## 6. Material de apoyo / enlaces

- Documentacion oficial de Retrofit: https://square.github.io/retrofit/
- Documentacion oficial de OkHttp: https://square.github.io/okhttp/
- API publica de practica JSONPlaceholder: https://jsonplaceholder.typicode.com
- Guia de Kotlinx Serialization: https://kotlinlang.org/docs/serialization.html
- Guia de Gson en GitHub: https://github.com/google/gson
- Arquitectura de aplicaciones Android (capa de datos): https://developer.android.com/topic/architecture/data-layer
