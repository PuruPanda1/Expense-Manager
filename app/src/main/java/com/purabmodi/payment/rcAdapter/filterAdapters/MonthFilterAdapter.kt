package com.purabmodi.payment.rcAdapter.filterAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.purabmodi.payment.R
import com.purabmodi.payment.fragments.stats.Stats

class MonthFilterAdapter(val fragment: Stats) :
    RecyclerView.Adapter<MonthFilterAdapter.MonthHolder>() {
    private var data = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private var selectedItem = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val l = LayoutInflater.from(parent.context)
        val listItem = l.inflate(R.layout.account_row, parent, false)
        return MonthHolder(listItem)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {
        // set the values to the Views using holder variable
        val item = data[position]
        holder.name.text = item
        if (selectedItem == holder.adapterPosition) {
            holder.layout.setCardBackgroundColor(holder.layout.context.getColor(R.color.primary_blue))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.white))
        } else {
            holder.layout.setCardBackgroundColor(holder.layout.context.getColor(R.color.optionsBackground))
            holder.name.setTextColor(holder.layout.context.getColor(R.color.black))
        }

        holder.layout.setOnClickListener {
            selectedItem = holder.adapterPosition
            fragment.setMonthFilter(selectedItem)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        // return the size of the list
        return data.size
    }

    fun setSelectedItem(month:Int){
        selectedItem = month
        notifyDataSetChanged()
    }

    class MonthHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.accountNameRC)
        val layout: CardView = view.findViewById(R.id.accountLayoutRC)
    }
}
