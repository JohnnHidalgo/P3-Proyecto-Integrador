package com.upb.taskmanager.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Sesion 14: Retrofit.
 *
 * DTO (Data Transfer Object) que representa la forma exacta en la que llega
 * una tarea desde el servicio remoto de ejemplo (ver [ApiService]). Se
 * mantiene separado del modelo de dominio [com.upb.taskmanager.model.Tarea]
 * a proposito: la API externa usa nombres de campo distintos (`title`,
 * `completed`) y no tiene por que coincidir con el modelo interno de la app.
 */
data class TareaDto(
    val id: Int,
    val title: String,
    val completed: Boolean,
    @SerializedName("userId") val userId: Int = 0
)
