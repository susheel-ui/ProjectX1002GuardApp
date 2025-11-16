package com.example.project_b_security_gardapp.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_b_security_gardapp.databinding.ItemImageBinding

class ImageAdapter(private val images: List<Uri>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = images[position]
        Glide.with(holder.itemView.context)
            .load(uri)
            .centerCrop()
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = images.size
}
