package com.example.cryptoapp.presentation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCoinInfoBinding
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import com.squareup.picasso.Picasso

class PriceListAdapter(private val context: Context) :
    PagingDataAdapter<CoinInfoEntity, PriceListAdapter.PriceListViewHolder>(PriceListDiffUtil()) {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceListViewHolder {
        val binding = ItemCoinInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PriceListViewHolder(binding).also { it.setOnItemClickListener() }
    }

    override fun onBindViewHolder(holder: PriceListViewHolder, position: Int) {
        val item = getItem(position)
        holder.setData(item)
    }

    override fun onBindViewHolder(
        holder: PriceListViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] is Bundle) {
            val bundle = payloads[0] as Bundle
            val tvPrice = bundle.getString(PriceListDiffUtil.PRICE_KEY)
            val tvLastUpdate = bundle.getString(PriceListDiffUtil.LAST_UPDATE_KEY)?.let {
                String.format(
                    context.resources.getString(R.string.last_update_template),
                    bundle.getString(PriceListDiffUtil.LAST_UPDATE_KEY)
                )
            }
            holder.updateData(tvPrice, tvLastUpdate)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(coinInfo: CoinInfoEntity, itemImage: View)
    }

    inner class PriceListViewHolder(binding: ItemCoinInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val ivLogoCoin = binding.ivLogoCoin
        private val tvSymbols = binding.tvSymbols
        private val tvPrice = binding.tvPrice
        private val tvLastUpdate = binding.tvLastUpdate

        fun setData(coinInfo: CoinInfoEntity?) {
            coinInfo?.let { data ->
                val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                tvSymbols.text = String.format(
                    symbolsTemplate,
                    data.fromSymbol,
                    data.toSymbol
                )
                tvPrice.text = data.price
                tvLastUpdate.text = String.format(lastUpdateTemplate, data.lastUpdate)
                ivLogoCoin.transitionName = coinInfo.fromSymbol
                Picasso.get().load(data.imageUrl).into(ivLogoCoin)
            }
        }

        fun updateData(tvPrice: String?, tvLastUpdate: String?) {
            tvPrice?.let { this.tvPrice.text = it }
            tvLastUpdate?.let { this.tvLastUpdate.text = it }
        }

        fun setOnItemClickListener() {
            itemView.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let {
                    onItemClickListener?.onItemClick(it, ivLogoCoin)
                }
            }
        }
    }

    class PriceListDiffUtil : DiffUtil.ItemCallback<CoinInfoEntity>() {

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