package com.k0syach0k.exchange_rate.ui.convert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.k0syach0k.exchange_rate.databinding.FragmentConvertBinding
import java.math.RoundingMode

class ConvertFragment : Fragment() {

    private var _binding: FragmentConvertBinding? = null
    private val args by navArgs<ConvertFragmentArgs>()
    private val currency by lazy { args.currency }
    private var editFlag = true

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConvertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textviewOther.text = currency.name
        binding.edittextRu.doAfterTextChanged {
            if (editFlag) {
                editFlag = false
                onRuNumberChange()
                editFlag = true
            }
        }
        binding.edittextOther.doAfterTextChanged {
            if (editFlag) {
                editFlag = false
                onOtherNumberChange()
                editFlag = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onRuNumberChange() {
        val ruCount = binding.edittextRu.text.toString().toBigDecimalOrNull()
        if (ruCount != null) {
            val value = currency.value.toBigDecimal()
            val nominal = currency.nominal.toBigDecimal()
            val otherCount = (ruCount * nominal).divide(value, 2, RoundingMode.DOWN)
            binding.edittextOther.setText(otherCount.toPlainString(), TextView.BufferType.EDITABLE)
        } else {
            binding.edittextOther.setText("", TextView.BufferType.EDITABLE)
        }
    }

    private fun onOtherNumberChange() {
        val otherCount = binding.edittextOther.text.toString().toBigDecimalOrNull()
        if (otherCount != null) {
            val value = currency.value.toBigDecimal()
            val nominal = currency.nominal.toBigDecimal()
            val ruCount = (value * otherCount).divide(nominal, 2, RoundingMode.DOWN)
            binding.edittextRu.setText(ruCount.toPlainString(), TextView.BufferType.EDITABLE)
        } else {
            binding.edittextRu.setText("", TextView.BufferType.EDITABLE)
        }
    }
}
