package com.robosoft.foursquare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.foursquare.databinding.ReviewAdapterBinding
import com.robosoft.foursquare.model.Tip
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Tip>() {
        override fun areItemsTheSame(oldItem: Tip, newItem: Tip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tip, newItem: Tip): Boolean {
            return newItem == oldItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var tips: List<Tip>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    class MyViewHolder(binding: ReviewAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        private val date = binding.date
        private val reviewText = binding.review
        fun bind(tip: Tip) {
            reviewText.text = tip.text
            date.text = convertDate(tip.createdAt)
        }

        private fun convertDate(dateString: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val sdf = SimpleDateFormat("MMM dd,yyyy", Locale.US)
            val date: Date? = format.parse(dateString)
            date?.let {
                return sdf.format(it)
            }
            return ""
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ReviewAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount(): Int {
        return tips.size
    }
}