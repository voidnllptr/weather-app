package com.example.weatherapp.feature.weather.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.feature.weather.presentation.viewmodel.WeatherViewModel
import com.example.weatherapp.feature.weather.domain.model.WeatherForecast
import com.example.weatherapp.feature.weather.presentation.state.WeatherState
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModel()
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WeatherAdapter(
            forecasts = emptyList(),
            context = requireContext(),
            onItemClick = { forecast -> navigateToDetails(forecast) }
        )

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvWeather.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WeatherFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: WeatherState) {
        when {
            state.isLoading -> showLoading()
            state.error != null -> showError(state.error)
            else -> {
                hideLoading()
                updateWeatherData(state.forecasts)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvWeather.visibility = View.GONE
        binding.tvError.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.rvWeather.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.rvWeather.visibility = View.GONE
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = message

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Повторить") { viewModel.loadWeather() }
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateWeatherData(forecasts: List<WeatherForecast>) {

        if (forecasts.isNotEmpty()) {
            adapter.forecasts = forecasts
            adapter.notifyDataSetChanged()
            binding.rvWeather.visibility = View.VISIBLE
        } else {
            binding.rvWeather.visibility = View.GONE
        }
    }

    private fun navigateToDetails(forecast: WeatherForecast) {
        try {
            val bundle = Bundle().apply {
                putParcelable("selected_forecast", forecast)
            }
            findNavController().navigate(
                R.id.action_weatherFragment_to_weatherDetailFragment,
                bundle
            )
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Ошибка навигации", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
