package com.example.payment.fragments.stats

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.payment.R
import com.example.payment.databinding.FragmentStatsBinding
import com.example.payment.modals.CustomTimeData
import com.example.payment.modals.customData
import com.example.payment.rcAdapter.TransactionsAdapter
import com.example.payment.rcAdapter.filterAdapters.AccountFilterAdapter
import com.example.payment.rcAdapter.filterAdapters.CategoryFilterAdapter
import com.example.payment.rcAdapter.filterAdapters.MonthFilterAdapter
import com.example.payment.transactionDb.Transaction
import com.example.payment.userDb.UserViewModel
import com.example.payment.viewModel.AccountViewModel
import com.example.payment.viewModel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class Stats : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private lateinit var accountViewModel: AccountViewModel
    private val cal = Calendar.getInstance()
    private val y = cal.get(Calendar.YEAR)
    private val m = cal.get(Calendar.MONTH)
    private val d = cal.get(Calendar.DAY_OF_MONTH)
    private var startDate = MutableLiveData(0L)
    private var endDate = MutableLiveData(0L)
    private var monthSelected = MutableLiveData(false)
    private var accountSelected = MutableLiveData(false)
    private var categorySelected = MutableLiveData(false)
    private var month = -1
    private var accountName = ""
    private var category = ""
    private var accountList: MutableList<String> = mutableListOf()
    private var monthList: MutableList<Int> = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    private var categoryList = mutableListOf(
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
        "Suspense",
        "Updated Balance",
        "Transportation"
    )
    private lateinit var viewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private var currency = MutableLiveData("INR")
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

//        setting the viewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        lateinit var adapter: TransactionsAdapter

//        live data for currency
        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                currency.value = it.userCurrency
            }
            adapter = TransactionsAdapter(this, currency.value!!)
            binding.transactionsRC.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionsRC.adapter = adapter
        }

//          add new transaction onclick listener
        binding.addBtn.setOnClickListener {
            val action = StatsDirections.actionStatsToAddTransaction(
                Transaction(
                    -1,
                    "",
                    0f,
                    true,
                    0L,
                    "",
                    0,
                    0,
                    0,
                    0f,
                    ""
                )
            )
            Navigation.findNavController(binding.root).navigate(action)
        }

