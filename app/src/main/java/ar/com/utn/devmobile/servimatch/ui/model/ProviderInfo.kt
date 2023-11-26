package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class ProviderInfo(
    @SerializedName("id")
    val identificador: Int,

    @SerializedName("image_path")
    val imageResource: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("cant_signos_pesos")
    val priceSimbol: Int,

    @SerializedName("ubicaciones")
    val location: List<String>,

    @SerializedName("profesion")
    val rol: String,

    @SerializedName("recomendado")
    val isRecommended: Boolean,

    @SerializedName("latitud")
    val latitud: Double,

    @SerializedName("longitud")
    val longitud: Double,

    @SerializedName("rangoMax")
    val rangoMax: Double,

    @SerializedName("puntaje")
    val puntaje: Float
)