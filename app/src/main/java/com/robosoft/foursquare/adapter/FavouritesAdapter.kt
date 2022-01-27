package com.robosoft.foursquare.adapter

import android.content.Intent
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FavouriteItemAdapterBinding
import com.robosoft.foursquare.preferences.Preferences
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.view.PlaceDetailsActivity
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class FavouritesAdapter(
    private val cellClickListener: CellClickListener,
    private val currentLocation: Location
) :
    RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>() {
    var favourites: ArrayList<FavouriteModel> = ArrayList()
    fun submitList(favouriteList: List<FavouriteModel>){
        val oldList = favourites
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(
            FavouriteItemDiffCallback(
                oldList,
                favouriteList
            )
        )
        favourites.clear()
        favourites.addAll(favouriteList)
        diffResult.dispatchUpdatesTo(this)
    }

    class FavouriteItemDiffCallback(
        private var oldItem : List<FavouriteModel>,
        private var newItem : List<FavouriteModel>
    ) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
           return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldItem[oldItemPosition].fsqId == newItem[newItemPosition].fsqId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldItem[oldItemPosition] == newItem[newItemPosition])
        }
    }

    class MyViewHolder(binding: FavouriteItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val placeName = binding.placeName
        private val placeType = binding.placeType
        private val distance = binding.distance
        private val placePrice = binding.price
        private val placeAddress = binding.placeAddress
        private val placeImg = binding.imageView
        private val ratings = binding.ratings
        private val fav = binding.fav
        private val card = binding.layout
        private var imageUrl = ""

        fun bind(
            cellClickListener: CellClickListener,
            favourite: FavouriteModel,
            currentLocation: Location
        ) {
            val requestOptions = RequestOptions().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
            imageUrl = favourite.placeImg
            Glide.with(placeImg.context).load(imageUrl).apply(requestOptions)
                .into(placeImg)
            val destLocation = Location("destination")
            destLocation.latitude = favourite.lat.toDouble()
            destLocation.longitude = favourite.lng.toDouble()
            val km = DecimalFormat("##.##").format(currentLocation.distanceTo(destLocation) / 1000)
            distance.text = distance.context.getString(R.string.card_distance_text, km)
            ratings.text = favourite.rating
            placePrice.text = favourite.price
            placeName.text = favourite.placeName
            placeType.text = favourite.placeType
            placeAddress.text = favourite.address
            fav.setOnClickListener {
                fav.visibility = View.GONE
                val idList= Preferences.getArrayPrefs("PlaceList",fav.context)
                idList.remove(favourite.fsqId)
                Preferences.setArrayPrefs("PlaceList",idList,fav.context)
                cellClickListener.onCellClickListener(favourite, true)
            }
            card.setOnClickListener { v ->
                val intent = Intent(v.context, PlaceDetailsActivity::class.java)
                intent.putExtra("fsqId", favourite.fsqId)
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FavouriteItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(cellClickListener, favourites[position], currentLocation)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }
}