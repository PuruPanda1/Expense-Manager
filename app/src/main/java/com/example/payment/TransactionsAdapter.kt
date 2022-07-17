package com.example.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.db.Transaction

class TransactionsAdapter:RecyclerView.Adapter<myholder>() {
    private var data = emptyList<Transaction>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myholder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.transactions_row_rc,parent,false)
        return myholder(listItem)
    }

    override fun onBindViewHolder(holder: myholder, position: Int) {
        // set the values to the Views using holder variable
        val item = data[position]
        holder.description.text = item.tDescription
        val updatedAmount = holder.amount.getResources().getString(R.string.amountInRupee, item.tAmount.toString())
        holder.amount.text = updatedAmount
        holder.date.text = "15 July"
        holder.category.text = item.transactionType
    }

    override fun getItemCount(): Int {
        // return the size of the list
        return data.size
    }
    fun setData(list:List<Transaction>)
    {
        data = list
        notifyDataSetChanged()
    }
}

class myholder(val view: View) : RecyclerView.ViewHolder(view){
    // Get the views using findViewById and store it in varible to store the data
    val description: TextView = view.findViewById(R.id.descriptionShowRC)
    val category: TextView = view.findViewById(R.id.categoryShowRC)
    val amount: TextView = view.findViewById(R.id.amountShowRC)
    val date : TextView = view.findViewById(R.id.dateShowRC)
}