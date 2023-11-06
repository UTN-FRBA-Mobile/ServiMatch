package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class ProviderProfile(
    @SerializedName("id")
    val id: Int,

    @SerializedName("image_path")
    val imagePath: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("profesion")
    val profesion: String,

    @SerializedName("cant_signos_pesos")
    val cantSignosPesos: Int,

    @SerializedName("precio_visita")
    val precio_visita: String,

    @SerializedName("ubicaciones")
    val ubicaciones: List<String>,

    @SerializedName("disponibilidad")
    val disponibilidad: List<String>,

    @SerializedName("servicios_completados")
    val serviciosCompletados: Int,

    @SerializedName("comentarios")
    val comentarios: List<Comentario>
)

