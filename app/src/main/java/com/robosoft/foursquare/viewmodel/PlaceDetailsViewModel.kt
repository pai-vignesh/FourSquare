package com.robosoft.foursquare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.robosoft.foursquare.repository.MainRepository
import com.robosoft.foursquare.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel  @Inject constructor(private val repository: MainRepository) : ViewModel(){
    fun getPlaceDetails(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(data = repository.getPlaceDetails(query)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }
}