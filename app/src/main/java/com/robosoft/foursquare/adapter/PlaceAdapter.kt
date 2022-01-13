package com.robosoft.foursquare.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.robosoft.foursquare.databinding.PlaceItemAdapterBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.view.PlaceDetailsActivity

class PlaceAdapter (private val cellClickListener: CellClickListener) :
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
        private val placeType = binding.distanceAndValue
        private val placeAddr = binding.placeAddress
        private val placeImg = binding.imageView

        fun bind(cellClickListener: CellClickListener, place: PlaceData) {
//            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
//            Glide.with(placeImg.context).load(place.).apply(requestOptions).into(img)
            placeName.text = place.name
            if(place.categories.isNotEmpty()){
                placeType.text = place.categories[0].name
            }
            placeAddr.text = place.location.address
//            fav.setOnClickListener {
//                fav.visibility = View.GONE
//                unFav.visibility = View.VISIBLE
//            }

//            unFav.setOnClickListener {
//                unFav.visibility = View.GONE
//                fav.visibility = View.VISIBLE
//                val favouritesModel = FavouritesModel(
//                    0,
//                    photo.photographer,
//                    photo.src.medium,
//                    photo.src.portrait,
//                    true
//                )
//                cellClickListener.onCellClickListener(favouritesModel)
//            }

            placeImg.setOnClickListener { v ->
                val intent = Intent(v.context, PlaceDetailsActivity::class.java)
//                intent.putExtra("imgUrl", photo.src.portrait)
//                intent.putExtra("artistName", photo.photographer)
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
        holder.bind(cellClickListener, placeData[position])
    }

    override fun getItemCount(): Int {
        return placeData.size
    }
}