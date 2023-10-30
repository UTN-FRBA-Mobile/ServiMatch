package ar.com.utn.devmobile.servimatch.ui.model

import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<JSONObject>
    @GET("/zonas")
    suspend fun zonas(): List<String>
    @GET("/tipoProfesion")
    suspend fun profesiones(): List<String>

    @GET("/valoraciones")
    suspend fun rating(): List<String>

}