package com.purabmodi.payment.rcAdapter.filterAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.purabmodi.payment.R
import com.purabmodi.payment.fragments.stats.Stats

class CategoryFilterAdapter(val fragment: Stats) :
    RecyclerView.Adapter<CategoryFilterAdapter.CategoryHolder>() {
    private var data = listOf(
        "Credit Card Due",
        "Bills",
        "DineOut",
        "Entertainment",
        "Fuel",
        "Groceries",
        "Income",
        "Salary",
        "Shopping",
        "Stationary",
        "General",
        "Transportation"
    )
    private var selectedItem = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.account_row, parent, false)
        return CategoryHolder(listItem)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        // set the values to the Views using holder variable
        val item = data[position]
        holder.name.text = item
        if (selectedItem == item) {
            holder.layout.setBackgroundColor(holder.layout.context.getColor(R.color.primary_blue))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.white))
        } else {
            holder.layout.setBackgroundColor(holder.layout.context.getColor(R.color.optionsBackground))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.black))
        }

        holder.layout.setOnClickListener {
            selectedItem = item
            fragment.setCategoryFilter(selectedItem)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        // return the size of the list
        return data.size
    }

    fun setSelectedItem(item:String){
        selectedItem = item
        notifyDataSetChanged()
    }

    class CategoryHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.accountNameRC)
        val layout: ConstraintLayout = view.findViewById(R.id.accountLayoutRC)
    }
}