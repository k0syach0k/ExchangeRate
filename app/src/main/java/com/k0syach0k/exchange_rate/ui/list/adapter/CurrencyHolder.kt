package com.k0syach0k.exchange_rate.ui.list.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.k0syach0k.exchange_rate.R
import com.k0syach0k.exchange_rate.model.currency.Currency

class CurrencyHolder(
    private val view: View,
    private val context: Context,
    onItemClicked: (position: Int) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener { onItemClicked(bindingAdapterPosition) }
    }

    fun bind(currency: Currency) {
        view.findViewById<TextView>(R.id.currencyCharCode).text = currency.charCode
        view.findViewById<TextView>(R.id.currencyName).text =
            context.getString(R.string.nominal_name_currency_text, currency.nominal, currency.name)
        view.findViewById<TextView>(R.id.currencyValue).text = currency.value.toString()
        when {
            currency.previousValue < currency.value ->
                view.findViewById<ImageView>(R.id.currencyChangeRateImage)
                    .setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.arrow_downward_24))
            currency.previousValue > currency.value ->
                view.findViewById<ImageView>(R.id.currencyChangeRateImage)
                    .setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.arrow_upward_24))
        }
    }
}
