package com.example.payment.rcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R
import com.example.payment.fragments.stats.addTranasactions.AddTransaction
import com.example.payment.transactionDb.AccountDetails

class WalletAccountDetailsAdapter() : RecyclerView.Adapter<WalletAccountDetailsHolder>() {
    private var data = emptyList<AccountDetails>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletAccountDetailsHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.wallet_accounts_row, parent, false)
        return WalletAccountDetailsHolder(listItem)
    }

    override fun onBindViewHolder(holder: WalletAccountDetailsHolder, position: Int) {
        // set the values to the Views using holder variable
        val item = data[position]
        holder.name.text = item.accountName
        holder.balance.text = holder.balance.context.getString(R.string.amountInRupee,item.accountBalance.toString())
    }

    override fun getItemCount(): Int {
        // return the size of the list
        return data.size
    }

    fun setData(list: List<AccountDetails>) {
        data = list
        notifyDataSetChanged()
    }
}

class WalletAccountDetailsHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.walletAccountNameRC)
    val balance: TextView = view.findViewById(R.id.walletAccountBalanceRC)
//    val layout: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.accountLayoutRC)
}