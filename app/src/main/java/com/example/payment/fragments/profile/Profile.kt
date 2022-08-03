package com.example.payment.fragments.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.payment.R
import com.example.payment.databinding.FragmentProfileBinding
import com.example.payment.userDb.User
import com.example.payment.userDb.UserViewModel

class Profile : Fragment() {
    private lateinit var viewModel: UserViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.userName.isEnabled = false
        binding.userBio.isEnabled = false
        binding.userBudget.isEnabled = false

//        Setting the predefault values
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel.userDetails.observe(viewLifecycleOwner) {
            if (it == null) {
                viewModel.insertUser(
                    User(
                        1,
                        getString(R.string.devName),
                        getString(R.string.devBio),
                        4000f
                        )
                )
            } else {
                binding.userName.setText(it.username)
                binding.userBudget.setText(it.userBudget.toString())
                binding.userBio.setText(it.userBio)
            }
        }

        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}