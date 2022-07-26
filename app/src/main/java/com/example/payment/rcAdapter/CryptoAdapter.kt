package com.example.payment.rcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.payment.R
import com.example.payment.fragments.wallet.cryptoList.cryptoListFragmentDirections
import com.example.payment.model.trendingCoin

class CryptoAdapter : RecyclerView.Adapter<MyHolder>() {
    private var dataList: List<trendingCoin> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.crypto_list_row, parent, false)
        return MyHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = dataList[position]
        holder.name.text = item.name
        val priceString = String.format(
            holder.name.getResources()
                .getString(R.string.amountInRupee, item.market_data.current_price.inr.toString())
        )
        holder.price.text = priceString
        holder.symbol.text = item.symbol.uppercase()
        val url = item.image.small
        Glide.with(holder.image)
            .load(url)
            .into(holder.image)

//        onclick listener
        holder.layout.setOnClickListener {
            val action = cryptoListFragmentDirections.actionCryptoListFragmentToAddCryptoTransaction(
                item.name,
                item.market_data.current_price.inr.toFloat(),
                item.symbol,
                item.id
            )
            Navigation.findNavController(holder.layout).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(list: List<trendingCoin>) {
        dataList = list
        notifyDataSetChanged()
    }
}

class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.cryptoNameRC)
    val symbol = view.findViewById<TextView>(R.id.cryptoSymbolRC)
    val price = view.findViewById<TextView>(R.id.cryptoPriceRC)
    val layout = view.findViewById<CardView>(R.id.cryptoListLayoutRC)
    val image = view.findViewById<ImageView>(R.id.cryptoImageRC)
}