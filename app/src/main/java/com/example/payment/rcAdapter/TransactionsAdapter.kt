package com.example.payment.rcAdapter

import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R
import com.example.payment.fragments.stats.Stats
import com.example.payment.fragments.stats.StatsDirections
import com.example.payment.transactionDb.Transaction
import java.text.NumberFormat
import java.util.*

class TransactionsAdapter(val fragment: Stats, private val currency: String) :
    ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(
        Comparator()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transactions_row_rc, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, fragment, currency)
    }

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val description: TextView = view.findViewById(R.id.descriptionShowRC)
        private val category: TextView = view.findViewById(R.id.categoryShowRC)
        val amount: TextView = view.findViewById(R.id.amountShowRC)
        val date: TextView = view.findViewById(R.id.dateShowRC)
        val layout: CardView = view.findViewById(R.id.eachRowLayout)
        private val image: ImageView = view.findViewById(R.id.floatingTransactionIcon)
        private val account: TextView = view.findViewById(R.id.accountRC)

        fun bind(item: Transaction, fragment: Stats, currency: String) {
            val currencyFormatter = NumberFormat.getCurrencyInstance()
            currencyFormatter.maximumFractionDigits = 1
            currencyFormatter.currency = Currency.getInstance(currency)
            // set the values to the Views using holder variable
            val simpleDateFormat = SimpleDateFormat("dd MMM")
            val dateString = simpleDateFormat.format(item.date)
            description.text = item.tDescription
            account.text = account.context.getString(R.string.paidViaString, item.modeOfPayment)
            date.text = dateString
            category.text = item.transactionType

            if (item.isExpense) {
                amount.setTextColor(
                    ContextCompat.getColor(
                        amount.context,
                        R.color.expense_color
                    )
                )
                val updatedAmount = currencyFormatter.format(item.expenseAmount)
                amount.text = updatedAmount
            } else {
                amount.setTextColor(
                    ContextCompat.getColor(
                        amount.context,
                        R.color.income_color
                    )
                )
                val updatedAmount = currencyFormatter.format(item.incomeAmount)
                amount.text = updatedAmount
            }

            when (item.transactionType) {
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
                "Salary" -> image.setImageResource(R.drawable.salary_icon)
                "Updated Balance" -> image.setImageResource(R.drawable.salary_icon)
                "Income" -> image.setImageResource(R.drawable.income_icon)
                else -> image.setImageResource(R.drawable.ic_entertainment)
            }

            layout.setOnLongClickListener {
                popupMenus(it, layout.context, item, fragment)
                true
            }
        }

        private fun popupMenus(v: View, c: Context, item: Transaction, fragment: Stats) {
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.pop_up_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editOption -> {
//                    redirecting to add transaction page for editing
                        val action = StatsDirections.actionStatsToAddTransaction(item)
                        Navigation.findNavController(v).navigate(action)
                        true
                    }
                    R.id.deleteOption -> {
//                    Alert dialog box for confirmation
                        val builder = AlertDialog.Builder(c)
                        builder.setPositiveButton("Yes") { _, _ ->
//                        Toast.makeText(c, "yes", Toast.LENGTH_SHORT).show()
                            fragment.deleteTransaction(item)
                        }
                        builder.setNegativeButton("No") { _, _ ->
                        }
                        builder.setTitle("Delete Transaction")
                        builder.setMessage("Are you sure to delete this transaction?")
                        builder.create().show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    class Comparator : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

    }
}