package com.robosoft.foursquare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.robosoft.foursquare.repository.MainRepository
import com.robosoft.foursquare.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel(){

    fun getNearbyPlaces(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getNearbyPlaces(query)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getQueryPlaces(query: String,ll:String,sort:String = "RELEVANCE") = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val fields = "fsq_id,photos,name,price,location,categories,geocodes,rating"
            emit(Resource.success(data = repository.getQueryPlaces(query,ll,fields,sort)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}