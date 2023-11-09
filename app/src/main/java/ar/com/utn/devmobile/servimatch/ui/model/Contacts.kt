package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class Contacts(
    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("email")
    val email: String
)