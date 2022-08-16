package com.example.payment.fragments.mainScreen.detailedTransactions.totalAnalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.R
import com.example.payment.databinding.FragmentDetailedTransactionAnalysisBinding
import com.example.payment.rcAdapter.TransactionTypeAdapter
import com.example.payment.transactionDb.MyTypes
import com.example.payment.userDb.UserViewModel
import com.example.payment.viewModel.TransactionViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailedTransactionAnalysis : Fragment() {
    var startDate: Long = 0
    var endDate: Long = 0
    private var _binding: FragmentDetailedTransactionAnalysisBinding? = null
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
    private var amount = 0f
    private var currency = MutableLiveData("INR")
    private var currencyFormatter = NumberFormat.getCurrencyInstance()
    private lateinit var userViewModel: UserViewModel
    private lateinit var viewModel: TransactionViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            FragmentDetailedTransactionAnalysisBinding.inflate(layoutInflater, container, false)

//        setting up the back button
        binding.backToMainScreen.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_detailedTransactionAnalysis_to_MainScreen)
        }
//        defaults for the currency Formatter
        currencyFormatter.maximumFractionDigits = 1
        currencyFormatter.currency = Currency.getInstance(currency.value)

        //        setting the curreny Formater for the page
        currency.observe(viewLifecycleOwner) {
            currencyFormatter.currency = Currency.getInstance(currency.value)
            setAmount()
        }

//      setting the viewModels
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

//        set monthly balance amount
        val month = months[Calendar.getInstance().get(Calendar.MONTH)]
        binding.detailedMonthlyDuration.text =
            String.format(getString(R.string.monthlyDuration, month))
//        observer for balance
        viewModel.readMonthlySpends.observe(viewLifecycleOwner) {
            amount = it ?: 0f
            setAmount()
        }
//      setting the adapter for the recyclerview
        lateinit var adapter: TransactionTypeAdapter
        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                currency.value = it.userCurrency
            }
            adapter = TransactionTypeAdapter(this, currency.value!!)
            binding.transactionTypeRC.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionTypeRC.adapter = adapter
        }


//        setting the rangePicker
        binding.rangePicker.setOnClickListener {
            openRangePicker(adapter)
        }
        viewModel.readTransactionTypeAmount.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            updateChart(it)
        }

//        setting the pie chart
        setupAnalysisPieChart()

        return binding.root
    }

    private fun setAmount() {
        binding.detailedmonthlyExpense.text = currencyFormatter.format(amount)
    }

    private fun openRangePicker(adapter: TransactionTypeAdapter) {
        val simpleDateFormat = SimpleDateFormat("dd MMM YY")
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Choose duration")
            .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "datepicker")

        dateRangePicker.addOnPositiveButtonClickListener {
            startDate = it.first
            endDate = it.second
            viewModel.setCustomDurationData(listOf(it.first, (it.second + 86400000)))
        }
        viewModel.readTransactionTypeAmount.removeObservers(viewLifecycleOwner)
        viewModel.readMonthlySpends.removeObservers(viewLifecycleOwner)
        //        calender icon
        viewModel.listAccordingToDate.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            updateChart(list)
        }
        viewModel.sumAccordingToDate.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.detailedmonthlyExpense.text = currencyFormatter.format(0)
            } else {
                binding.detailedmonthlyExpense.text = currencyFormatter.format(it)
            }
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
        binding.analysisPieChart.isRotationEnabled = true
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

    private fun updateChart(list: List<MyTypes>) {
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
        data.setValueFormatter(PercentFormatter(binding.analysisPieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
        binding.analysisPieChart.data = data
        binding.analysisPieChart.invalidate()
        binding.analysisPieChart.animateY(1400, Easing.EaseInOutQuad)
    }


}