package com.example.cryptoapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cryptoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CoinPriceListFragment.OnFragmentCallListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showFragment(fragmentToShow: Fragment) {
        when(val orientation = resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                binding.fragmentContainer?.id?.let {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(it, fragmentToShow)
                        .commit()
                }
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                binding.rightFragmentContainer?.id?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(it, fragmentToShow)
                        .commit()
                }
                binding.rightFragmentContainer?.visibility = View.VISIBLE
            }
            else -> throw RuntimeException("Unknown orientation: $orientation")
        }
    }
}