package com.upb.taskmanager.data.remote

import com.upb.taskmanager.util.Resultado
import retrofit2.HttpException
import java.io.IOException

/**
 * Sesion 15: Retrofit + Corrutinas.
 *
 * Repositorio remoto: aisla al resto de la app de los detalles de Retrofit
 * ([ApiService], [RetrofitInstance]). Quien use este repositorio no necesita
 * saber que la implementacion usa Retrofit por debajo; solo le importa que
 * puede pedir tareas remotas de forma asincrona con una funcion `suspend`.
 *
 * Sesion 16: la llamada de red se envuelve en un [Resultado], para que quien
 * la use (el [com.upb.taskmanager.viewmodel.TareasViewModel]) no tenga que
 * lidiar con excepciones directamente y pueda mostrar un mensaje de error
 * legible en la UI si algo falla (sin conexion, error del servidor, etc.).
 */
class TareasRemoteRepository(
    private val apiService: ApiService = RetrofitInstance.apiService
) {

    /**
     * Obtiene la lista de tareas de ejemplo desde el servicio remoto. Al ser
     * `suspend`, debe llamarse desde una corrutina (por ejemplo, dentro de un
     * `viewModelScope.launch`).
     */
    suspend fun obtenerTareasRemotas(): Resultado<List<TareaDto>> {
        return try {
            Resultado.Exito(apiService.obtenerTareasRemotas())
        } catch (sinConexion: IOException) {
            Resultado.Error("No se pudo conectar con el servidor. Revisa tu conexion a internet.")
        } catch (errorHttp: HttpException) {
            Resultado.Error("El servidor respondio con un error (codigo ${errorHttp.code()}).")
        } catch (errorInesperado: Exception) {
            Resultado.Error("Ocurrio un error inesperado al obtener las tareas remotas.")
        }
    }
}
