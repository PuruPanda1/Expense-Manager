package com.purabmodi.payment.fragments.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.purabmodi.payment.R
import com.purabmodi.payment.accountsDb.Accounts
import com.purabmodi.payment.databinding.FragmentWalletBinding
import com.purabmodi.payment.rcAdapter.WalletAccountDetailsAdapter
import com.purabmodi.payment.transactionDb.Transaction
import com.purabmodi.payment.userDb.UserViewModel
import com.purabmodi.payment.viewModel.AccountViewModel
import com.purabmodi.payment.viewModel.TransactionViewModel
import java.util.*

class Wallet : Fragment() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private var currency: MutableLiveData<String> = MutableLiveData("INR")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(layoutInflater, container, false)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        transactionViewModel =
            ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        var adapter = WalletAccountDetailsAdapter(currency.value!!, this)

        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                currency.value = it.userCurrency
            }
            adapter = WalletAccountDetailsAdapter(currency.value!!, this)
            binding.walletAccountsRecyclerView.adapter = adapter
            binding.walletAccountsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        }

        transactionViewModel.readAccountDetails.observe(viewLifecycleOwner) {
            binding.alertText.isVisible = it.isEmpty()
            adapter.submitList(it)
        }

        return binding.root
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

    fun deleteBankAccount(accountName: String) {
        transactionViewModel.deleteAccountTransaction(accountName)
        accountViewModel.deleteAccountWithName(accountName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
