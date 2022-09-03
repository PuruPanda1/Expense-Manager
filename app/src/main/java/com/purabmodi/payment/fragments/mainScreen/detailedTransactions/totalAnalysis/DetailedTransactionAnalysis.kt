package com.purabmodi.payment.fragments.mainScreen.detailedTransactions.totalAnalysis

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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.purabmodi.payment.MainActivity
import com.purabmodi.payment.R
import com.purabmodi.payment.databinding.FragmentDetailedTransactionAnalysisBinding
import com.purabmodi.payment.rcAdapter.TransactionTypeAdapter
import com.purabmodi.payment.transactionDb.MyTypes
import com.purabmodi.payment.userDb.UserViewModel
import com.purabmodi.payment.viewModel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class DetailedTransactionAnalysis : Fragment() {
    var startDate: Long = 0
    var endDate: Long = 0
    private var _binding: FragmentDetailedTransactionAnalysisBinding? = null
    private val months = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    private var amount = 0f
    private var currency = MutableLiveData("INR")
    private var currencyFormatter = NumberFormat.getCurrencyInstance()
    private lateinit var userViewModel: UserViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private var month :Int = 0
    private var year :Int = 0
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            FragmentDetailedTransactionAnalysisBinding.inflate(layoutInflater, container, false)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        transactionViewModel.monthYear.observe(viewLifecycleOwner) {
            month = it[0]
            year = it[1]
            val month = months[it[0] - 1]
            binding.detailedMonthlyDuration.text =
                String.format(getString(R.string.monthlyDuration, month,year.toString()))
        }

        binding.previousMonth.setOnClickListener {
            if(month == 1){
                --year;
                month = 12
            } else {
                --month
            }
            transactionViewModel.setMonthYear(listOf(month,year))
        }

        binding.nextMonth.setOnClickListener {
            if(month == 12){
                ++year;
                month = 1
            } else {
                ++month
            }
            transactionViewModel.setMonthYear(listOf(month,year))
        }


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



//        observer for balance
        transactionViewModel.readMonthlySpends.observe(viewLifecycleOwner) {
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

        transactionViewModel.readMonthlySumByCategory.observe(viewLifecycleOwner) {
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

    private fun setupAnalysisPieChart() {
        binding.analysisPieChart.minAngleForSlices = 40f
        binding.analysisPieChart.centerText = "Expenses"
        binding.analysisPieChart.setCenterTextSize(15f)
        binding.analysisPieChart.setCenterTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryTextColor
            )
        )
        binding.analysisPieChart.isDrawHoleEnabled = true
        binding.analysisPieChart.setUsePercentValues(true)
        binding.analysisPieChart.setEntryLabelTextSize(10f)
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
        binding.analysisPieChart.setExtraOffsets(12f, 12f, 12f, 12f)
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