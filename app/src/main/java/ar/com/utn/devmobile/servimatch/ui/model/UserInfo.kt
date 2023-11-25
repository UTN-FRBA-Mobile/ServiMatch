package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("username")
    val username: String,

    @SerializedName("ubicacion")
    val ubicacion: String
)
