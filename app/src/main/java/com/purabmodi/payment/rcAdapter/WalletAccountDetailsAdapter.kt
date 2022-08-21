package com.purabmodi.payment.rcAdapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.purabmodi.payment.R
import com.purabmodi.payment.fragments.wallet.Wallet
import com.purabmodi.payment.transactionDb.AccountDetails
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
                val builder = AlertDialog.Builder(it.context)
                builder.setPositiveButton("Yes") { _, _ ->
                    deleteAccount(item.accountName, fragment)
                }
                builder.setNegativeButton("No") { _, _ ->
                }
                builder.setTitle("Delete Payment Account")
                builder.setMessage("Deletion of Payment Account will remove all the transactions made on this payment account")
                builder.create().show()
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