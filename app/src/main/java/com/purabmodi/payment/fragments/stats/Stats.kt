package com.purabmodi.payment.fragments.stats

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.purabmodi.payment.R
import com.purabmodi.payment.databinding.FragmentStatsBinding
import com.purabmodi.payment.fragments.stats.StatsDirections.actionStatsToAccountTransfer
import com.purabmodi.payment.fragments.stats.StatsDirections.actionStatsToAddTransaction
import com.purabmodi.payment.modals.CustomTimeData
import com.purabmodi.payment.modals.customData
import com.purabmodi.payment.rcAdapter.TransactionsAdapter
import com.purabmodi.payment.rcAdapter.filterAdapters.AccountFilterAdapter
import com.purabmodi.payment.rcAdapter.filterAdapters.CategoryFilterAdapter
import com.purabmodi.payment.rcAdapter.filterAdapters.MonthFilterAdapter
import com.purabmodi.payment.transactionDb.Transaction
import com.purabmodi.payment.userDb.UserViewModel
import com.purabmodi.payment.viewModel.AccountViewModel
import com.purabmodi.payment.viewModel.TransactionViewModel

class Stats : Fragment() {
    private var _binding: FragmentStatsBinding? = null

    private val accountsAdapter = AccountFilterAdapter(this)
    private val categoryAdapter = CategoryFilterAdapter(this)
    private val monthAdapter = MonthFilterAdapter(this)

    //    animation declaration
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }
    private var switchValue = false

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
        "General",
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

//        Setting the animation
        binding.toggleFAB.setOnClickListener {
            switchValue = !switchValue
            onToggleButtonClick()
        }

        binding.transferFAB.setOnClickListener {
            val action = actionStatsToAccountTransfer(
                Transaction(
                    -1,
                    "",
                    0f,
                    1,
                    0L,
                    "",
                    0,
                    0,
                    0,
                    0,
                    0f,
                    "",
                )
            )
            Navigation.findNavController(binding.root).navigate(action)
        }

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
            val action = actionStatsToAddTransaction(
                Transaction(
                    -1,
                    "",
                    0f,
                    1,
                    0L,
                    "",
                    0,
                    0,
                    0,
                    0,
                    0f,
                    "",
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

    private fun onToggleButtonClick() {
        setVisibility()
        setAnimation()
    }

    private fun setVisibility() {
        binding.addBtn.isVisible = switchValue
        binding.transferFAB.isVisible = switchValue
        binding.TransferTextview.isVisible = switchValue
        binding.addTransactionTextView.isVisible = switchValue
        binding.addBtn.isClickable = switchValue
        binding.transferFAB.isClickable = switchValue
    }

    private fun setAnimation() {
        if (switchValue) {
            binding.toggleFAB.startAnimation(rotateOpen)
            binding.addBtn.startAnimation(fromBottom)
            binding.transferFAB.startAnimation(fromBottom)
            binding.TransferTextview.startAnimation(fromBottom)
            binding.addTransactionTextView.startAnimation(fromBottom)
        } else {
            binding.toggleFAB.startAnimation(rotateClose)
            binding.addBtn.startAnimation(toBottom)
            binding.transferFAB.startAnimation(toBottom)
            binding.TransferTextview.startAnimation(toBottom)
            binding.addTransactionTextView.startAnimation(toBottom)
        }
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

        accountRC.adapter = accountsAdapter
        accountViewModel.readAllAccounts.observe(viewLifecycleOwner) {
            accountsAdapter.setData(it)
            it.forEach { account ->
                accountList.add(account.name)
            }
        }
//      setting month recycler view
        monthRC.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        monthRC.adapter = monthAdapter

//      setting category recycler view
        categoryRC.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
            reDeclareValues()
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
                        startDate.value!! - 86400000,
                        endDate.value!!
                    )
                )
                viewModel.readTransactionsByDuration.observe(viewLifecycleOwner) {
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
                viewModel.readTransactionsByMonth.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                reDeclareValues()
                bottomsheet.dismiss()
            }
        }
        bottomsheet.show()
    }

    private fun reDeclareValues() {
        accountsAdapter.setSelectedItem("")
        categoryAdapter.setSelectedItem("")
        monthAdapter.setSelectedItem(-1)
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
            "General",
            "Updated Balance",
            "Transportation",
            "Transfer"
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