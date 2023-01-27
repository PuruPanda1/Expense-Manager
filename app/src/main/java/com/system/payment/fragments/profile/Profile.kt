package com.system.payment.fragments.profile

import android.app.Activity
import android.content.Intent
import android.icu.util.Currency
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.system.payment.R
import com.system.payment.databinding.FragmentProfileBinding
import com.system.payment.userDb.User
import com.system.payment.userDb.UserViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mynameismidori.currencypicker.CurrencyPicker

class Profile : Fragment() {
    private lateinit var viewModel: UserViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var currencyCode: String = "INR"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        var isEditable = false
        var userPhoto: Uri = Uri.parse("android.resource://com.purabmodi.payment/drawable/dev_photo")

//        imagePicker calling activity result
        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    userPhoto = fileUri
                    Glide.with(requireContext()).load(userPhoto).into(binding.userPhoto)
                } else {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

//        setting the imeoptions -> done for bio
        binding.userBio.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.userName.isEnabled = false
        binding.userBio.isEnabled = false
        binding.userBudget.isEnabled = false
        binding.userCurrency.isEnabled = false

//        setting the user image
        binding.changeProfilePhoto.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .crop()
                .maxResultSize(
                    620,
                    620
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

//        Setting the predefault values
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel.userDetails.observe(viewLifecycleOwner) {
            if (it == null) {
                viewModel.insertUser(
                    User(
                        1,
                        getString(R.string.devName),
                        getString(R.string.devBio),
                        4000,
                        userPhoto.toString(),
                        "INR"
                    )
                )
            } else {
                binding.userName.setText(it.username)
                binding.userBudget.setText(it.userBudget.toString())
                binding.userBio.setText(it.userBio)
                val currencyName = Currency.getInstance(it.userCurrency).displayName
                val currencySymbol = Currency.getInstance(it.userCurrency).symbol
                binding.userCurrency.text = "Currency: $currencyName ( $currencySymbol )"
                currencyCode = it.userCurrency
                userPhoto = Uri.parse(it.userPhoto)
                Glide.with(requireContext())
                    .load(it.userPhoto)
                    .into(binding.userPhoto)
            }
        }

        binding.editButton.setOnClickListener {
            if (!isEditable) {
                isEditable = !isEditable
                binding.editButton.text = "Save Changes"
                binding.userBio.setRawInputType(InputType.TYPE_CLASS_TEXT)
                binding.userName.isEnabled = true
                binding.userBio.isEnabled = true
                binding.userBudget.isEnabled = true
                binding.userCurrency.isEnabled = true
                binding.changeProfilePhoto.isVisible = true
            } else {
                isEditable = !isEditable
                binding.editButton.text = "edit"
                binding.userName.isEnabled = false
                binding.userCurrency.isEnabled = false
                binding.userBio.isEnabled = false
                binding.changeProfilePhoto.isVisible = false
                binding.userBudget.isEnabled = false
                val username = binding.userName.text.toString()
                val userBio = binding.userBio.text.toString()
                val userBudget = binding.userBudget.text.toString().toInt()
                viewModel.updateUser(
                    User(
                        1,
                        username,
                        userBio,
                        userBudget,
                        userPhoto.toString(),
                        currencyCode
                    )
                )
            }
        }

        binding.userCurrency.setOnClickListener {
            val picker = CurrencyPicker.newInstance("Select Currency")  // dialog title
            picker.setListener{ _, code, _, _ ->
                currencyCode = code
                val currencyName = Currency.getInstance(currencyCode).displayName
                val currencySymbol = Currency.getInstance(currencyCode).symbol
                binding.userCurrency.text = "Currency: $currencyName ( $currencySymbol )"
                picker.dismiss()
            }
            picker.show(requireActivity().supportFragmentManager, "CURRENCY_PICKER")
        }

        binding.dailyViewText.setOnClickListener {
            val action = ProfileDirections.actionProfileToDetailedView(currencyCode)
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.instagramIcon.setOnClickListener {
            socialMediaLinks(1)
        }

        binding.twitterIcon.setOnClickListener {
            socialMediaLinks(2)
        }

        binding.githubIcon.setOnClickListener {
            socialMediaLinks(3)
        }

        binding.linkedInIcon.setOnClickListener {
            socialMediaLinks(4)
        }

        binding.instagramIconText.setOnClickListener {
            socialMediaLinks(1)
        }

        binding.twitterIconText.setOnClickListener {
            socialMediaLinks(2)
        }

        binding.githubIconText.setOnClickListener {
            socialMediaLinks(3)
        }

        binding.linkedInIconText.setOnClickListener {
            socialMediaLinks(4)
        }

        return binding.root
    }

    private fun socialMediaLinks(option: Int) {
        val uri: Uri = when (option) {
            1 -> Uri.parse("https://www.instagram.com/purab_here/")
            2 -> Uri.parse("https://twitter.com/Purab__here")
            3 -> Uri.parse("https://github.com/PuruPanda1")
            4 -> Uri.parse("https://www.linkedin.com/in/purab-modi-4b1081209")
            else -> Uri.parse("hey")
        }
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}