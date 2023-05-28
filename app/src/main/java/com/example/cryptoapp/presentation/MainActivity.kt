package com.example.cryptoapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.OnHierarchyChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.di.MainActivitySubcomponent
import com.example.cryptoapp.presentation.CoinApplication.Companion.LOG_DEBUG_TAG
import com.example.cryptoapp.presentation.fragments.CoinPriceListFragment

class MainActivity : AppCompatActivity(), CoinPriceListFragment.DetailFragmentCallListener {

    lateinit var mainActivitySubcomponent: MainActivitySubcomponent
    private lateinit var binding: ActivityMainBinding
    private var orientation: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivitySubcomponent = (application as CoinApplication).applicationComponent
            .activitySubcomponent()
            .build()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) configureDetailFragment()

        if (savedInstanceState == null) supportFragmentManager.commit {
            add(binding.fragmentContainer.id, CoinPriceListFragment::class.java, null)
        }
    }

    override fun onResume() {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        super.onResume()
    }

    override fun showDetailFragment(detailFragment: Class<out Fragment>, arguments: Bundle?) {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(binding.fragmentContainer.id, detailFragment, arguments)
                    addToBackStack(null)
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                binding.detailFragmentContainer?.id?.let {
                    supportFragmentManager.popBackStack(
                        DISPLAY_INFO_TRANSACTION,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    supportFragmentManager.commit {
                        add(it, detailFragment, arguments)
                        addToBackStack(DISPLAY_INFO_TRANSACTION)
                    }
                    Log.d(LOG_DEBUG_TAG, "${supportFragmentManager.backStackEntryCount}")
                }
            }

            else -> throw RuntimeException("Unknown orientation: $orientation")
        }
    }

    private fun configureDetailFragment() {
        binding.detailFragmentContainer
            ?.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
                override fun onChildViewAdded(parent: View?, child: View?) {
                    prepareScreenTransition(TRANSITION_DIRECTION_FORWARD)
                    binding.fragmentScrollWrapper?.visibility = View.VISIBLE
                    Log.d(LOG_DEBUG_TAG, "child view added")
                }

                override fun onChildViewRemoved(parent: View?, child: View?) {
                    prepareScreenTransition(TRANSITION_DIRECTION_BACKWARD)
                    binding.fragmentScrollWrapper?.visibility = View.GONE
                    Log.d(LOG_DEBUG_TAG, "child view removed")
                }
            })
        binding.fragmentScrollWrapper?.visibility = View.GONE
    }

    private fun prepareScreenTransition(direction: Int) {
        when (direction) {
            TRANSITION_DIRECTION_FORWARD -> {
                TransitionManager.beginDelayedTransition(
                    binding.fragmentContainer as ViewGroup,
                    ChangeBounds()
                )
                TransitionManager.beginDelayedTransition(
                    binding.fragmentScrollWrapper as ViewGroup,
                    Fade().setDuration(700)
                )
            }

            TRANSITION_DIRECTION_BACKWARD -> {
                TransitionManager.beginDelayedTransition(
                    binding.fragmentContainer as ViewGroup,
                    ChangeBounds()
                )
            }
        }
    }

    companion object {
        private const val DISPLAY_INFO_TRANSACTION = "info"

        private const val TRANSITION_DIRECTION_FORWARD = 0
        private const val TRANSITION_DIRECTION_BACKWARD = 1
    }
}