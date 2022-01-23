package com.robosoft.foursquare.util

import com.robosoft.foursquare.room.FavouriteModel

interface CellClickListener {
    fun onCellClickListener(data: FavouriteModel,isRemove:Boolean)
}