package com.example.honeystocktake.api

import retrofit2.Response
import retrofit2.http.*

interface API {
    @GET("/?z=1")
    suspend fun getSheet(
        @Query("ID") uniqueId: String,
        @Query("p1") plateValue: String
    ): Response<Datamodel>


    @POST("/?z=2")
    suspend fun pushQStock(
        @Query("ID") uniqueId: String,
        @Query("p1") plateValue: String,
        @Query("p2") locationValue: String,
        @Query("p3") stockBy: String,
        @Query("p4") query: Int,
        @Query("p5") length: Double?,
        @Query("p6") width: Double?
    ): Response<Datamodel>


    @GET("/?z=3")
    suspend fun getUser(
        @Query("ID") uniqueId: String,
        @Query("p1") pin: String
    ): Response<ApiResponse>


    @GET("/?z=4")
    suspend fun getSpinnerOptions(
        @Query("ID") uniqueId: String
    ): Response<List<spinnerListData>>

    @GET("/?z=5")
    suspend fun getLocations(
        @Query("ID") uniqueId: String
    ): Response<List<Location>>

    @POST("/?z=6")
    suspend fun pushBulk(
        @Query("ID") uniqueId: String,
        @Query("p1") plateValue: String,
        @Query("p2") plateValue2: String,
        @Query("p3") locationValue: String,
        @Query("p4") stockBy: String,
        @Query("p5") length: Double?,
        @Query("p6") width: Double?
    ): Response<bulk>


}