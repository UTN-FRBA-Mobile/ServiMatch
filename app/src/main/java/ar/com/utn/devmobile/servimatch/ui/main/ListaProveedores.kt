package ar.com.utn.devmobile.servimatch.ui.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.model.ApiService
import ar.com.utn.devmobile.servimatch.ui.model.BodyRequestBusqueda
import ar.com.utn.devmobile.servimatch.ui.model.FiltroAplicado
import ar.com.utn.devmobile.servimatch.ui.model.ProviderInfo

class ListaDeProveedores : ViewModel() {

    val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)

    //Esta lista se muestra en la primera mitad de la pantalla home
    val recomendados= mutableStateOf<MutableList<ProviderInfo>>(mutableListOf())

    //Esta lista se muestra en la segunda mitad de la pantalla home
    val general = mutableStateOf<MutableList<ProviderInfo>>(mutableListOf())

    //Esta lista solo se muestra si no esta vacia. Y se arma cuando se aplican filtros.
    val busqueda = mutableStateOf<MutableList<ProviderInfo>>(mutableListOf())

    //Esta lista contiene los filtros que aplico
    val filtrosAplicados = mutableListOf<FiltroAplicado>()

    //Esta funcion se ejecuta cuando renderiza el HomeActivity. Le pega a la base y hace un get a los recomendados.
    suspend fun getRecomendados(){
        try {
            Log.d("--->", apiService.recomendados().toString())

        } catch (e: Exception) {
            // Podríamos agregar una alerta de error si falla la carga.
            Log.d("--->", e.toString())
        }

        recomendados.value = mutableListOf<ProviderInfo>().apply {
            add(ProviderInfo(0,R.drawable.hombre1, "Joaquin Benitez","$$$$",  "Palermo - Plomero", "plomero"))
            add( ProviderInfo(1,R.drawable.hombre2, "Eduardo Alarcón","$$$",  "Recoleta - Cerrajero", "cerrajero"))
            add(ProviderInfo(2,R.drawable.mujer1, "Maria Esperanza","$$",  "Belgrano - Plomero", "Plomero"))
        }
    }

    //Esta funcion se ejecuta cuando renderiza el HomeActivity. Le pega a la base y hace un get general.
    fun getGeneral(){
        general.value = mutableListOf<ProviderInfo>().apply {
            add(ProviderInfo(3,R.drawable.hombre3, "Lucas Sainz","$$",  "Flores - Reparación Aire Acondicionado", "reparacion aire acondicionado"))
            add(ProviderInfo(4,R.drawable.mujer2, "Eugenia Romano","$",  "Caballito - Limpieza Hogar ", "limpieza hogar"))

        }
    }

    //  Esta funcion se dispara cuando se aprieta algun filtro en el HomeActivity. En el composable Filter.
    fun buscarPorFiltro(categoria: String, valor: String) {

        // Buscar si ya existe un filtro con la misma categoría
        val filtroExistenteIndex = filtrosAplicados.indexOfFirst { it.categoria == categoria }

        if (filtroExistenteIndex != -1) {
            // Si ya existe un filtro con la misma categoría, reemplazarlo con uno nuevo
            val filtroExistente = filtrosAplicados[filtroExistenteIndex]
            val filtroActualizado = filtroExistente.copy(valor = valor)
            filtrosAplicados[filtroExistenteIndex] = filtroActualizado
        } else {
            // Si no existe, agregar un nuevo FiltroAplicado
            filtrosAplicados.add(FiltroAplicado(categoria, valor))
        }
        //Armo el body que voy a enviar al servicio.
        val body = BodyRequestBusqueda(filtrosAplicados);

        //Le pego y me traigo los proveedores. Los cargo en la lista de busqueda, cuando deje de ser vacia se renderiza.
        busqueda.value = mutableListOf<ProviderInfo>().apply {
            addAll(busqueda.value)
            add( ProviderInfo(2,R.drawable.mujer1, "Maria Esperanza","$$",  "Belgrano - Plomero", "plomero")
            )
            add(ProviderInfo(0,R.drawable.hombre1, "Joaquin Benitez","$$$$",  "Palermo - Plomero", "plomero"),
            )

        }
    }
}
