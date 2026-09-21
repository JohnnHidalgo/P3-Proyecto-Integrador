package com.upb.taskmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Sesion 20: Room - persistencia local.
 *
 * Base de datos Room de la app. Se declara `abstract` porque Room genera la
 * implementacion real en tiempo de compilacion (con el procesador de
 * anotaciones `kapt`). El companion object implementa un singleton simple
 * (con `synchronized` para evitar crear dos instancias en hilos distintos),
 * que es el patron recomendado por la documentacion oficial de Room: abrir
 * una base de datos es costoso, asi que conviene reutilizar siempre la misma
 * instancia durante toda la vida de la app.
 */
@Database(entities = [TareaEntity::class], version = 1, exportSchema = false)
abstract class TareaDatabase : RoomDatabase() {

    abstract fun tareaDao(): TareaDao

    companion object {
        private const val NOMBRE_BASE_DE_DATOS = "task_manager.db"

        @Volatile
        private var instancia: TareaDatabase? = null

        fun obtenerInstancia(contexto: Context): TareaDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    TareaDatabase::class.java,
                    NOMBRE_BASE_DE_DATOS
                ).build().also { instancia = it }
            }
        }
    }
}
