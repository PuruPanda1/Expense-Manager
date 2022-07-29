package com.example.payment.rcAdapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R
import com.example.payment.cryptoDb.CryptoTransaction
import com.example.payment.fragments.stats.StatsDirections
import com.example.payment.fragments.wallet.cryptoPortfolio.CryptoPortfolio
import com.example.payment.fragments.wallet.cryptoPortfolio.CryptoPortfolioDirections
import com.example.payment.transactionDb.Transaction

class CryptoPortfolioAdapter(private val fragment: CryptoPortfolio) :
    RecyclerView.Adapter<CryptoPortfolioHolder>() {
    private var dataList: List<CryptoTransaction> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoPortfolioHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.portfolio_row, parent, false)
        return CryptoPortfolioHolder(listItem)
    }

    override fun onBindViewHolder(holder: CryptoPortfolioHolder, position: Int) {
        val item = dataList[position]
        holder.symbol.text = item.symbol
        holder.count.text = item.numberOfCoins.toString()

        val currentValue = item.numberOfCoins * item.currentPrice
        var number3digits: Double = Math.round(currentValue * 1000.0) / 1000.0
        var number2digits: Double = Math.round(number3digits * 100.0) / 100.0
        var currentValueSolution: Double = Math.round(number2digits * 10.0) / 10.0
        holder.currentValue.text = "₹${currentValueSolution}"

        var profitLoss = item.numberOfCoins * (item.currentPrice - item.buyingPrice)
        number3digits = Math.round(profitLoss * 1000.0) / 1000.0
        number2digits = Math.round(number3digits * 100.0) / 100.0
        profitLoss = Math.round(number2digits * 10.0) / 10.0
        if (profitLoss >= 0) {
            holder.profitLoss.setTextColor(
                ContextCompat.getColor(
                    holder.count.context,
                    R.color.income_color
                )
            )
        } else {
            holder.profitLoss.setTextColor(
                ContextCompat.getColor(
                    holder.count.context,
                    R.color.expense_color
                )
            )
        }

        var percentage = (profitLoss / (item.numberOfCoins*item.currentPrice)) * 100
        number3digits = Math.round(percentage * 1000.0) / 1000.0
        number2digits = Math.round(number3digits * 100.0) / 100.0
        percentage = Math.round(number2digits * 10.0) / 10.0

        holder.profitLoss.text = "₹${profitLoss}(${percentage}%)"

        holder.layout.setOnLongClickListener {
            delete(it, holder.layout.context, item)
            true
        }
    }

    private fun delete(v: View, c: Context, item: CryptoTransaction) {
        //                    Alert dialog box for confirmation
        val builder = AlertDialog.Builder(c)
        builder.setPositiveButton("Yes") { _, _ ->
//      Toast.makeText(c, "yes", Toast.LENGTH_SHORT).show()
            fragment.deleteCryptoTransaction(item)
            notifyDataSetChanged()
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.setTitle("Delete Transaction")
        builder.setMessage("Are you sure to delete this transaction?")
        builder.create().show()
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(list: List<CryptoTransaction>) {
        dataList = list
        notifyDataSetChanged()
    }
}

class CryptoPortfolioHolder(view: View) : RecyclerView.ViewHolder(view) {
    val symbol: TextView = view.findViewById<TextView>(R.id.investmentSymbol)
    val count: TextView = view.findViewById<TextView>(R.id.investmentQty)
    val currentValue: TextView = view.findViewById<TextView>(R.id.investmentCV)
    val profitLoss: TextView = view.findViewById<TextView>(R.id.investmentPL)
    val layout: CardView = view.findViewById<CardView>(R.id.cryptoLayout)
}