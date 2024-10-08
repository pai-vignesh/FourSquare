package com.robosoft.foursquare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.robosoft.foursquare.repository.RoomRepository
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val roomRepository: RoomRepository) :
    ViewModel() {

    fun deleteFavourites(favouriteModel: FavouriteModel) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(roomRepository.deleteFavourites(favouriteModel)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }
    val favourites = roomRepository.favourites
}