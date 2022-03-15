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
    private val value by lazy { args.currency.value.toBigDecimal() }
    private val nominal by lazy { args.currency.nominal.toBigDecimal() }
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
        when (val ruCount = binding.edittextRu.text.toString().toBigDecimalOrNull()) {
            null -> binding.edittextOther.setText("", TextView.BufferType.EDITABLE)
            else -> binding.edittextOther.setText(
                (ruCount * nominal).divide(value, 2, RoundingMode.DOWN).toPlainString(),
                TextView.BufferType.EDITABLE
            )
        }
    }

    private fun onOtherNumberChange() {
        when (val otherCount = binding.edittextOther.text.toString().toBigDecimalOrNull()) {
            null -> binding.edittextRu.setText("", TextView.BufferType.EDITABLE)
            else -> binding.edittextRu.setText(
                (value * otherCount).divide(nominal, 2, RoundingMode.DOWN).toPlainString(),
                TextView.BufferType.EDITABLE
            )
        }
    }
}
