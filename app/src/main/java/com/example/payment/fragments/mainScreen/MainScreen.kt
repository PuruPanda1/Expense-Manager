package com.example.payment.fragments.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.payment.R
import com.example.payment.viewModel.TransactionViewModel
import com.example.payment.databinding.FragmentMainScreenBinding
import com.example.payment.transactionDb.Transaction
import com.example.payment.transactionDb.myTypes
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val months = listOf(
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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

//        setting viewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

//        set balance amount
        viewModel.readDifferenceSum.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.balanceAmount.text =
                    String.format(getString(R.string.amountInRupee, it.toString()))
            } else {
                binding.balanceAmount.text =
                    String.format(getString(R.string.amountInRupee, "0"))
            }
        }

//        set monthly balance amount
        val month = months[Calendar.getInstance().get(Calendar.MONTH)]
        binding.monthlyDuration.text = String.format(getString(R.string.monthlyDuration, month))
//        observer for balance
        viewModel.readMonthlySpends.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.monthlyExpense.text =
                    String.format(getString(R.string.amountInRupee, it.toString()))
            } else {
                binding.monthlyExpense.text =
                    String.format(getString(R.string.amountInRupee, "0"))
            }
        }

//         setting the pie chart
        setupPieChart()
//        observer for pie chart
        viewModel.readTransactionTypeAmount.observe(viewLifecycleOwner) {
            updateChart(it)
        }

//      analysis button
        binding.checkDetailedAnalysisBtn.setOnClickListener {
//            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_MainScreen_to_detailedTransactionAnalysis)
        }

//      setting the data for the recent transactions
        viewModel.readAllTransaction.observe(viewLifecycleOwner) { transactionList ->
            if (transactionList.isEmpty() || transactionList.size < 3) {
                binding.eachRowLayout1.isVisible = false
                binding.eachRowLayout2.isVisible = false
                binding.eachRowLayout3.isVisible = false
            } else {
                setRecentTransaction(transactionList!!)
            }
        }
        return binding.root
    }

    private fun setRecentTransaction(transactionList: List<Transaction>) {
        //            setting amount for the 1st transaction
        if (transactionList[0].isExpense) {
            binding.amountShowMainScreen1.text =
                String.format(
                    getString(
                        R.string.amountInRupee,
                        transactionList[0].expenseAmount.toString()
                    )
                )
            binding.amountShowMainScreen1.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.expense_color
                )
            )
        } else {
            binding.amountShowMainScreen1.text =
                String.format(
                    getString(
                        R.string.amountInRupee,
                        transactionList[0].incomeAmount.toString()
                    )
                )
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
                String.format(
                    getString(
                        R.string.amountInRupee,
                        transactionList[1].expenseAmount.toString()
                    )
                )
            binding.amountShowMainScreen2.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.expense_color
                )
            )
        } else {
            binding.amountShowMainScreen2.text =
                String.format(
                    getString(
                        R.string.amountInRupee,
                        transactionList[1].incomeAmount.toString()
                    )
                )
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
                String.format(
                    getString(
                        R.string.amountInRupee,
                        transactionList[2].expenseAmount.toString()
                    )
                )
            binding.amountShowMainScreen3.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.expense_color
                )
            )
        } else {
            binding.amountShowMainScreen3.text =
                String.format(
                    getString(
                        R.string.amountInRupee,
                        transactionList[2].incomeAmount.toString()
                    )
                )
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
//                setting the image
        when (transactionList[0].transactionType) {
            "DineOut" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.pizza_icon)
            "Bills" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.bill_icon)
            "Credit Card Due" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.creditcard_icon)
            "Entertainment" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.entertainment_icon)
            "Fuel" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.fuel_icon)
            "Groceries" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.grocery_icon)
            "Shopping" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.shopping_icon)
            "Stationary" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.stationary_icon)
            "Suspense" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.general_icon)
            "Transportation" -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.transportation_icon)
            else -> binding.transactionTypeFloatingIcon1.setImageResource(R.drawable.ic_entertainment)
        }
        when (transactionList[1].transactionType) {
            "DineOut" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.pizza_icon)
            "Bills" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.bill_icon)
            "Credit Card Due" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.creditcard_icon)
            "Entertainment" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.entertainment_icon)
            "Fuel" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.fuel_icon)
            "Groceries" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.grocery_icon)
            "Shopping" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.shopping_icon)
            "Stationary" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.stationary_icon)
            "Suspense" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.general_icon)
            "Transportation" -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.transportation_icon)
            else -> binding.transactionTypeFloatingIcon2.setImageResource(R.drawable.ic_entertainment)
        }
        when (transactionList[2].transactionType) {
            "DineOut" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.pizza_icon)
            "Bills" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.bill_icon)
            "Credit Card Due" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.creditcard_icon)
            "Entertainment" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.entertainment_icon)
            "Fuel" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.fuel_icon)
            "Groceries" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.grocery_icon)
            "Shopping" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.shopping_icon)
            "Stationary" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.stationary_icon)
            "Suspense" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.general_icon)
            "Transportation" -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.transportation_icon)
            else -> binding.transactionTypeFloatingIcon3.setImageResource(R.drawable.ic_entertainment)
        }
    }

    private fun setupPieChart() {
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.setEntryLabelTextSize(8f)
        binding.pieChart.isRotationEnabled = false
        binding.pieChart.holeRadius = 65f
        binding.pieChart.setEntryLabelColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryTextColor
            )
        )
        binding.pieChart.description.text = ""
        binding.pieChart.isDrawHoleEnabled = true
        val cardColor = binding.monthlyBudgetCard.cardBackgroundColor.defaultColor
        binding.pieChart.setHoleColor(cardColor)
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.setExtraOffsets(10f, 10f, 10f, 10f)
    }


    private fun updateChart(list: List<myTypes>) {
        val entries = mutableListOf<PieEntry>()
        val colors = ArrayList<Int>()

        //        10 COLORS
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color1))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color2))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color3))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color4))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color5))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color6))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color7))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color8))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color9))
        colors.add(ContextCompat.getColor(requireContext(), R.color.Color10))

//        setting the value
        list.forEach {
            entries.add(PieEntry(it.amount, it.name))
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.colors = colors
        val data = PieData(dataSet)
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