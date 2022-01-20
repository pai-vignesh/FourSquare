package com.robosoft.foursquare.util

import com.robosoft.foursquare.model.Photo

interface PhotoClickListener {
    fun onPhotoClickListener(photo: Photo)
}