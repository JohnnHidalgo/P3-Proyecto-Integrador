package com.upb.taskmanager.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore de preferencias a nivel de app. Se declara como propiedad de
 * extension de [Context] (fuera de la clase) porque DataStore recomienda
 * tener una unica instancia por archivo de preferencias en toda la app.
 */
private val Context.dataStorePreferencias by preferencesDataStore(name = "preferencias_usuario")

/**
 * Sesion 22: DataStore.
 *
 * Guarda preferencias simples del usuario que deben sobrevivir a que la app
 * se cierre por completo (a diferencia del estado en memoria de un
 * ViewModel, que se pierde). Usa Preferences DataStore, el remplazo
 * recomendado de `SharedPreferences`.
 */
class PreferenciasUsuario(private val contexto: Context) {

    /**
     * `true` si el usuario prefiere que la lista de tareas se muestre
     * filtrada (solo pendientes) por defecto. Se expone como [Flow] porque
     * DataStore es asincrono: leerlo nunca bloquea el hilo principal.
     */
    val mostrarSoloPendientes: Flow<Boolean> = contexto.dataStorePreferencias.data.map { preferencias ->
        preferencias[LLAVE_MOSTRAR_SOLO_PENDIENTES] ?: false
    }

    suspend fun cambiarMostrarSoloPendientes(mostrarSoloPendientes: Boolean) {
        contexto.dataStorePreferencias.edit { preferencias ->
            preferencias[LLAVE_MOSTRAR_SOLO_PENDIENTES] = mostrarSoloPendientes
        }
    }

    companion object {
        private val LLAVE_MOSTRAR_SOLO_PENDIENTES = booleanPreferencesKey("mostrar_solo_pendientes")
    }
}
