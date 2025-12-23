package com.example.weatherapp.feature.calculator.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.FragmentCalculatorBinding
import com.example.weatherapp.feature.calculator.presentation.viewmodel.CalculatorEvent
import com.example.weatherapp.feature.calculator.presentation.viewmodel.CalculatorViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeState()
    }

    private fun initListeners() {
        binding.btnClear.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Clear)
        }
        binding.btnDelete.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Delete)
        }
        binding.btnDivide.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Operator("/"))
        }
        binding.btnMultiply.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Operator("*"))
        }
        binding.btnMinus.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Operator("-"))
        }
        binding.btnPlus.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Operator("+"))
        }

        binding.btn0.setOnClickListener { sendNumber(0) }
        binding.btn1.setOnClickListener { sendNumber(1) }
        binding.btn2.setOnClickListener { sendNumber(2) }
        binding.btn3.setOnClickListener { sendNumber(3) }
        binding.btn4.setOnClickListener { sendNumber(4) }
        binding.btn5.setOnClickListener { sendNumber(5) }
        binding.btn6.setOnClickListener { sendNumber(6) }
        binding.btn7.setOnClickListener { sendNumber(7) }
        binding.btn8.setOnClickListener { sendNumber(8) }
        binding.btn9.setOnClickListener { sendNumber(9) }

        binding.btnDot.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Decimal)
        }
        binding.btnEquals.setOnClickListener {
            viewModel.onEvent(CalculatorEvent.Calculate)
        }
    }

    private fun sendNumber(n: Int) {
        viewModel.onEvent(CalculatorEvent.Number(n))
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                binding.tvExpression.text = state.expression.ifEmpty { "0" }
                binding.tvResult.text = state.result

                if (state.error != null) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = state.error
                } else {
                    binding.tvError.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}