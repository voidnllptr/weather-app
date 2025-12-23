package com.example.weatherapp.feature.list.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentListBinding
import com.example.weatherapp.feature.list.presentation.viewmodel.JewelryListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class JewelryFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JewelryListViewModel by viewModel()

    private lateinit var jewelryAdapter: JewelryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeJewelryList()
    }

    private fun setupRecycler() {
        jewelryAdapter = JewelryAdapter(emptyList())
        binding.rvJewelryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jewelryAdapter
        }
    }

    private fun observeJewelryList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.jewelryList.collectLatest { list ->
                jewelryAdapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
