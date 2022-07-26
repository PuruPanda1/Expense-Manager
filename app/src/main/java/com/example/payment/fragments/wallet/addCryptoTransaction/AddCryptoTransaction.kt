package com.example.payment.fragments.wallet.addCryptoTransaction

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.payment.R
import com.example.payment.cryptoDb.CryptoTransaction
import com.example.payment.cryptoDb.CryptoViewModel
import com.example.payment.databinding.FragmentAddCryptoTransactionBinding

class addCryptoTransaction : Fragment() {
    private var _binding: FragmentAddCryptoTransactionBinding? = null
    private var date: Long = 0
    private lateinit var viewModel: CryptoViewModel
    private val args by navArgs<addCryptoTransactionArgs>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCryptoTransactionBinding.inflate(layoutInflater, container, false)

//        back button working
        binding.backToCryptoList.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_addCryptoTransaction_to_cryptoListFragment)
        }

//        calling viewmodel and setting it up
        viewModel = ViewModelProvider(requireActivity()!!)[CryptoViewModel::class.java]

//        setting datepickeicon onClickListener
        binding.datePickerIcon.setOnClickListener {
            datePicker()
        }


//        checking whether update or insert
        if (args.isUpdate) {
            binding.buyingPrice.setText(args.buyingPrice.toString())
            binding.numberOfCoins.setText(args.numberOfCoins.toString())
            date = args.date
            binding.addCryptoTransactionBtn.text = "Update Transaction"
        }

        binding.cryptoName.text = args.cryptoName
        val moneyString =
            String.format(resources.getString(R.string.amountInRupee, args.cryptoPrice.toString()))
        binding.cryptoPrice.text = moneyString

        binding.addCryptoTransactionBtn.setOnClickListener {
            if (args.isUpdate) {
                updateCryptoTransaction()
            } else {
                insertCryptoTransaction()
            }
        }
        return binding.root
    }

    private fun updateCryptoTransaction() {
        val id = args.cryptoId
        val numberOfCoins = binding.numberOfCoins.text.toString()
        val buyingPrice = binding.buyingPrice.text.toString()
        val name = args.cryptoName
        if (check(id, numberOfCoins, buyingPrice, name, date)) {
            viewModel.updateCryptoTransaction(
                CryptoTransaction(
                    args.sid,
                    id,
                    name,
                    numberOfCoins.toDouble(),
                    buyingPrice.toDouble(),
                    date
                )
            )
            Toast.makeText(requireContext(), "Transaction Updated", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).navigate(R.id.action_addCryptoTransaction_to_cryptoListFragment)
        } else {
            if (date == 0L) {
                Toast.makeText(
                    requireContext(),
                    "Click on Calender Icon to choose the date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun insertCryptoTransaction() {
        val id = args.cryptoId
        val numberOfCoins = binding.numberOfCoins.text.toString()
        val buyingPrice = binding.buyingPrice.text.toString()
        val name = args.cryptoName
        if (check(id, numberOfCoins, buyingPrice, name, date)) {
            viewModel.insertCryptoTransaction(
                CryptoTransaction(
                    0,
                    id,
                    name,
                    numberOfCoins.toDouble(),
                    buyingPrice.toDouble(),
                    date
                )
            )
            Toast.makeText(requireContext(), "Transaction Added", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).navigate(R.id.action_addCryptoTransaction_to_cryptoListFragment)
        } else {
            if (date == 0L) {
                Toast.makeText(
                    requireContext(),
                    "Click on Calender Icon to choose the date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun check(
        id: String,
        numberOfCoins: String,
        buyingPrice: String,
        name: String,
        date: Long
    ): Boolean {
        if (id.isBlank() || numberOfCoins.isBlank() || buyingPrice.isBlank() || name.isBlank() || date == 0L) {
            return false
        }
        return true
    }

    private fun datePicker() {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datepickerdialog: DatePickerDialog = DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                cal.set(year, monthOfYear, dayOfMonth)
                date = cal.timeInMillis
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
}