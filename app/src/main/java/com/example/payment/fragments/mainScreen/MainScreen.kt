package com.example.payment.fragments.mainScreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.payment.R
import com.example.payment.TransactionViewModel
import com.example.payment.databinding.FragmentMainScreenBinding
import com.example.payment.db.Transaction
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import kotlin.collections.ArrayList

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val months = listOf<String>(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "November",
        "December"
    )
    private lateinit var viewModel: TransactionViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

//        setting viewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

//        set balance amount
        viewModel.readDifferenceSum.observe(viewLifecycleOwner, Observer {
            binding.balanceAmount.text =
                String.format(getString(R.string.amountInRupee, it.toString()))
        })

//        set monthly balance amount
        val month = months[Calendar.getInstance().get(Calendar.MONTH)]
        binding.monthlyDuration.text = String.format(getString(R.string.monthlyDuration, month))
//        observer for balance
        viewModel.readMonthlySpends.observe(viewLifecycleOwner, Observer {
            binding.monthlyExpense.text =
                String.format(getString(R.string.amountInRupee, it.toString()))
        })

//         setting the pie chart
        setupPieChart()
//        observer for pie chart
        var income = 0f
        var expense = 0f
        viewModel.incomeSum.observe(viewLifecycleOwner, Observer {
            income = it
            updateChart(income,expense)
        })

        viewModel.expenseSum.observe(viewLifecycleOwner, Observer {
            expense = it
            updateChart(income,expense)
        })

//      analysis button
        binding.checkDetailedAnalysisBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun setupPieChart() {
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
        binding.pieChart.description.text = ""
        binding.pieChart.isDrawHoleEnabled = true
        var c = binding.monthlyBudgetCard.cardBackgroundColor
        binding.pieChart.setHoleColor(c.defaultColor)
        var l = binding.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment =Legend.LegendHorizontalAlignment.RIGHT
        l.orientation=Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.textColor = ContextCompat.getColor(requireContext(), R.color.primaryTextColor)
        l.isEnabled = true
    }

    fun updateChart(income:Float,expense:Float) {
        var entries = mutableListOf<PieEntry>()
        var colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(), R.color.income_color))
//        colors.add(ContextCompat.getColor(requireContext(), R.color.expense_color))
        colors.add(Color.RED)
        entries.add(PieEntry(income,"Income"))
        entries.add(PieEntry(expense,"Expense"))
        var dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        var data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(binding.pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
        binding.pieChart.data = data
        binding.pieChart.invalidate()
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}