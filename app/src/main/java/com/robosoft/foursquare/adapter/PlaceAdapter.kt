package com.robosoft.foursquare.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.PlaceItemAdapterBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.view.PlaceDetailsActivity
import java.text.DecimalFormat
import androidx.core.content.ContextCompat


class PlaceAdapter(
    private val cellClickListener: CellClickListener,
    private val currentLocation: Location
) :
    RecyclerView.Adapter<PlaceAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<PlaceData>() {
        override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem.fsqId == newItem.fsqId
        }

        override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return newItem == oldItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var placeData: List<PlaceData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    class MyViewHolder(binding: PlaceItemAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        private val placeName = binding.placeName
        private val placeType = binding.placeType
        private val distance = binding.distance
        private val placePrice = binding.price
        private val placeAddress = binding.placeAddress
        private val placeImg = binding.imageView
        private val ratings = binding.ratings
        private val fav = binding.fav
        private val unFav = binding.unfav
        private val card = binding.layout
        private var imageUrl = ""

        fun bind(
            cellClickListener: CellClickListener,
            place: PlaceData,
            currentLocation: Location
        ) {
            if (!place.photos.isNullOrEmpty()) {
                val requestOptions = RequestOptions().diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                val imgSize = "400x400"
                imageUrl =
                    place.photos[0].prefix + imgSize + place.photos[0].suffix
                Glide.with(placeImg.context).load(imageUrl).apply(requestOptions)
                    .into(placeImg)
            }
            val destLocation = Location("destination")
            destLocation.latitude = place.geocodes.main.latitude.toDouble()
            destLocation.longitude = place.geocodes.main.longitude.toDouble()
            val km = DecimalFormat("##.##").format(currentLocation.distanceTo(destLocation) / 1000)
            distance.text = distance.context.getString(R.string.card_distance_text, km)
            place.rating?.let { stars ->
                val finalRating = ((stars * 5) / 10)
                when {
                    finalRating>=4 -> {
                        ratings.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(ratings.context,R.color.green900))
                    }
                    finalRating>=3 -> {
                        ratings.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(ratings.context,R.color.green700))
                    }
                    finalRating>=2 -> {
                        ratings.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(ratings.context,R.color.green500))
                    }
                    else -> {
                        ratings.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(ratings.context,R.color.green300))
                    }
                }
                ratings.text = String.format("%.1f",finalRating)
            }

            place.price?.let { price ->
                when (price) {
                    1 -> placePrice.text = placePrice.context.getString(R.string.expense, "₹")
                    2 -> placePrice.text = placePrice.context.getString(R.string.expense, "₹₹")
                    3 -> placePrice.text = placePrice.context.getString(R.string.expense, "₹₹₹")
                    4 -> placePrice.text = placePrice.context.getString(R.string.expense, "₹₹₹₹")
                    5 -> placePrice.text = placePrice.context.getString(R.string.expense, "₹₹₹₹₹")
                    else -> placePrice.text = placePrice.context.getString(R.string.expense, "")
                }
            }
            placeName.text = place.name
            if (place.categories.isNotEmpty()) {
                placeType.text = place.categories[0].name
            }
            placeAddress.text = place.location.address
            fav.setOnClickListener {
                fav.visibility = View.GONE
                unFav.visibility = View.VISIBLE
                val favouriteModel = FavouriteModel(
                    place.fsqId,
                    place.name,
                    placeType.text.toString(),
                    placePrice.text.toString(),
                    place.geocodes.main.latitude,
                    place.geocodes.main.longitude,
                    ratings.text.toString(),
                    imageUrl,
                    placeAddress.text.toString()
                )
                cellClickListener.onCellClickListener(favouriteModel, false)
            }

            unFav.setOnClickListener {
                unFav.visibility = View.GONE
                fav.visibility = View.VISIBLE
                val favouriteModel = FavouriteModel(
                    place.fsqId,
                    place.name,
                    placeType.text.toString(),
                    placePrice.text.toString(),
                    place.geocodes.main.latitude,
                    place.geocodes.main.longitude,
                    ratings.text.toString(),
                    imageUrl,
                    placeAddress.text.toString()
                )
                cellClickListener.onCellClickListener(favouriteModel, true)
            }

            card.setOnClickListener { v ->
                val intent = Intent(v.context, PlaceDetailsActivity::class.java)
                intent.putExtra("fsqId", place.fsqId)
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PlaceItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(cellClickListener, placeData[position], currentLocation)
    }

    override fun getItemCount(): Int {
        return placeData.size
    }
}