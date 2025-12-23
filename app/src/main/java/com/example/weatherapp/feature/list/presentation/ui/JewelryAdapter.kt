package com.example.weatherapp.feature.list.presentation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemListBinding
import com.example.weatherapp.feature.list.domain.model.Jewelry

class JewelryAdapter(
    private var jewelryList: List<Jewelry>
) : RecyclerView.Adapter<JewelryAdapter.JewelryViewHolder>() {

    inner class JewelryViewHolder(
        private val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(jewelry: Jewelry) {
            val context = itemView.context

            val typeText = when (jewelry.type) {
                "keychain" -> context.getString(R.string.jewelry_type_keychain)
                "necklace" -> context.getString(R.string.jewelry_type_necklace)
                "hairclip" -> context.getString(R.string.jewelry_type_hairclip)
                else -> jewelry.type
            }

            val brandText = when (jewelry.brand) {
                "Sanrio" -> context.getString(R.string.jewelry_brand_sanrio)
                "Aliexpress" -> context.getString(R.string.jewelry_brand_aliexpress)
                else -> jewelry.brand
            }

            binding.tvTypeBrand.text = "$typeText • $brandText"
            binding.tvDescription.text = jewelry.description
            binding.tvCost.text = context.getString(R.string.price_format, jewelry.cost)
            binding.ivJewelryImage.setImageResource(jewelry.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JewelryViewHolder {
        val binding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JewelryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JewelryViewHolder, position: Int) {
        holder.bind(jewelryList[position])
    }

    override fun getItemCount(): Int = jewelryList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Jewelry>) {
        jewelryList = newList
        notifyDataSetChanged()
    }
}
