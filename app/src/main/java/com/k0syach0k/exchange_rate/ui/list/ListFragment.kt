package com.k0syach0k.exchange_rate.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.k0syach0k.exchange_rate.R
import com.k0syach0k.exchange_rate.databinding.FragmentListBinding
import com.k0syach0k.exchange_rate.ui.list.adapter.CurrencyAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private var currencyAdapter: CurrencyAdapter? = null
    private val viewModel: ListViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        binding.refreshFAB.setOnClickListener {
            viewModel.getCurrencyRate()
        }

        currencyAdapter = CurrencyAdapter(this.requireContext()) { position: Int ->
            val args = Bundle()
            val currentCurrency = currencyAdapter!!.getCurrency(position)
            args.putParcelable("currency", currentCurrency)
            findNavController().navigate(R.id.action_ListFragment_to_ExchangeFragment, args)
        }

        with(view.findViewById<RecyclerView>(R.id.recyclerView)) {
            adapter = currencyAdapter
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.refreshFAB.hide()
                false -> binding.refreshFAB.show()
            }
        }

        viewModel.listCurrency.observe(viewLifecycleOwner) {
            currencyAdapter!!.submitList(it)
            binding.textviewEmpty.visibility = View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.dataRate.observe(viewLifecycleOwner) {
            it?.let {
                (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.list_fragment_label_at, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currencyAdapter = null
    }
}