//        setting the observer
        viewModel.readAllTransaction.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.transactionsRC.post { binding.transactionsRC.smoothScrollToPosition(0) }
            }
        }

        binding.filterIcon.setOnClickListener {
            showBottomSheetFilter(requireContext(), adapter)
        }

        return binding.root
    }

    private fun showBottomSheetFilter(context: Context, adapter: TransactionsAdapter) {
        val view = LayoutInflater.from(context).inflate(R.layout.filter_bottom_sheet, null)
        val bottomsheet = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val simpleDateFormat = SimpleDateFormat("dd MMM YY")
        bottomsheet.setContentView(view)

        val monthRC = view.findViewById<RecyclerView>(R.id.monthsRC)
        val accountRC = view.findViewById<RecyclerView>(R.id.accountsRC)
        val categoryRC = view.findViewById<RecyclerView>(R.id.transactionTypeRcFilter)
        val startDateView = view.findViewById<TextView>(R.id.startingDateTextView)
        val endDateView = view.findViewById<TextView>(R.id.endDateTextView)
        val showAllBtn = view.findViewById<Button>(R.id.showAllBtn)
        val applyBtn = view.findViewById<Button>(R.id.applyBtn)
        val noAccount = view.findViewById<ImageView>(R.id.noAccount)
        val noCategory = view.findViewById<ImageView>(R.id.noCategory)

//      setting accounts recycler view
        accountRC.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val accountsAdapter = AccountFilterAdapter(this)
        accountRC.adapter = accountsAdapter
        accountViewModel.readAllAccounts.observe(viewLifecycleOwner) {
            accountsAdapter.setData(it)
            it.forEach { account ->
                accountList.add(account.name)
            }
        }
//      setting month recycler view
        monthRC.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val monthAdapter = MonthFilterAdapter(this)
        monthRC.adapter = monthAdapter

//      setting category recycler view
        categoryRC.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val categoryAdapter = CategoryFilterAdapter(this)
        categoryRC.adapter = categoryAdapter

//        updating start and end date text view when month is selected
        monthSelected.observe(viewLifecycleOwner) {
            if (it) {
                startDate.value = 0L
                endDate.value = 0L
            }
        }
//        cross button hide option
        accountSelected.observe(viewLifecycleOwner) {
            noAccount.isVisible = it
        }

        categorySelected.observe(viewLifecycleOwner) {
            noCategory.isVisible = it
        }

        noCategory.setOnClickListener {
            categorySelected.value = false
            categoryAdapter.setSelectedItem("")
        }

        noAccount.setOnClickListener {
            accountSelected.value = false
            accountsAdapter.setSelectedItem("")
        }

        startDate.observe(viewLifecycleOwner) {
            if (it != 0L) {
                startDateView.text = simpleDateFormat.format(it)
            } else {
                startDateView.text = "Starting Date"
            }
        }

        endDate.observe(viewLifecycleOwner) {
            if (it != 0L) {
                endDateView.text = simpleDateFormat.format(it)
            } else {
                endDateView.text = "Ending Date"
            }
        }

        startDateView.setOnClickListener {
            monthAdapter.setSelectedItem(-1)
            datePicker(true)
        }

        endDateView.setOnClickListener {
            datePicker(false)
        }

        showAllBtn.setOnClickListener {
            viewModel.readAllTransaction.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.submitList(it)
                }
            }

            bottomsheet.dismiss()
        }

        applyBtn.setOnClickListener {
//            conditions
            if (monthSelected.value!!) {
                monthChange()
            }
            if (categorySelected.value!!) {
                categoryChange()
            }
            if (accountSelected.value!!) {
                accountChange()
            }

            if (startDate.value != 0L && endDate.value != 0L) {
                viewModel.setCustomTimeData(
                    CustomTimeData(
                        categoryList,
                        accountList,
                        startDate.value!!-820000,
                        endDate.value!!
                    )
                )
                viewModel.readCustomDefinedTimeData.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                reDeclareValues()
                bottomsheet.dismiss()
            } else if (startDate.value != 0L && endDate.value == 0L) {
                Toast.makeText(context, "Select End Date", Toast.LENGTH_SHORT).show()
            } else if (startDate.value == 0L && endDate.value != 0L) {
                Toast.makeText(context, "Select Start Date", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.setCustomData(customData(categoryList, accountList, monthList))
                viewModel.readCustomDefinedData.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                reDeclareValues()
                bottomsheet.dismiss()
            }
        }
        bottomsheet.show()
    }

    private fun reDeclareValues() {
        startDate.value = 0L
        endDate.value = 0L
        monthSelected.value = false
        accountSelected.value = false
        categorySelected.value = false
        monthList = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        categoryList = mutableListOf(
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
            "Suspense",
            "Updated Balance",
            "Transportation"
        )
        accountViewModel.readAllAccounts.observe(viewLifecycleOwner) {
            it.forEach { account ->
                accountList.add(account.name)
            }
        }
    }

    private fun categoryChange() {
        categoryList.clear()
        categoryList.add(category)
    }

    private fun accountChange() {
        accountList.clear()
        accountList.add(accountName)
    }

    private fun monthChange() {
        monthList.clear()
        monthList.add(1 + month)
    }

    //    true for startDate false for endDate
    private fun datePicker(option: Boolean) {
        val datepickerdialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                cal.set(year, monthOfYear, dayOfMonth)
                if (option) {
                    startDate.value = cal.timeInMillis
                } else {
                    endDate.value = cal.timeInMillis
                }
            },
            y,
            m,
            d
        ).show()
    }


    fun deleteTransaction(transaction: Transaction) {
        viewModel.deleteTransaction(transaction)
        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setModeOfPayment(accountName: String) {
        accountSelected.value = true
        this.accountName = accountName
    }

    fun setMonthFilter(month: Int) {
        monthSelected.value = true
        this.month = month
    }

    fun setCategoryFilter(category: String) {
        categorySelected.value = true
        this.category = category
    }
}