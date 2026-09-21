package com.upb.taskmanager.data.remote

/**
 * Sesion 15: Retrofit + Corrutinas.
 *
 * Repositorio remoto: aisla al resto de la app de los detalles de Retrofit
 * ([ApiService], [RetrofitInstance]). Quien use este repositorio no necesita
 * saber que la implementacion usa Retrofit por debajo; solo le importa que
 * puede pedir tareas remotas de forma asincrona con una funcion `suspend`.
 */
class TareasRemoteRepository(
    private val apiService: ApiService = RetrofitInstance.apiService
) {

    /**
     * Obtiene la lista de tareas de ejemplo desde el servicio remoto. Al ser
     * `suspend`, debe llamarse desde una corrutina (por ejemplo, dentro de un
     * `viewModelScope.launch`, ver [com.upb.taskmanager.viewmodel.TareasViewModel]).
     */
    suspend fun obtenerTareasRemotas(): List<TareaDto> = apiService.obtenerTareasRemotas()
}
