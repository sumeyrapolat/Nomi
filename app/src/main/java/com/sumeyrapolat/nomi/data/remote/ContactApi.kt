package com.sumeyrapolat.nomi.data.remote

import com.sumeyrapolat.nomi.data.remote.dto.CreateUserRequest
import com.sumeyrapolat.nomi.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.*
interface ContactApi {

    @GET("api/User/getAll")
    @Headers("Accept: application/json")
    suspend fun getAllUsers(
        @Header("ApiKey") apiKey: String
    ): Response<ApiResponse<UsersWrapper>>

    @POST("api/User")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun createUser(
        @Header("ApiKey") apiKey: String,
        @Body body: CreateUserRequest
    ): Response<ApiResponse<UserResponse>>

    @DELETE("api/User/{id}")
    @Headers("Accept: application/json")
    suspend fun deleteUser(
        @Header("ApiKey") apiKey: String,
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}
