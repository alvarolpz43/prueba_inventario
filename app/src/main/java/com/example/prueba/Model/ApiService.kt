package com.example.prueba.Model


import com.example.prueba.Inventory.Data.DataPedidosResponse
import com.example.prueba.Inventory.Data.DataProductsResponse
import com.example.prueba.Inventory.Data.DataReuqestProduct
import com.example.prueba.Inventory.Data.PedidoRequest
import com.example.prueba.Login.LoginRequest
import com.example.prueba.Login.LoginResponse
import com.example.prueba.User.DataProfile
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
     fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("profile")
    suspend fun getprofileUser(@Header("Authorization") token: String): Response<DataProfile>

    @GET("product")
    suspend fun getAllProducts(): Response<DataProductsResponse>

    @POST("pedidos")
    suspend fun postPedidos(
        @Header("Authorization") token: String,
        @Body pedidoRequest: PedidoRequest
    ): Response<DataPedidosResponse>
}

