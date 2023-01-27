package com.system.payment.fragments.stats.accountTransfer

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.system.payment.R
import com.system.payment.accountsDb.Accounts
import com.system.payment.databinding.FragmentAccountTransferBinding
import com.system.payment.rcAdapter.acTransferAdapter.FromBankAdapter
import com.system.payment.rcAdapter.acTransferAdapter.ToBankAdapter
import com.system.payment.transactionDb.Transaction
import com.system.payment.userDb.UserViewModel
import com.system.payment.viewModel.AccountViewModel
import com.system.payment.viewModel.TransactionViewModel
import java.util.*

class AccountTransfer : Fragment() {
    private var _binding: FragmentAccountTransferBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AccountTransferArgs>()
    private lateinit var modeOfPayment: String
    private var day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var week: Int = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1
    private var month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var year: Int = Calendar.getInstance().get(Calendar.YEAR)
    private val cal: Calendar = Calendar.getInstance()
    private val y = cal.get(Calendar.YEAR)
    private val m = cal.get(Calendar.MONTH)
    private val d = cal.get(Calendar.DAY_OF_MONTH)
    private var date: MutableLiveData<Long> = MutableLiveData(cal.timeInMillis)
    private var currency = MutableLiveData("INR")
    private var fromBankAccount = ""
    private var toBankAccount = ""
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountTransferBinding.inflate(layoutInflater, container, false)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        transactionViewModel =
            ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        date.observe(viewLifecycleOwner) {
            val simpleDateFormat = SimpleDateFormat("dd/MM/YY")
            val dateString = simpleDateFormat.format(it)
            binding.transactionDate.text = getString(R.string.transaction_date, dateString)
        }

        binding.calenderPicker.setOnClickListener {
            datePicker()
        }

        currency.observe(viewLifecycleOwner) {
            binding.currencyDisplay.text = Currency.getInstance(currency.value).symbol
        }

        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                currency.value = it.userCurrency
            }
        }

        val fromBankAdapter = FromBankAdapter(this)
        binding.fromAccountRC.adapter = fromBankAdapter
        binding.fromAccountRC.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        accountViewModel.readAllAccounts.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                fromBankAdapter.setData(it)
                modeOfPayment = if (args.transaction == null) {
                    fromBankAccount = it[0].name

                    fromBankAdapter.setSelectedItem(it[0].name)
                    it[0].name
                } else {
                    fromBankAdapter.setSelectedItem(args.transaction!!.modeOfPayment)
                    args.transaction!!.modeOfPayment
                }
            } else {
                accountViewModel.insertAccount(Accounts(0, "CASH"))
                accountViewModel.insertAccount(Accounts(0, "BANK"))
            }
        }

        val toBankAdapter = ToBankAdapter(this)
        binding.toAccountRC.adapter = toBankAdapter
        binding.toAccountRC.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        accountViewModel.readAllAccounts.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                toBankAdapter.setData(it)
                modeOfPayment = if (args.transaction == null) {
                    toBankAccount = it[0].name
                    toBankAdapter.setSelectedItem(it[0].name)
                    it[0].name
                } else {
                    toBankAdapter.setSelectedItem(args.transaction!!.modeOfPayment)
                    args.transaction!!.modeOfPayment
                }
            } else {
                accountViewModel.insertAccount(Accounts(0, "CASH"))
                accountViewModel.insertAccount(Accounts(0, "BANK"))
            }
        }

        binding.saveTransactionBtn.setOnClickListener {
            insertTransaction()
        }

        return binding.root
    }

    private fun insertTransaction() {
        val amt = binding.transactionAmount.text.toString()
        var description = binding.transactionDescription.text.toString()
        if (amt.isBlank()) {
            Toast.makeText(requireContext(), "Enter Amount", Toast.LENGTH_SHORT).show()
        } else {
            if (fromBankAccount == toBankAccount) {
                Toast.makeText(requireContext(), "Error: Same Bank Account!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (description.isEmpty()) {
                    description = "$fromBankAccount to $toBankAccount"
                }
//                expense Transaction
                transactionViewModel.insertTransaction(
                    Transaction(
                        0,
                        description,
                        0f,
                        1,
                        date.value!!,
                        "Transfer",
                        day,
                        week,
                        month,
                        year,
                        amt.toFloat(),
                        fromBankAccount
                    )
                )
                //                income Transaction
                transactionViewModel.insertTransaction(
                    Transaction(
                        0,
                        description,
                        amt.toFloat(),
                        0,
                        date.value!!,
                        "Transfer",
                        day,
                        week,
                        month,
                        year,
                        0f,
                        toBankAccount
                    )
                )
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_accountTransfer_to_Stats)
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun datePicker() {
        val datepickerdialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                cal.set(year, monthOfYear, dayOfMonth)
                date.value = cal.timeInMillis
                day = d
                week = cal.get(Calendar.WEEK_OF_YEAR) - 1
                month = cal.get(Calendar.MONTH) + 1
                this.year = cal.get(Calendar.YEAR)
            },
            y,
            m,
            d
        )
        datepickerdialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setFromBankAccount(name: String) {
        fromBankAccount = name
    }

    fun setToBankAccount(name: String) {
        toBankAccount = name
    }
}