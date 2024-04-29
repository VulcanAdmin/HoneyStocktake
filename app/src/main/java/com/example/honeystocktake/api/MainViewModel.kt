package com.example.honeystocktake.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    var myResponse: MutableLiveData<Response<Datamodel>> = MutableLiveData()
    var myResponse2: MutableLiveData<Response<ApiResponse>> = MutableLiveData()
    var myPost: MutableLiveData<Response<Datamodel>> = MutableLiveData()
    var spinnerResponse: MutableLiveData<Response<List<spinnerListData>>> = MutableLiveData()
    var locationsResponse: MutableLiveData<Response<List<Location>>> = MutableLiveData()
    var bulkPost: MutableLiveData<Response<bulk>> = MutableLiveData()



    fun getSheet(uniqueId: String, plateValue: String){
        viewModelScope.launch {
            val response = repository.getSheet(uniqueId, plateValue)
            myResponse.value = response
        }
    }

    fun pushQStock(uniqueId: String, plateValue: String, locationValue: String, stockBy: String, query: Int, length: Double?, width: Double?) {
        viewModelScope.launch {
            val response = repository.pushQStock(uniqueId, plateValue, locationValue, stockBy, query, length, width)
            myPost.value = response
        }
    }

    fun getUser(uniqueId: String, pin: String){
        viewModelScope.launch {
            val response = repository.getUser(uniqueId, pin)
            myResponse2.value = response
        }
    }

    fun getSpinnerOptions(uniqueId: String){
        viewModelScope.launch {
            val response = repository.getSpinnerOptions(uniqueId)
            spinnerResponse.value = response
        }
    }

    fun getLocations(uniqueId: String){
        viewModelScope.launch {
            val response = repository.getLocations(uniqueId)
            locationsResponse.value = response
        }
    }

    fun pushBulk(uniqueId: String, plateValue: String, plateValue2: String, locationValue: String, stockBy: String, length: Double?, width: Double?) {
        viewModelScope.launch {
            val response = repository.pushBulk(uniqueId, plateValue, plateValue2, locationValue, stockBy, length, width)
            bulkPost.value = response
        }
    }
}