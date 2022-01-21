package com.robosoft.foursquare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.robosoft.foursquare.repository.MainRepository
import com.robosoft.foursquare.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: MainRepository) : ViewModel(){

    fun getPlaceReviews(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getPlaceReviews(query)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}