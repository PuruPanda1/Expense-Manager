package com.system.payment.fragments.bottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.system.payment.R
import com.system.payment.accountsDb.Accounts
import com.system.payment.databinding.FragmentAddNewBottomSheetDialogBinding
import com.system.payment.transactionDb.Transaction
import com.system.payment.viewModel.AccountViewModel
import com.system.payment.viewModel.TransactionViewModel
import java.util.*


class AddNewBottomSheetDialog(
    private val openAddTransaction: () -> Unit,
    private val openBankTransfer: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAddNewBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewBottomSheetDialogBinding.inflate(inflater, container, false)
        transactionViewModel =
            ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        initUI()

        return binding.root
    }

    private fun initUI() {
        binding.addTransactionLayout.setOnClickListener {
            openAddTransaction()
            dismiss()
        }

        binding.TransferLayout.setOnClickListener {
            openBankTransfer()
            dismiss()
        }

        binding.addBankLayout.setOnClickListener {
            showBottomSheetLayout(binding.root.context)
            dismiss()
        }

    }

    private fun showBottomSheetLayout(context: Context) {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        bottomSheet.setContentView(view)

        val accountName = bottomSheet.findViewById<EditText>(R.id.accountNameTextView)
        val accountBalance = bottomSheet.findViewById<EditText>(R.id.balanceAmountTextView)
        val submitButton = bottomSheet.findViewById<Button>(R.id.addUpdateButton)


        if (submitButton != null && accountName != null && accountBalance != null) {
            submitButton.setOnClickListener {
                val aName = accountName.text.toString()
                val aBalance = accountBalance.text.toString()
                if (aBalance.isBlank() || aName.isBlank()) {
                    Toast.makeText(context, "Fields can not be empty", Toast.LENGTH_SHORT).show()
                } else {
                    val cal = Calendar.getInstance()
                    val y = cal.get(android.icu.util.Calendar.YEAR)
                    val m = cal.get(android.icu.util.Calendar.MONTH)
                    val d = cal.get(android.icu.util.Calendar.DAY_OF_MONTH)
                    cal.set(y, m, d)
                    val date = cal.timeInMillis
                    accountViewModel.insertAccount(Accounts(0, aName))
                    transactionViewModel.insertTransaction(
                        Transaction(
                            0,
                            "Updated Balance",
                            aBalance.toFloat(),
                            0,
                            date,
                            "Updated Balance",
                            d,
                            (cal.get(Calendar.WEEK_OF_YEAR) - 1),
                            (m + 1),
                            y,
                            0f,
                            aName,
                        )
                    )
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                    bottomSheet.dismiss()
                }
            }
        }


//        close button onclick listener
        bottomSheet.findViewById<Button>(R.id.closeButton)?.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}