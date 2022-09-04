package com.purabmodi.payment.rcAdapter.acTransferAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.purabmodi.payment.R
import com.purabmodi.payment.accountsDb.Accounts
import com.purabmodi.payment.fragments.stats.accountTransfer.AccountTransfer

class FromBankAdapter(val fragment: AccountTransfer) : RecyclerView.Adapter<AccountsHolder>() {
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
        if (selectedItem == item.name) {
            holder.name.setTextColor(holder.layout.context.getColor(R.color.white))
            holder.layout.setCardBackgroundColor(holder.layout.context.getColor(R.color.primary_blue))
        } else {
            holder.layout.setCardBackgroundColor(holder.layout.context.getColor(R.color.optionsBackground))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.black))
        }

        holder.layout.setOnClickListener {
            selectedItem = item.name
            fragment.setFromBankAccount(item.name)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list: List<Accounts>) {
        data = list
        notifyDataSetChanged()
    }

    fun setSelectedItem(selectedItem: String) {
        this.selectedItem = selectedItem
        notifyDataSetChanged()
    }
}

class AccountsHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.accountNameRC)
    val layout: CardView = view.findViewById(R.id.accountLayoutRC)
}