package com.example.payment.rcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R
import com.example.payment.transactionDb.AccountDetails

class WalletAccountDetailsAdapter :
    ListAdapter<AccountDetails, WalletAccountDetailsAdapter.AccountViewHolder>(Comparator()) {

    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.walletAccountNameRC)
        private val balance: TextView = view.findViewById(R.id.walletAccountBalanceRC)

        fun bind(item: AccountDetails) {
            name.text = item.accountName
            balance.text = balance.context.getString(
                R.string.amountInRupee,
                item.accountBalance.toString()
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.wallet_accounts_row, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class Comparator : DiffUtil.ItemCallback<AccountDetails>(){
        override fun areItemsTheSame(oldItem: AccountDetails, newItem: AccountDetails): Boolean {
            return oldItem.accountName == newItem.accountName
        }

        override fun areContentsTheSame(oldItem: AccountDetails, newItem: AccountDetails): Boolean {
            return oldItem == newItem
        }

    }
}