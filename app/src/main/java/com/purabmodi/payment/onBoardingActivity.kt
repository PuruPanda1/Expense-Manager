package com.purabmodi.payment

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.purabmodi.payment.databinding.ActivityOnBoardingBinding
import com.purabmodi.payment.databinding.OnboardingScreenLayoutBinding

class onBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.onBoardingBackground)

        initUI()
    }

    private fun initUI() {
        binding.viewPager2.adapter = AppIntroViewPager2Adapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
        }.attach()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.nextBtn.text = getString(R.string.getStarted)
                } else {
                    binding.nextBtn.text = getString(R.string.next)
                }
            }
        })

        binding.skipBtn.setOnClickListener {
            startActivity(Intent(this, DropPage::class.java))
            finish()
        }

        binding.nextBtn.setOnClickListener {
            if (binding.nextBtn.text.toString() == getString(R.string.getStarted)) {
                startActivity(Intent(this, DropPage::class.java))
                finish()
            } else {
                // to change current page - on click "Next BUTTON"
                val current = (binding.viewPager2.currentItem) + 1
                binding.viewPager2.currentItem = current

                // to update button text
                if (current >= 2) {
                    binding.nextBtn.text = getString(R.string.getStarted)//"Get Started"
                    binding.nextBtn.contentDescription =
                        getString(R.string.getStarted)//"Get Started"

                } else {
                    binding.nextBtn.text = getString(R.string.next)//"Next"
                    binding.nextBtn.contentDescription = getString(R.string.next)//"Next"
                }
            }
        }

    }

}
class AppIntroViewPager2Adapter : RecyclerView.Adapter<PagerVH2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH2 {
        return PagerVH2(
            OnboardingScreenLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //get the size of color array
    override fun getItemCount(): Int = 3 // Int.MAX_VALUE

    //binding the screen with view
    override fun onBindViewHolder(holder: PagerVH2, position: Int) = holder.itemView.run {

        with(holder) {
            if (position == 0) {
                bindingDesign.introTitle.text = context.getString(R.string.introTitle1)
                bindingDesign.introDesc.text = context.getString(R.string.introDesc1)
                bindingDesign.introAnim.setAnimation(R.raw.onboardinganim1)
            }
            if (position == 1) {
                bindingDesign.introTitle.text = context.getString(R.string.introTitle2)
                bindingDesign.introDesc.text = context.getString(R.string.introDesc2)
                bindingDesign.introAnim.setAnimation(R.raw.piggy)
            }
            if (position == 2) {
                bindingDesign.introTitle.text = context.getString(R.string.introTitle3)
                bindingDesign.introDesc.text = context.getString(R.string.introDesc3)
                bindingDesign.introAnim.setAnimation(R.raw.robot)
            }
        }
    }
}
class PagerVH2(val bindingDesign: OnboardingScreenLayoutBinding) : RecyclerView.ViewHolder(bindingDesign.root)