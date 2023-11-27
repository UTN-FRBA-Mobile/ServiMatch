package ar.com.utn.devmobile.servimatch.ui.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Properties

object ApiClient {
    val properties = Properties()

    private const val BASE_URL = "http://10.0.2.2:5000"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}