package com.example.payment.rcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R
import com.example.payment.fragments.wallet.Wallet
import com.example.payment.transactionDb.AccountDetails
import java.text.NumberFormat
import java.util.*

class WalletAccountDetailsAdapter(private val currency: String, val fragment: Wallet) :
    ListAdapter<AccountDetails, WalletAccountDetailsAdapter.AccountViewHolder>(Comparator()) {

    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.walletAccountNameRC)
        private val balance: TextView = view.findViewById(R.id.walletAccountBalanceRC)
        private val layout: CardView = view.findViewById(R.id.accountWalletLayout)

        fun bind(item: AccountDetails, currency: String, fragment: Wallet) {
            val currencyFormatter = NumberFormat.getCurrencyInstance()
            currencyFormatter.maximumFractionDigits = 1
            currencyFormatter.currency = Currency.getInstance(currency)
            name.text = item.accountName
            balance.text = currencyFormatter.format(item.accountBalance)

            layout.setOnLongClickListener {
                deleteAccount(item.accountName, fragment)
                true
            }
        }

        fun deleteAccount(accountName: String, fragment: Wallet) {
            fragment.deleteBankAccount(accountName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.wallet_accounts_row, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, currency, fragment)
    }

    class Comparator : DiffUtil.ItemCallback<AccountDetails>() {
        override fun areItemsTheSame(oldItem: AccountDetails, newItem: AccountDetails): Boolean {
            return oldItem.accountName == newItem.accountName
        }

        override fun areContentsTheSame(oldItem: AccountDetails, newItem: AccountDetails): Boolean {
            return oldItem == newItem
        }

    }
}