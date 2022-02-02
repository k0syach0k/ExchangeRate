package com.k0syach0k.exchange_rate.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.k0syach0k.exchange_rate.R
import com.k0syach0k.exchange_rate.model.Currency

class CurrencyAdapter(
    private val context: Context,
    private val onItemClicked: (position: Int) -> Unit
) :
    ListAdapter<Currency, CurrencyHolder>(CurrencyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return CurrencyHolder(view, context, onItemClicked)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getCurrency(position: Int): Currency {
        return this.getItem(position)
    }

    class CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.numberCode == newItem.numberCode
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }
    }
}
