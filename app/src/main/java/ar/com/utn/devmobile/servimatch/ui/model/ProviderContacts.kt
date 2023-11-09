package ar.com.utn.devmobile.servimatch.ui.model

import com.google.gson.annotations.SerializedName

data class ProviderContacts(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("image_path")
    val image: String,

    @SerializedName("medios_de_contacto")
    val contactos: Contacts,

    @SerializedName("ubicaciones")
    val area: List<String>
)
