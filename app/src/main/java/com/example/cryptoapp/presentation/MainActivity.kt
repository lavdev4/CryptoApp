package com.example.cryptoapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.Explode
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.OnHierarchyChangeListener
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
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

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configureDetailFragmentContainer()
        }

        if (savedInstanceState == null) supportFragmentManager.commit {
            add(binding.fragmentContainer.id, CoinPriceListFragment::class.java, null)
        }
    }

    override fun onPause() {
        if (isChangingConfigurations) {
            supportFragmentManager.popBackStackImmediate(
                DISPLAY_INFO_TRANSACTION,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
        super.onPause()
    }

    override fun showDetailFragment(
        detailFragment: Class<out Fragment>,
        arguments: Bundle?,
        transitionImage: View
    ) {
        supportFragmentManager
            .popBackStack(DISPLAY_INFO_TRANSACTION, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                detailFragment.newInstance().also {
                    it.arguments = arguments
                    setTransitions(it)
                    setSharedTransitions(it)
                }.let { fragment ->
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        addSharedElement(transitionImage, transitionImage.transitionName)
                        replace(binding.fragmentContainer.id, fragment)
                        addToBackStack(DISPLAY_INFO_TRANSACTION)
                    }
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                binding.detailFragmentContainer?.id?.let { containerId ->
                    supportFragmentManager.commit {
                        add(containerId, detailFragment, arguments)
                        addToBackStack(DISPLAY_INFO_TRANSACTION)
                    }
                }
            }

            else -> throw RuntimeException("Unknown orientation: $orientation")
        }
        Log.d(
            LOG_DEBUG_TAG,
            "FM BackStack entry count: ${supportFragmentManager.backStackEntryCount}"
        )
    }

    private fun configureDetailFragmentContainer() {
        binding.detailFragmentContainer
            ?.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
                override fun onChildViewAdded(parent: View?, child: View?) {
                    prepareLandScreenTransition(TRANSITION_DIRECTION_FORWARD)
                    binding.detailFragmentContainerWrapper?.visibility = View.VISIBLE
                    Log.d(LOG_DEBUG_TAG, "child view added")
                }

                override fun onChildViewRemoved(parent: View?, child: View?) {
                    prepareLandScreenTransition(TRANSITION_DIRECTION_BACKWARD)
                    binding.detailFragmentContainerWrapper?.visibility = View.GONE
                    Log.d(LOG_DEBUG_TAG, "child view removed")
                }
            })
        // the delay is needed for the Fade() transaction animation to work
        // when it starts for the first time
        Handler(mainLooper).postDelayed({
            binding.detailFragmentContainerWrapper?.visibility = View.GONE
        }, 5)
    }

    private fun prepareLandScreenTransition(direction: Int) {
        when (direction) {
            TRANSITION_DIRECTION_FORWARD -> {
                TransitionManager.beginDelayedTransition(
                    binding.fragmentContainer as ViewGroup,
                    ChangeBounds().apply {
                        duration = LAND_TRANSITIONS_DURATION_BOUNDS
                        interpolator = DecelerateInterpolator(LAND_TRANSITION_INTERPOLATOR_BOUNDS)
                    }
                )
                TransitionManager.beginDelayedTransition(
                    binding.detailFragmentContainerWrapper as ViewGroup,
                    Fade().apply {
                        duration = LAND_TRANSITIONS_DURATION_FADE
                        interpolator = AccelerateInterpolator(LAND_TRANSITION_INTERPOLATOR_FADE)
                    }
                )
            }

            TRANSITION_DIRECTION_BACKWARD -> {
                TransitionManager.beginDelayedTransition(
                    binding.fragmentContainer as ViewGroup,
                    ChangeBounds().apply {
                        duration = LAND_TRANSITIONS_DURATION_BOUNDS
                        interpolator = DecelerateInterpolator(LAND_TRANSITION_INTERPOLATOR_BOUNDS)
                    }
                )
            }
        }
    }

    private fun setTransitions(fragment: Fragment): Fragment {
        return fragment.apply {
            enterTransition = Explode().apply {
                duration = PORT_ENTER_TRANSITION_DURATION
                interpolator = DecelerateInterpolator(PORT_TRANSITION_INTERPOLATOR)
            }
            returnTransition = Fade().apply {
                duration = PORT_RETURN_TRANSITION_DURATION
                interpolator = DecelerateInterpolator(PORT_TRANSITION_INTERPOLATOR)
            }
        }
    }

    private fun setSharedTransitions(fragment: Fragment): Fragment {
        return TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
            addTransition(ChangeImageTransform())
            duration = PORT_ENTER_TRANSITION_DURATION
            interpolator = DecelerateInterpolator(PORT_TRANSITION_INTERPOLATOR)
        }.let {
            fragment.apply {
                sharedElementEnterTransition = it
            }
        }
    }

    companion object {
        private const val DISPLAY_INFO_TRANSACTION = "info"
        private const val PORT_ENTER_TRANSITION_DURATION = 500L
        private const val PORT_RETURN_TRANSITION_DURATION = 250L
        private const val PORT_TRANSITION_INTERPOLATOR = 2.0f
        private const val LAND_TRANSITIONS_DURATION_BOUNDS = 450L
        private const val LAND_TRANSITIONS_DURATION_FADE = 700L
        private const val LAND_TRANSITION_INTERPOLATOR_BOUNDS = 1.5f
        private const val LAND_TRANSITION_INTERPOLATOR_FADE = 1.5f

        private const val TRANSITION_DIRECTION_FORWARD = 0
        private const val TRANSITION_DIRECTION_BACKWARD = 1
    }
}