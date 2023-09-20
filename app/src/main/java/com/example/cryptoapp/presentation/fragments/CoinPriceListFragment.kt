package com.example.cryptoapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.databinding.FragmentCoinPriceListBinding
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import com.example.cryptoapp.presentation.CoinApplication.Companion.LOG_DEBUG_TAG
import com.example.cryptoapp.presentation.MainActivity
import com.example.cryptoapp.presentation.adapters.PriceListAdapter
import com.example.cryptoapp.presentation.viewmodels.AppViewModelFactory
import com.example.cryptoapp.presentation.viewmodels.CoinPriceListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinPriceListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<CoinPriceListViewModel> { viewModelFactory }
    private lateinit var detailFragmentCallListener: DetailFragmentCallListener
    private var _binding: FragmentCoinPriceListBinding? = null
    private val binding: FragmentCoinPriceListBinding
        get() = _binding ?: throw RuntimeException("FragmentCoinPriceListBinding is null")

    override fun onAttach(context: Context) {
        Log.d(LOG_DEBUG_TAG, "CoinPriceListFragment onAttach")
        super.onAttach(context)
        (requireActivity() as MainActivity).mainActivitySubcomponent.inject(this)

        if (requireActivity() is DetailFragmentCallListener) {
            detailFragmentCallListener = requireActivity() as DetailFragmentCallListener
        } else throw RuntimeException("Parent activity doesn't implement OnFragmentCallListener interface.")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(LOG_DEBUG_TAG, "CoinPriceListFragment onCreateView")
        _binding = FragmentCoinPriceListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        val adapter = PriceListAdapter(requireContext())
        adapter.onItemClickListener = setupItemClickListener()
        binding.rvCoinPriceList.adapter = adapter

        lifecycleScope.launch {
            viewModel.priceList.onStart {
                (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
            }.collectLatest {
                adapter.submitData(it)
                Log.d(LOG_DEBUG_TAG, "Adapter update")
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupItemClickListener(): PriceListAdapter.OnItemClickListener {
        return object : PriceListAdapter.OnItemClickListener {
            override fun onItemClick(coinInfo: CoinInfoEntity, itemImage: View) {
                arguments = CoinDetailFragment.createArguments(coinInfo.fromSymbol)
                detailFragmentCallListener.showDetailFragment(
                    CoinDetailFragment::class.java,
                    arguments,
                    itemImage
                )
            }
        }
    }

    interface DetailFragmentCallListener {
        fun showDetailFragment(
            detailFragment: Class<out Fragment>,
            arguments: Bundle?,
            transitionImage: View
        )
    }
}
