package com.robosoft.foursquare.repository

import com.robosoft.foursquare.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getNearbyPlaces(ll:String) = apiHelper.getNearbyPlaces(ll)
}