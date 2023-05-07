package com.example.cryptoapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCoinInfoBinding
import com.example.cryptoapp.domain.CoinInfoEntity
import com.squareup.picasso.Picasso

class CoinInfoAdapter(private val context: Context) :
    ListAdapter<CoinInfoEntity, CoinInfoAdapter.CoinInfoViewHolder>(CoinInfoDiffUtil()) {

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinInfoEntity)
    }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val binding = ItemCoinInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = currentList[position]
        with(holder) {
            with(coin) {
                val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                tvSymbols.text = String.format(symbolsTemplate, fromSymbol, toSymbol)
                tvPrice.text = price
                tvLastUpdate.text = String.format(lastUpdateTemplate, lastUpdate)
                Picasso.get().load(imageUrl).into(ivLogoCoin)
                itemView.setOnClickListener {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: CoinInfoViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] is Bundle) {
            val bundle = payloads[0] as Bundle
            holder.update(bundle, context)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

     class CoinInfoViewHolder(binding: ItemCoinInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val ivLogoCoin = binding.ivLogoCoin
        val tvSymbols = binding.tvSymbols
        val tvPrice = binding.tvPrice
        val tvLastUpdate = binding.tvLastUpdate

        fun update(bundle: Bundle, context: Context) {
            if (bundle.containsKey(CoinInfoDiffUtil.PRICE_KEY)) {
                tvPrice.text = bundle.getString(CoinInfoDiffUtil.PRICE_KEY)
            }
            if (bundle.containsKey(CoinInfoDiffUtil.LAST_UPDATE_KEY)) {
                tvLastUpdate.text = String.format(
                    context.resources.getString(R.string.last_update_template),
                    bundle.getString(CoinInfoDiffUtil.LAST_UPDATE_KEY)
                )
            }
        }
    }

    class CoinInfoDiffUtil : DiffUtil.ItemCallback<CoinInfoEntity>() {

        companion object {
            const val PRICE_KEY = "new_price_value"
            const val LAST_UPDATE_KEY = "new_last_update_value"
        }

        override fun areItemsTheSame(oldItem: CoinInfoEntity, newItem: CoinInfoEntity): Boolean {
            return oldItem.fromSymbol == newItem.fromSymbol
        }

        override fun areContentsTheSame(oldItem: CoinInfoEntity, newItem: CoinInfoEntity): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CoinInfoEntity, newItem: CoinInfoEntity): Any? {
            if (
                oldItem.toSymbol == newItem.toSymbol &&
                oldItem.imageUrl == newItem.imageUrl
            ) {
                val payloads = Bundle()
                if (oldItem.price != newItem.price) {
                    payloads.putString(PRICE_KEY, newItem.price)
                }
                if (
                    oldItem.lastUpdate != newItem.lastUpdate &&
                    oldItem.lastUpdate != null &&
                    newItem.lastUpdate != null
                ) {
                    payloads.putString(LAST_UPDATE_KEY, newItem.lastUpdate)
                }
                return payloads
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }
}