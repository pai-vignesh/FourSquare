package com.robosoft.foursquare.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robosoft.foursquare.api.ApiHelper
import com.robosoft.foursquare.model.Main
import com.robosoft.foursquare.repository.MainRepository
import com.robosoft.foursquare.viewmodel.GalleryViewModel
import com.robosoft.foursquare.viewmodel.HomeViewModel
import com.robosoft.foursquare.viewmodel.PlaceDetailsViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            return GalleryViewModel(MainRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel::class.java)){
            return PlaceDetailsViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}