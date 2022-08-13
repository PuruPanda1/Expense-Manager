package com.example.payment.rcAdapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.DetailedCategoryTransactionsFragment
import com.example.payment.R
import com.example.payment.fragments.stats.StatsDirections
import com.example.payment.transactionDb.Transaction
import java.text.SimpleDateFormat

class TransactionsCategoryWiseAdapter(val fragment: DetailedCategoryTransactionsFragment) :
    RecyclerView.Adapter<TransactionsCategoryWiseHolder>() {
    private var data = emptyList<Transaction>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionsCategoryWiseHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.transactions_row_rc, parent, false)
        return TransactionsCategoryWiseHolder(listItem)
    }

    override fun onBindViewHolder(holder: TransactionsCategoryWiseHolder, position: Int) {
        // set the values to the Views using holder variable
        val simpleDateFormat = SimpleDateFormat("dd MMM")
        val item = data[position]
        val dateString = simpleDateFormat.format(item.date)
        holder.description.text = item.tDescription
        holder.account.text = holder.account.context.getString(R.string.paidViaString,item.modeOfPayment)
        holder.date.text = dateString
        holder.category.text = item.transactionType

        if (item.isExpense) {
            holder.amount.setTextColor(
                ContextCompat.getColor(
                    holder.amount.context,
                    R.color.expense_color
                )
            )
            val updatedAmount =
                holder.amount.resources.getString(
                    R.string.amountInRupee,
                    item.expenseAmount.toString()
                )
            holder.amount.text = updatedAmount
        } else {
            holder.amount.setTextColor(
                ContextCompat.getColor(
                    holder.amount.context,
                    R.color.income_color
                )
            )
            val updatedAmount =
                holder.amount.resources.getString(
                    R.string.amountInRupee,
                    item.incomeAmount.toString()
                )
            holder.amount.text = updatedAmount
        }

        when (item.transactionType) {
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
            "Salary" -> holder.image.setImageResource(R.drawable.salary_icon)
            "Income" -> holder.image.setImageResource(R.drawable.income_icon)
            else -> holder.image.setImageResource(R.drawable.ic_entertainment)
        }

        holder.layout.setOnLongClickListener {
            popupMenus(it, holder.layout.context, item)
            true
        }

    }

    override fun getItemCount(): Int {
        // return the size of the list
        return data.size
    }

    fun setData(list: List<Transaction>) {
        data = list
        notifyDataSetChanged()
    }

    //    pop up menu for edit and delete options on long click on elements
    private fun popupMenus(v: View, c: Context, item: Transaction) {
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
                        notifyDataSetChanged()
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

class TransactionsCategoryWiseHolder(val view: View) : RecyclerView.ViewHolder(view) {
    // Get the views using findViewById and store it in varible to store the data
    val description: TextView = view.findViewById(R.id.descriptionShowRC)
    val category: TextView = view.findViewById(R.id.categoryShowRC)
    val amount: TextView = view.findViewById(R.id.amountShowRC)
    val date: TextView = view.findViewById(R.id.dateShowRC)
    val layout: CardView = view.findViewById(R.id.eachRowLayout)
    val image: ImageView = view.findViewById(R.id.floatingTransactionIcon)
    val account: TextView = view.findViewById(R.id.accountRC)
}