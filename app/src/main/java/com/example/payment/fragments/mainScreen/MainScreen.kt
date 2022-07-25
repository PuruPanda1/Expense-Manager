package com.example.payment.fragments.mainScreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.payment.R
import com.example.payment.TransactionViewModel
import com.example.payment.databinding.FragmentMainScreenBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.SimpleDateFormat
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
            if(it!=null) {
                binding.balanceAmount.text =
                    String.format(getString(R.string.amountInRupee, it.toString()))
            }
            else{
                binding.balanceAmount.text =
                    String.format(getString(R.string.amountInRupee, "0"))
            }
        })

//        set monthly balance amount
        val month = months[Calendar.getInstance().get(Calendar.MONTH)]
        binding.monthlyDuration.text = String.format(getString(R.string.monthlyDuration, month))
//        observer for balance
        viewModel.readMonthlySpends.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                binding.monthlyExpense.text =
                    String.format(getString(R.string.amountInRupee, it.toString()))
            }
            else{
                binding.monthlyExpense.text =
                    String.format(getString(R.string.amountInRupee, "0"))
            }
        })

//         setting the pie chart
        setupPieChart()
//        observer for pie chart
        var income = 0f
        var expense = 0f
        viewModel.incomeSum.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                income = it
            }
            updateChart(income, expense)
        })

        viewModel.expenseSum.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                expense = it
            }
            updateChart(income, expense)
        })

//      analysis button
        binding.checkDetailedAnalysisBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
        }

//      setting the data for the recent transactions
        viewModel.readAllTransaction.observe(viewLifecycleOwner, Observer { transactionList ->
            if (transactionList.isEmpty() || transactionList.size < 3) {
                binding.eachRowLayout1.isVisible = false
                binding.eachRowLayout2.isVisible = false
                binding.eachRowLayout3.isVisible = false
            } else {
                //            setting amount for the 1st transaction
                if (transactionList[0].isExpense) {
                    binding.amountShowMainScreen1.text =
                        transactionList[0].expenseAmount.toString()
                    binding.amountShowMainScreen1.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.expense_color
                        )
                    )
                } else {
                    binding.amountShowMainScreen1.text =
                        transactionList[0].incomeAmount.toString()
                    binding.amountShowMainScreen1.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.income_color
                        )
                    )
                }
//            setting amount for the 2nd transaction
                if (transactionList[1].isExpense) {
                    binding.amountShowMainScreen2.text =
                        transactionList[1].expenseAmount.toString()
                    binding.amountShowMainScreen2.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.expense_color
                        )
                    )
                } else {
                    binding.amountShowMainScreen2.text =
                        transactionList[1].incomeAmount.toString()
                    binding.amountShowMainScreen2.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.income_color
                        )
                    )
                }
//            setting amount for the 3rd transaction
                if (transactionList[2].isExpense) {
                    binding.amountShowMainScreen3.text =
                        transactionList[2].expenseAmount.toString()
                    binding.amountShowMainScreen3.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.expense_color
                        )
                    )
                } else {
                    binding.amountShowMainScreen3.text =
                        transactionList[2].incomeAmount.toString()
                    binding.amountShowMainScreen3.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.income_color
                        )
                    )
                }
//            setting date
                val simpleDateFormat = SimpleDateFormat("dd MMM")
                var dateString = simpleDateFormat.format(transactionList[0].date)
                binding.dateShowMainScreen1.text = dateString
                dateString = simpleDateFormat.format(transactionList[1].date)
                binding.dateShowMainScreen2.text = dateString
                dateString = simpleDateFormat.format(transactionList[2].date)
                binding.dateShowMainScreen3.text = dateString
//            setting category values
                binding.categoryShowMainScreen1.text =
                    transactionList[0].transactionType
                binding.categoryShowMainScreen2.text =
                    transactionList[1].transactionType
                binding.categoryShowMainScreen3.text =
                    transactionList[2].transactionType
//            setting description
                binding.descriptionShowMainScreen1.text =
                    transactionList[0].tDescription
                binding.descriptionShowMainScreen2.text =
                    transactionList[1].tDescription
                binding.descriptionShowMainScreen3.text =
                    transactionList[2].tDescription
            }
        })
        return binding.root
    }

    private fun setupPieChart() {
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.setEntryLabelColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryTextColor
            )
        )
        binding.pieChart.description.text = ""
        binding.pieChart.isDrawHoleEnabled = true
        var c = binding.monthlyBudgetCard.cardBackgroundColor
        binding.pieChart.setHoleColor(c.defaultColor)
        var l = binding.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.textColor = ContextCompat.getColor(requireContext(), R.color.primaryTextColor)
        l.isEnabled = true
    }

    fun updateChart(income: Float, expense: Float) {
        var entries = mutableListOf<PieEntry>()
        var colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(), R.color.income_color))
//        colors.add(ContextCompat.getColor(requireContext(), R.color.expense_color))
        colors.add(Color.RED)
        entries.add(PieEntry(income, "Income"))
        entries.add(PieEntry(expense, "Expense"))
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