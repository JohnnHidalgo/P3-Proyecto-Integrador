package com.upb.taskmanager.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Sesion 14: Retrofit.
 *
 * Define los endpoints REST que consume la app. Retrofit genera la
 * implementacion de esta interfaz automaticamente (ver [RetrofitInstance]).
 *
 * NOTA DIDACTICA: se usa `https://jsonplaceholder.typicode.com/`, una API
 * REST publica y gratuita pensada para practicar, unicamente con fines de
 * aprendizaje. No es un backend real de Task Manager UPB; en un proyecto en
 * produccion este `baseUrl` apuntaria al backend propio de la aplicacion.
 */
interface ApiService {

    /**
     * Obtiene una lista de tareas de ejemplo desde el endpoint `/todos`.
     * Es una funcion `suspend` porque Retrofit 2.6+ soporta corrutinas de
     * forma nativa: se puede llamar directamente desde una corrutina, sin
     * necesidad de callbacks (ese uso con corrutinas se profundiza en la
     * Sesion 15).
     */
    @GET("todos")
    suspend fun obtenerTareasRemotas(@Query("_limit") limite: Int = 10): List<TareaDto>
}
