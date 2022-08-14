package com.example.payment.rcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.DetailedTransactionAnalysis
import com.example.payment.DetailedTransactionAnalysisDirections
import com.example.payment.R
import com.example.payment.transactionDb.myTypes
import java.text.NumberFormat
import java.util.*

class TransactionTypeAdapter(val fragment: DetailedTransactionAnalysis,val currency: String) :
    ListAdapter<myTypes, TransactionTypeAdapter.TransactionTypeViewHolder>(Comparator()) {


    class TransactionTypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.transactionTypeName)
        val amount: TextView = view.findViewById(R.id.transactionTypeAmount)
        private val count: TextView = view.findViewById(R.id.transactionTypeCount)
        private val image: ImageView = view.findViewById(R.id.transactionTypeIcon)
        val layout: CardView = view.findViewById(R.id.transactionTypeLayout)

        fun bind(item: myTypes, fragment: DetailedTransactionAnalysis,currency: String) {
            val currencyFormatter = NumberFormat.getCurrencyInstance()
            currencyFormatter.maximumFractionDigits = 1
            currencyFormatter.currency = Currency.getInstance(currency)
            name.text =
                String.format(name.context.getString(R.string.transactionTypeName, item.name))
            amount.text = currencyFormatter.format(item.amount)
            if (item.count > 1) {
                count.text =
                    String.format(name.context.getString(R.string.transactionTypeCount, item.count))
            } else {
                count.text = String.format(
                    name.context.getString(
                        R.string.transactionTypeCountSingular,
                        item.count
                    )
                )
            }
            when (item.name) {
                "DineOut" -> image.setImageResource(R.drawable.pizza_icon)
                "Bills" -> image.setImageResource(R.drawable.bill_icon)
                "Credit Card Due" -> image.setImageResource(R.drawable.creditcard_icon)
                "Entertainment" -> image.setImageResource(R.drawable.entertainment_icon)
                "Fuel" -> image.setImageResource(R.drawable.fuel_icon)
                "Groceries" -> image.setImageResource(R.drawable.grocery_icon)
                "Shopping" -> image.setImageResource(R.drawable.shopping_icon)
                "Stationary" -> image.setImageResource(R.drawable.stationary_icon)
                "Suspense" -> image.setImageResource(R.drawable.general_icon)
                "Transportation" -> image.setImageResource(R.drawable.transportation_icon)
                else -> image.setImageResource(R.drawable.ic_entertainment)
            }
//        setting the onclick listener
            layout.setOnClickListener {
                val action =
                    DetailedTransactionAnalysisDirections.actionDetailedTransactionAnalysisToDetailedCategoryTransactionsFragment(
                        item.name,
                        fragment.startDate,
                        fragment.endDate
                    )
                Navigation.findNavController(layout).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionTypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detailed_analysis_row, parent, false)
        return TransactionTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionTypeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, fragment, currency)
    }

    class Comparator : DiffUtil.ItemCallback<myTypes>() {
        override fun areItemsTheSame(oldItem: myTypes, newItem: myTypes): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: myTypes, newItem: myTypes): Boolean {
            return oldItem == newItem
        }

    }
}