package com.purabmodi.payment.fragments.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.purabmodi.payment.MainActivity
import com.purabmodi.payment.R
import com.purabmodi.payment.databinding.FragmentMainScreenBinding
import com.purabmodi.payment.transactionDb.MyTypes
import com.purabmodi.payment.userDb.UserViewModel
import com.purabmodi.payment.viewModel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private var currency: MutableLiveData<String> = MutableLiveData("INR")
    private val currencyFormatter = NumberFormat.getCurrencyInstance()
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
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private val binding get() = _binding!!
    private var balanceAmount = 0f
    private var monthlyAmount = 0f
    private var budget = 4000
    var month : Int = 0
    var year : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        transactionViewModel =
            ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        transactionViewModel.monthYear.observe(viewLifecycleOwner) {
            month = it[0]
            year = it[1]
            val month = months[it[0] - 1]
            binding.monthlyDuration.text = String.format(getString(R.string.monthlyDuration, month,year.toString()))
        }

        binding.previousMonth.setOnClickListener {
            if(month == 1){
                --year
                month = 12
            } else {
                --month
            }
            transactionViewModel.setMonthYear(listOf(month,year))
        }

        binding.nextMonth.setOnClickListener {
            if(month == 12){
                ++year
                month = 1
            } else {
                ++month
            }
            transactionViewModel.setMonthYear(listOf(month,year))
        }

//        setting the greeting text
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 12..16 -> {
                binding.greetingMsg.text = getString(R.string.afternoon)
            }
            in 17..20 -> {
                binding.greetingMsg.text = getString(R.string.evening)
            }
            in 21..23 -> {
                binding.greetingMsg.text = getString(R.string.night)
            }
            in 1..7 -> {
                binding.greetingMsg.text = getString(R.string.night)
            }
            else -> {
                binding.greetingMsg.text = getString(R.string.morning)
            }
        }

        binding.incomeBox.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToIncomeExpenseView(
                "INCOME",
                currency.value!!
            )
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.expenseBox.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToIncomeExpenseView(
                "EXPENSE",
                currency.value!!
            )
            Navigation.findNavController(binding.root).navigate(action)
        }

//        default values for currency formatter
        currencyFormatter.maximumFractionDigits = 1
        currencyFormatter.currency = Currency.getInstance(currency.value)

//        change in the currency
        currency.observe(viewLifecycleOwner) {
            currencyFormatter.currency = Currency.getInstance(it!!)
            setAmount()
        }


//        Setting the userName
        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                currency.value = it.userCurrency
                currencyFormatter.maximumFractionDigits = 1
                currencyFormatter.currency = Currency.getInstance(currency.value)
                binding.userName.text = it.username
                budget = it.userBudget
                var per = 0.toString()
                if(budget!=0){
                    per = ((it.userBudget / budget) * 100).toString()
                }
                binding.expensePercentage.text = getString(R.string.percentageBudget, per)
            } else {
                binding.expensePercentage.text = getString(R.string.percentageBudget, "0")
            }
            setAmount()
        }

//        set balance amount
        transactionViewModel.readDifferenceSum.observe(viewLifecycleOwner) {
            balanceAmount = it ?: 0f
            setAmount()
        }
//        set income and expense amount
        transactionViewModel.incomeSum.observe(viewLifecycleOwner) {
            when {
                it != null -> binding.incomeAmount.text = currencyFormatter.format(it)
                else -> binding.incomeAmount.text = currencyFormatter.format(0)
            }
        }

        transactionViewModel.expenseSum.observe(viewLifecycleOwner) {
            when {
                it != null -> binding.expenseAmount.text = currencyFormatter.format(it)
                else -> binding.expenseAmount.text = currencyFormatter.format(0)
            }
        }


//        observer for balance
        transactionViewModel.readMonthlySpends.observe(viewLifecycleOwner) {
            if (it != null) {
                monthlyAmount = it

            } else {
                monthlyAmount = 0f
            }
            setAmount()
        }

//         setting the pie chart
        setupPieChart()
//        observer for pie chart
        transactionViewModel.readMonthlySumByCategory.observe(viewLifecycleOwner) {
            binding.alertText.isVisible = it.isEmpty()
            binding.checkDetailedAnalysisBtn.isEnabled = it.isNotEmpty()
            binding.pieChart.isVisible = it.isNotEmpty()
            updateChart(it)
        }

//      analysis button
        binding.checkDetailedAnalysisBtn.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_MainScreen_to_detailedTransactionAnalysis)
        }

        return binding.root
    }

    private fun setAmount() {
        binding.monthlyExpense.text = currencyFormatter.format(monthlyAmount)
        binding.balanceAmount.text = currencyFormatter.format(balanceAmount)
        binding.userBudget.text = " / ${currencyFormatter.format(budget)}"
        var per = 0.toString()
        if(budget!=0){
             per = ((monthlyAmount / budget) * 100).toInt().toString()
        }
        binding.expensePercentage.text = getString(R.string.percentageBudget, per)
    }


    private fun setupPieChart() {
        binding.pieChart.minAngleForSlices = 40f
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.centerText = "Expenses"
        binding.pieChart.setCenterTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryTextColor
            )
        )
        binding.pieChart.setCenterTextSize(15f)
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.setEntryLabelTextSize(10f)
        binding.pieChart.isRotationEnabled = true
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
        binding.pieChart.setExtraOffsets(12f, 12f, 12f, 12f)
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
        data.setValueFormatter(PercentFormatter(binding.pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor))
        binding.pieChart.data = data
        binding.pieChart.invalidate()
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    override fun onResume() {
        super.onResume()
        val cal = Calendar.getInstance()
        transactionViewModel.setMonthYear(listOf((cal.get(Calendar.MONTH)+1),cal.get(Calendar.YEAR)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}