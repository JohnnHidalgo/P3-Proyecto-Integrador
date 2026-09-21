# Contenido de la Sesion 20 - Introduccion a Room

## 1. Objetivo de la sesion

Introducir a los estudiantes en la libreria de persistencia Room, comprendiendo el rol de sus tres piezas fundamentales (Entity, DAO y Database) para poder guardar datos de forma local y estructurada en una app Android con Kotlin.

## 2. Guion / desarrollo de la clase

### Bloque 1: Apertura (10 min)
- Se retoma brevemente la retroalimentacion del Segundo Momento de Evaluacion y se ubica esta sesion como el inicio de un nuevo bloque de contenidos: persistencia de datos.
- Se plantea la pregunta motivadora: hasta ahora, si el usuario cierra la app, se pierde toda la informacion que ingreso, como resolvemos esto de forma robusta.
- Se presenta Room como la solucion recomendada por Android para persistencia local estructurada, en contraste con alternativas mas simples como SharedPreferences (que se retomaran en la Sesion 22 con DataStore).

### Bloque 2: Exposicion de contenido (35 min)
- Que es Room y por que existe: una capa de abstraccion sobre SQLite que reduce el codigo repetitivo y agrega verificacion en tiempo de compilacion de las consultas SQL.
- Los tres componentes principales de Room:
  - Entity: una clase de datos que representa una tabla de la base de datos.
  - DAO (Data Access Object): una interfaz que define las operaciones de acceso a los datos (consultas, inserciones, actualizaciones, eliminaciones).
  - Database: la clase abstracta que junta las entidades y expone los DAO, actuando como punto de acceso principal a la base de datos.
- Se explica el rol de las anotaciones de Room (@Entity, @PrimaryKey, @Dao, @Query, @Database) y como Room genera codigo en tiempo de compilacion a partir de ellas.
- Se menciona la necesidad de usar coroutines (funciones suspend) o Flow para las operaciones de Room, ya que el acceso a la base de datos no debe bloquear el hilo principal.

### Bloque 3: Demo en vivo (30 min)
- El docente muestra en vivo, paso a paso, la creacion de las tres piezas basicas de Room para el proyecto de ejemplo de la materia (una app de gestion de tareas): la entidad TareaEntity, la interfaz TareaDao y la clase TareaDatabase.
- Se muestra como agregar las dependencias de Room en el archivo build.gradle.kts del proyecto.
- Se ejecuta la app y se verifica, con herramientas como el inspector de base de datos de Android Studio, que la tabla se crea correctamente.

### Bloque 4: Practica guiada (30 min)
- Los estudiantes replican en su propio entorno (o en el proyecto de ejemplo compartido por el docente) la creacion de una entidad, un DAO minimo (por ejemplo, solo con una consulta para obtener todos los registros) y la clase Database.
- El docente circula por el aula resolviendo dudas, con enfasis en errores comunes: falta de anotaciones, no declarar una clave primaria, o no registrar la entidad en la clase Database.

### Bloque 5: Cierre (15 min)
- Se resume la relacion entre Entity, DAO y Database, y como estas piezas se conectaran en la Sesion 21 con operaciones CRUD completas.
- Se explica la tarea para despues de la clase: configurar la base de datos del proyecto propio de cada estudiante.
- Se responde a preguntas finales.

## 3. Explicacion teorica breve

Room es la libreria de persistencia recomendada por Android Jetpack para trabajar con bases de datos SQLite de forma mas segura y con menos codigo repetitivo que usando SQLite directamente. Sus tres piezas centrales son:

- **Entity**: una clase de datos anotada con `@Entity`, donde cada instancia representa una fila de una tabla. Cada propiedad de la clase se convierte en una columna, y se debe marcar un campo como clave primaria con `@PrimaryKey`.
- **DAO (Data Access Object)**: una interfaz anotada con `@Dao` que declara los metodos de acceso a los datos. Room genera automaticamente la implementacion de estos metodos a partir de anotaciones como `@Insert`, `@Update`, `@Delete` y `@Query` (para consultas SQL personalizadas).
- **Database**: una clase abstracta anotada con `@Database`, que extiende `RoomDatabase` y declara que entidades forman parte de la base de datos y que DAO expone. Normalmente se implementa como un singleton, para evitar abrir multiples instancias de la base de datos dentro de la misma app.

Un punto clave a reforzar es que las operaciones de Room son, por defecto, operaciones de entrada/salida (I/O) potencialmente lentas, por lo que deben ejecutarse fuera del hilo principal. Room se integra naturalmente con Kotlin Coroutines: los metodos del DAO pueden marcarse como `suspend fun`, o pueden devolver un `Flow` para observar los cambios en los datos de forma reactiva.

## 4. Ejemplo de codigo en Kotlin

A continuacion se muestra el ejemplo base que se usara como hilo conductor en las siguientes sesiones: una app sencilla de gestion de tareas.

### Dependencias en build.gradle.kts (nivel de modulo)

```kotlin
dependencies {
    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
}
```

### Entity: TareaEntity.kt

```kotlin
package com.upb.gestortareas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val completada: Boolean = false
)
```

### DAO: TareaDao.kt

```kotlin
package com.upb.gestortareas.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<TareaEntity>>
}
```

Nota: en esta sesion el DAO se mantiene minimo, solo con una consulta de lectura. Las operaciones de insercion, actualizacion y eliminacion se agregaran en la Sesion 21.

### Database: TareaDatabase.kt

```kotlin
package com.upb.gestortareas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TareaEntity::class], version = 1, exportSchema = false)
abstract class TareaDatabase : RoomDatabase() {

    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCE: TareaDatabase? = null

        fun getDatabase(context: Context): TareaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    TareaDatabase::class.java,
                    "tareas_database"
                ).build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}
```

## 5. Ejercicio practico para los estudiantes

Como tarea para despues de la clase, y en linea con la seccion "Despues de la clase" de esta sesion (configurar la base de datos del proyecto), cada estudiante debe:

1. Anadir las dependencias de Room a su propio proyecto integrador.
2. Definir al menos una entidad propia relevante para su proyecto (puede ser TareaEntity si estan usando el ejemplo de gestion de tareas, u otra entidad equivalente si su proyecto es distinto).
3. Crear la interfaz DAO correspondiente con al menos una consulta de lectura (`@Query`).
4. Crear la clase Database que registre la entidad y expone el DAO, siguiendo el patron singleton mostrado en la demo.
5. Verificar, usando el inspector de base de datos de Android Studio, que la tabla se crea correctamente al ejecutar la app.

## 6. Material de apoyo / enlaces

- Documentacion oficial de Room: https://developer.android.com/training/data-storage/room
- Guia de referencia de Room (Entity, DAO, Database): https://developer.android.com/training/data-storage/room/defining-data
- Documentacion de Kotlin Coroutines (necesaria para operaciones asincronas con Room): https://kotlinlang.org/docs/coroutines-overview.html
- Guia de arquitectura de apps en Android (para ubicar donde encaja la capa de datos): https://developer.android.com/topic/architecture
