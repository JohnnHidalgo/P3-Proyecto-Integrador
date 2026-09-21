package com.upb.taskmanager.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Sesion 14: Retrofit.
 *
 * Punto unico de acceso al cliente Retrofit configurado para la app. Se
 * expone como `object` (singleton de Kotlin) para que toda la app reutilice
 * la misma instancia de [ApiService] en vez de crear una nueva por cada
 * llamada de red.
 */
object RetrofitInstance {

    // NOTA DIDACTICA: API publica de pruebas, solo con fines de aprendizaje.
    private const val URL_BASE = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
