package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class ReservaRequest(
    @SerializedName("turno_selected")
    val turnoSelected: String,

    @SerializedName("date")
    val fecha: String,

    @SerializedName("username")
    val cliente: String,

    @SerializedName("price")
    val precioConsulta: String
)
