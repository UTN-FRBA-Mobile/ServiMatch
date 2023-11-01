package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class Comentario(
    @SerializedName("image_path")
    val foto: String,

    @SerializedName("nombre_usuario")
    val nombreUsuario: String,

    @SerializedName("comentario")
    val comentario: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("puntaje")
    val puntaje: Double
)