package com.example.payment.rcAdapter

import com.example.payment.accountsDb.Accounts
import com.example.payment.fragments.stats.addTranasactions.AddTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R

class AccountsAdapter(val fragment: AddTransaction) : RecyclerView.Adapter<AccountsHolder>() {
    private var data = emptyList<Accounts>()
    private var selectedItem = "CASH"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.account_row, parent, false)
        return AccountsHolder(listItem)
    }

    override fun onBindViewHolder(holder: AccountsHolder, position: Int) {
        // set the values to the Views using holder variable
        val item = data[position]
        holder.name.text = item.name
        if(selectedItem == item.name){
            holder.layout.setBackgroundColor(holder.layout.context.getColor(R.color.primary_blue))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.white))
        } else {
            holder.layout.setBackgroundColor(holder.layout.context.getColor(R.color.optionsBackground))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.black))
        }

        holder.layout.setOnClickListener {
            selectedItem = item.name
            fragment.setModeOfPayment(item.name)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        // return the size of the list
        return data.size
    }

    fun setData(list: List<Accounts>) {
        data = list
        notifyDataSetChanged()
    }

    fun setSelectedItem(selectedItem:String){
        this.selectedItem = selectedItem
        notifyDataSetChanged()
    }
}

class AccountsHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.accountNameRC)
    val layout: ConstraintLayout = view.findViewById(R.id.accountLayoutRC)
}