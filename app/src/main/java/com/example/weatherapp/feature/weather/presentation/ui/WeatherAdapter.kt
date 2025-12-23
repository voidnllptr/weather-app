package com.example.weatherapp.feature.weather.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.feature.weather.domain.model.WeatherForecast
import com.example.weatherapp.feature.weather.domain.model.WeatherTypeDisplay

class WeatherAdapter(
    var forecasts: List<WeatherForecast>,
    private val context: Context,
    private val onItemClick: (WeatherForecast) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(
        itemView: View,
        private val onItemClick: ((Int) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeekText: TextView = itemView.findViewById(R.id.tvDayOfWeek)
        val formattedDateText: TextView = itemView.findViewById(R.id.tvFormattedDate)
        val weatherText: TextView = itemView.findViewById(R.id.tvWeather)
        val maxTempText: TextView = itemView.findViewById(R.id.tvMaxTemp)
        val minTempText: TextView = itemView.findViewById(R.id.tvMinTemp)
        val windSpeedText: TextView = itemView.findViewById(R.id.tvWindSpeed)
        val todayIndicator: TextView = itemView.findViewById(R.id.tvTodayIndicator)
        val weatherIcon: ImageView = itemView.findViewById(R.id.ivWeatherIcon)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(itemView) { position ->
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(forecasts[position])
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val forecast = forecasts[position]
        val weatherDisplay = WeatherTypeDisplay.fromApiString(forecast.weather)

        holder.dayOfWeekText.text = forecast.dayOfWeek
        holder.formattedDateText.text = forecast.formattedDate

        holder.weatherText.text = "${weatherDisplay.emoji} ${weatherDisplay.displayName}"

        holder.maxTempText.text = forecast.getFormattedMaxTemp()
        holder.minTempText.text = forecast.getFormattedMinTemp()

        holder.windSpeedText.text = forecast.windSpeed?.let { "$it м/с" } ?: "—"

        holder.todayIndicator.visibility = if (forecast.isToday) View.VISIBLE else View.GONE

        holder.weatherIcon.setImageResource(weatherDisplay.drawableResId)
    }
    override fun getItemCount(): Int = forecasts.size
}