package ar.com.utn.devmobile.servimatch.ui.main

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.model.ApiClient
import ar.com.utn.devmobile.servimatch.ui.model.ApiService
import ar.com.utn.devmobile.servimatch.ui.model.BodyRequestBusqueda
import ar.com.utn.devmobile.servimatch.ui.model.FiltroAplicado
import ar.com.utn.devmobile.servimatch.ui.model.ProviderInfo

fun List<ProviderInfo>?.safeAccess(block: (List<ProviderInfo>) -> Unit) {
    this?.let { block(it) }
}

class ListaDeProveedores : ViewModel() {

    // Declarar una variable mutable para el indicador de carga
    var isLoading = true
    //Esta lista se muestra en la primera mitad de la pantalla home
    val recomendados= mutableStateOf<MutableList<ProviderInfo>>(mutableListOf())

    //Esta lista se muestra en la segunda mitad de la pantalla home
    val general = mutableStateOf<MutableList<ProviderInfo>>(mutableListOf())

    //Esta lista solo se muestra si no esta vacia. Y se arma cuando se aplican filtros.
    var busqueda = mutableStateOf<MutableList<ProviderInfo>>(mutableListOf())

    //Esta lista contiene los filtros que aplico
    val filtrosAplicados = mutableListOf<FiltroAplicado>()

    //Esta funcion se ejecuta cuando renderiza el HomeActivity. Le pega a la base y hace un get a los recomendados.
    suspend fun getProvedores(latitud:Double, longitud: Double){
        try {

            val response = ApiClient.apiService.getProvidersByCoordinates(latitud,longitud)
            val providers = response.body()
            providers.safeAccess{ providersInfo ->
                recomendados.value = providersInfo.filter { it -> it.isRecommended }.toMutableList()
                general.value = providersInfo.filter { it -> !it.isRecommended }.toMutableList()
            }
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
            // Podríamos agregar una alerta de error si falla la carga.
            Log.d("--->", e.toString())
        }

    }



    //  Esta funcion se dispara cuando se aprieta algun filtro en el HomeActivity. En el composable Filter.
    /*fun buscarPorFiltro(categoria: String, valor: String) {

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
        Log.d("FILTROS", filtrosAplicados.toString())
        //Armo el body que voy a enviar al servicio.
        val body = BodyRequestBusqueda(filtrosAplicados);

        //Le pego y me traigo los proveedores. Los cargo en la lista de busqueda, cuando deje de ser vacia se renderiza.
        busqueda.value = mutableListOf<ProviderInfo>().apply {
            addAll(busqueda.value)
            add( ProviderInfo(2,"https://statics.launion.digital/2023/07/crop/64b07fb27fe81__940x620.webp", "Maria", "Esperanza",2,  listOf("Belgrano"), "plomero", true, latitud = 0.0, longitud = 0.0, rangoMax = 0.0)
            )
            add(ProviderInfo(0,"https://statics.launion.digital/2023/07/crop/64b07fb27fe81__940x620.webp", "Joaquin", "Benitez",4,  listOf("Palermo"), "plomero", false, latitud = 0.0, longitud = 0.0, rangoMax = 0.0),
            )

        }
    }*/
}
