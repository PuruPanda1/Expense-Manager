package com.example.payment.rcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.DetailedTransactionAnalysis
import com.example.payment.DetailedTransactionAnalysisDirections
import com.example.payment.R
import com.example.payment.transactionDb.myTypes

class TransactionTypeAdapter(private val fragment:DetailedTransactionAnalysis):RecyclerView.Adapter<TransactionTypeHolder>() {
    private var list : List<myTypes> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionTypeHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.detailed_analysis_row, parent, false)
        return TransactionTypeHolder(listItem)
    }

    override fun onBindViewHolder(holder: TransactionTypeHolder, position: Int) {
        val item = list[position]
        holder.name.text = String.format(holder.name.context.getString(R.string.transactionTypeName,item.name))
        holder.amount.text = String.format(holder.name.context.getString(R.string.amountInRupee,item.amount.toString()))
        if(item.count>1){
            holder.count.text = String.format(holder.name.context.getString(R.string.transactionTypeCount,item.count))
        } else{
            holder.count.text = String.format(holder.name.context.getString(R.string.transactionTypeCountSingular,item.count))
        }
        when(item.name){
            "DineOut" -> holder.image.setImageResource(R.drawable.pizza_icon)
            "Bills" -> holder.image.setImageResource(R.drawable.bill_icon)
            "Credit Card Due" -> holder.image.setImageResource(R.drawable.creditcard_icon)
            "Entertainment" -> holder.image.setImageResource(R.drawable.entertainment_icon)
            "Fuel" -> holder.image.setImageResource(R.drawable.fuel_icon)
            "Groceries" -> holder.image.setImageResource(R.drawable.grocery_icon)
            "Shopping" -> holder.image.setImageResource(R.drawable.shopping_icon)
            "Stationary" -> holder.image.setImageResource(R.drawable.stationary_icon)
            "Suspense" -> holder.image.setImageResource(R.drawable.general_icon)
            "Transportation" -> holder.image.setImageResource(R.drawable.transportation_icon)
            else -> holder.image.setImageResource(R.drawable.ic_entertainment)
        }
//        setting the onclick listener
        holder.layout.setOnClickListener{
            val action = DetailedTransactionAnalysisDirections.actionDetailedTransactionAnalysisToDetailedCategoryTransactionsFragment(item.name.toString(),fragment.startDate,fragment.endDate)
            Navigation.findNavController(holder.layout).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDataList(dataList: List<myTypes>){
        list = dataList
        notifyDataSetChanged()
    }
}

class TransactionTypeHolder(view: View):RecyclerView.ViewHolder(view){
    val name = view.findViewById<TextView>(R.id.transactionTypeName)
    val amount = view.findViewById<TextView>(R.id.transactionTypeAmount)
    val count = view.findViewById<TextView>(R.id.transactionTypeCount)
    val image = view.findViewById<ImageView>(R.id.transactionTypeIcon)
    val layout = view.findViewById<CardView>(R.id.transactionTypeLayout)
}