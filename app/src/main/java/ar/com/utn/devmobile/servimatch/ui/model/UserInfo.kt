package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("username")
    val username: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("latitud")
    val latitud: Double,

    @SerializedName("longitud")
    val longitud: Double
)
