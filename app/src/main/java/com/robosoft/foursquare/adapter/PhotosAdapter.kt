package com.robosoft.foursquare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.robosoft.foursquare.databinding.PhotoItemAdapterBinding
import com.robosoft.foursquare.model.Photo
import com.robosoft.foursquare.util.PhotoClickListener

class PhotosAdapter(private val photoClickListener: PhotoClickListener) :
    RecyclerView.Adapter<PhotosAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return newItem == oldItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var photos: List<Photo>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    class MyViewHolder(binding: PhotoItemAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imgIv

        fun bind(photoClickListener: PhotoClickListener, photo: Photo) {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            val imgSize = "400x400"
            val imageUrl = photo.prefix + imgSize + photo.suffix
            Glide.with(image.context).load(imageUrl).apply(requestOptions).into(image)
            image.setOnClickListener {
                photoClickListener.onPhotoClickListener(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PhotoItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(photoClickListener, photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}