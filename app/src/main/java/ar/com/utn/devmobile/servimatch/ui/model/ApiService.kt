package ar.com.utn.devmobile.servimatch.ui.model

import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<JSONObject>

    @GET("/users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<UserInfo>

    @PATCH("/users/{username}")
    suspend fun saveUserToken(
        @Path("username") username: String,
        @Body token: TokenRequest
    ): Response<JSONObject>

    @POST("/providers/{provider_id}/reservas")
    suspend fun createReserva(
        @Path("provider_id") providerId: Int,
        @Body loginRequest: ReservaRequest
    ): Response<JSONObject>

    @GET("/tipoProfesion")
    suspend fun profesiones(): List<String>

    @GET("/valoraciones")
    suspend fun rating(): List<String>

    @GET("/providers/{id}")
    suspend fun getProvider(@Path("id") id: Int): Response<ProviderProfile>

    @GET("/providers/{id}")
    suspend fun getProviderContacts(@Path("id") id: Int): Response<ProviderContacts>

    @GET("/providers/")
    suspend fun getProviders(): Response<List<ProviderInfo>>

    @GET("/providersByCoordinates")
    suspend fun getProvidersByCoordinates(
        @Query("latitud") latitud:Double,
        @Query("longitud") longitud:Double
    ): Response<List<ProviderInfo>>

    @GET("/providers/{provider_id}/reservas/dates")
    suspend fun getProvidersUnvailableDays(
        @Path("provider_id") idProveedor: Int
    ): Response<List<String>>

}
