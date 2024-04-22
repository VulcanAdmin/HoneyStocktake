package com.example.honeystocktake.api

import retrofit2.Response

class Repository {

    suspend fun getSheet(uniqueId: String, plateValue: String): Response<Datamodel> {
        return RetrofitInstance.api.getSheet(uniqueId, plateValue)
    }

//    suspend fun pushStock(uniqueId: String, plateValue: String, locationValue: String, stockBy: String): Response<Datamodel> {
//        return RetrofitInstance.api.pushStock(uniqueId, plateValue, locationValue, stockBy)
//    }

    suspend fun pushQStock(uniqueId: String, plateValue: String, locationValue: String, stockBy: String, query: Int, length: Double?, width: Double?): Response<Datamodel> {
        return RetrofitInstance.api.pushQStock(uniqueId, plateValue, locationValue, stockBy , query, length, width)
    }

    suspend fun getUser(uniqueId: String, pin: String): Response<ApiResponse> {
        return RetrofitInstance.api.getUser(uniqueId, pin)
    }

    suspend fun getSpinnerOptions(uniqueId: String): Response<List<spinnerListData>>? {
        return RetrofitInstance.api.getSpinnerOptions(uniqueId)
    }

    suspend fun getLocations(uniqueId: String): Response<List<Location>>? {
        return RetrofitInstance.api.getLocations(uniqueId)
    }

    suspend fun pushBulk(uniqueId: String, plateValue: String, plateValue2: String, locationValue: String, stockBy: String): Response<bulk> {
        return RetrofitInstance.api.pushBulk(uniqueId, plateValue, plateValue2, locationValue, stockBy)
    }
}