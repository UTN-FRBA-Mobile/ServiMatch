package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token")
    val token: String
)
