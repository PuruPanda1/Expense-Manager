package com.example.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.databinding.FragmentDetailedTransactionAnalysisBinding
import com.example.payment.rcAdapter.TransactionTypeAdapter
import com.example.payment.transactionDb.myTypes
import com.example.payment.viewModel.TransactionViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailedTransactionAnalysis : Fragment() {
    private var _binding: FragmentDetailedTransactionAnalysisBinding? = null
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
        _binding =
            FragmentDetailedTransactionAnalysisBinding.inflate(layoutInflater, container, false)

//        setting up the back button
        binding.backToMainScreen.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_detailedTransactionAnalysis_to_MainScreen)
        }

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]


//        set monthly balance amount
        val month = months[Calendar.getInstance().get(Calendar.MONTH)]
        binding.detailedMonthlyDuration.text =
            String.format(getString(R.string.monthlyDuration, month))
//        observer for balance
        viewModel.readMonthlySpends.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.detailedmonthlyExpense.text =
                    String.format(getString(R.string.amountInRupee, "0"))
            } else {
                binding.detailedmonthlyExpense.text =
                    String.format(getString(R.string.amountInRupee, it.toString()))
            }
        }

        val adapter = TransactionTypeAdapter()
        binding.transactionTypeRC.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionTypeRC.adapter = adapter

//        setting the rangePicker
        binding.rangePicker.setOnClickListener {
            openRangePicker(adapter)
        }
        viewModel.readTransactionTypeAmount.observe(viewLifecycleOwner) {
            adapter.setDataList(it)
            updateChart(it)
        }

//        setting the pie chart
        setupAnalysisPieChart()

        return binding.root
    }

    private fun openRangePicker(adapter: TransactionTypeAdapter) {
        val simpleDateFormat = SimpleDateFormat("dd MMM YY")
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Choose duration")
            .build()

        dateRangePicker.show(activity!!.supportFragmentManager, "datepicker")

        dateRangePicker.addOnPositiveButtonClickListener { it ->
            viewModel.getCustomDurationData(listOf(it.first, it.second))
        }
        viewModel.readTransactionTypeAmount.removeObservers(viewLifecycleOwner)
        viewModel.expenseSum.removeObservers(viewLifecycleOwner)
        //        calender icon
        viewModel.listAccordingToDate.observe(viewLifecycleOwner) { list ->
            adapter.setDataList(list)
        }
        viewModel.sumAccordingToDate.observe(viewLifecycleOwner) {
            binding.detailedmonthlyExpense.text =
                String.format(getString(R.string.amountInRupee, it.toString()))
        }
//        setting the duration in the card view
        viewModel.dates.observe(viewLifecycleOwner) {
            binding.detailedMonthlyDuration.text = getString(
                R.string.monthlyDuration,
                "${simpleDateFormat.format(it[0])} - ${simpleDateFormat.format(it[1])}"
            )
        }
    }

    private fun setupAnalysisPieChart() {
        binding.analysisPieChart.isDrawHoleEnabled = true
        binding.analysisPieChart.setUsePercentValues(true)
        binding.analysisPieChart.setEntryLabelTextSize(8f)
        binding.analysisPieChart.holeRadius = 65f
        binding.analysisPieChart.isRotationEnabled = false
        binding.analysisPieChart.setEntryLabelColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryTextColor
            )
        )
        binding.analysisPieChart.description.text = ""
        binding.analysisPieChart.isDrawHoleEnabled = true
        binding.analysisPieChart.setHoleColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryBackground
            )
        )
        binding.analysisPieChart.legend.isEnabled = false
        binding.analysisPieChart.setExtraOffsets(10f, 10f, 10f, 10f)
    }

    fun updateChart(list: List<myTypes>) {
        var entries = mutableListOf<PieEntry>()
        var colors = ArrayList<Int>()

        ColorTemplate.VORDIPLOM_COLORS.forEach {
            colors.add(it)
        }

//        setting the value
        list.forEach {
            entries.add(PieEntry(it.amount, it.name))
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.colors = colors
        var data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(binding.analysisPieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
        binding.analysisPieChart.data = data
        binding.analysisPieChart.invalidate()
        binding.analysisPieChart.animateY(1400, Easing.EaseInOutQuad)
    }


}