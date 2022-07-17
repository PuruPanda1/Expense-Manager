package com.example.payment.fragments.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.payment.R
import com.example.payment.databinding.FragmentMainScreenBinding

class MainScreen : Fragment() {
    private var _binding:FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        val s = String.format(getString(R.string.monthlyExpense,(2000.29).toString(),(3000).toString()))
        binding.monthlyExpense.setText(s)

        binding.addBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_MainScreen_to_addTransaction)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}