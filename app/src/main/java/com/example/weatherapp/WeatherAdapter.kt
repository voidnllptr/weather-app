package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.domain.model.WeatherForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherAdapter(
    private val onItemClick: (WeatherForecast) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var items: List<WeatherForecast> = emptyList()

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.tvDate)
        private val tempText: TextView = itemView.findViewById(R.id.tvTemperature)
        private val descriptionText: TextView = itemView.findViewById(R.id.tvDescription)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.ivWeatherIcon)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)

        @SuppressLint("SetTextI18n")
        fun bind(item: WeatherForecast) {
            dateText.text = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
                .format(Date(item.date * 1000))
            tempText.text = "${item.temp.toInt()}°C"
            descriptionText.text = item.description

            cardView.setCardBackgroundColor(getTemperatureColor(item))

            weatherIcon.visibility = View.GONE

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun getTemperatureColor(item: WeatherForecast): Int {
            val weatherMain = item.weather
            val temp = item.temp
            val color = when {
                weatherMain.contains("rain", ignoreCase = true) || weatherMain == "lightrain" || weatherMain == "tsrain" -> 
                    ContextCompat.getColor(itemView.context, R.color.card_background_rain)
                weatherMain == "snow" -> 
                    ContextCompat.getColor(itemView.context, R.color.card_background_snow)
                weatherMain.contains("cloudy", ignoreCase = true) || weatherMain == "pcloudy" || weatherMain == "mcloudy" -> 
                    ContextCompat.getColor(itemView.context, R.color.card_background_clouds)
                temp < 0 -> ContextCompat.getColor(itemView.context, R.color.temp_cold)
                temp < 10 -> ContextCompat.getColor(itemView.context, R.color.temp_cool)
                temp < 20 -> ContextCompat.getColor(itemView.context, R.color.temp_warm)
                else -> ContextCompat.getColor(itemView.context, R.color.temp_hot)
            }
            return color
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<WeatherForecast>) {
        items = newItems
        notifyDataSetChanged()
    }
}

