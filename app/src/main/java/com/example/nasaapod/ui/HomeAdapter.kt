package com.example.nasaapod.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.example.nasaapod.R
import com.example.nasaapod.databinding.ItemHomeApodBinding
import com.example.nasaapod.ui.data.ApodData

class HomeAdapter(private val apodItemClickListener: ApodClickListener) :
    ListAdapter<ApodData, HomeAdapter.HomeViewHolder>(diffUtil) {

    inner class HomeViewHolder(val itemBinding: ItemHomeApodBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(data: ApodData) {

            itemBinding.titleTxtView.text = data.title
            itemBinding.favoriteImgView.setOnClickListener {
                if (data.isFavourite) {
                    data.isFavourite = false
                    itemBinding.favoriteImgView.setAnimation(R.raw.lottie_favourite)
                } else {
                    data.isFavourite = true
                    itemBinding.favoriteImgView.setAnimation(R.raw.lottie_favourite)
                    itemBinding.favoriteImgView.playAnimation()
                }
                apodItemClickListener.onApodFavouriteIconClick(data)
            }

            if (data.isFavourite) {
                itemBinding.favoriteImgView.setAnimation(R.raw.lottie_favourite)
                itemBinding.favoriteImgView.playAnimation()
            } else {
                itemBinding.favoriteImgView.setImageResource(R.drawable.ic_favorite_border)
            }

            Glide
                .with(itemView)
                .load(data.thumbnailUrl)
                .placeholder(R.drawable.ic_type_image)
                .priority(Priority.HIGH)
                .into(itemBinding.imageImgView)

            itemBinding.root.setOnClickListener {
                apodItemClickListener.onApodItemClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeApodBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }
}

val diffUtil = object : DiffUtil.ItemCallback<ApodData>() {
    override fun areItemsTheSame(oldItem: ApodData, newItem: ApodData): Boolean {
        return oldItem.dateInMillis == newItem.dateInMillis
    }

    override fun areContentsTheSame(oldItem: ApodData, newItem: ApodData): Boolean {
        return oldItem == newItem
    }
}

interface ApodClickListener {
    fun onApodItemClick(ApodData: ApodData)
    fun onApodFavouriteIconClick(apodData: ApodData)
}
