package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class MapMarker(
    @SerializedName("latitud")
    val latitud: Double,

    @SerializedName("longitud")
    val longitud: Double,
)
