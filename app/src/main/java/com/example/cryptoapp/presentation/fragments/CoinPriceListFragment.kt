package com.example.cryptoapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.databinding.FragmentCoinPriceListBinding
import com.example.cryptoapp.domain.entities.CoinInfoEntity
import com.example.cryptoapp.presentation.CoinApplication.Companion.LOG_DEBUG_TAG
import com.example.cryptoapp.presentation.MainActivity
import com.example.cryptoapp.presentation.adapters.PriceListAdapter
import com.example.cryptoapp.presentation.viewmodels.AppViewModelFactory
import com.example.cryptoapp.presentation.viewmodels.CoinPriceListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinPriceListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private lateinit var viewModel: CoinPriceListViewModel
    private lateinit var onFragmentCallListener: OnFragmentCallListener
    private var _binding: FragmentCoinPriceListBinding? = null
    private val binding: FragmentCoinPriceListBinding
        get() = _binding ?: throw RuntimeException("FragmentCoinPriceListBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainActivitySubcomponent.inject(this)

        if (requireActivity() is OnFragmentCallListener) {
            onFragmentCallListener = requireActivity() as OnFragmentCallListener
        } else throw RuntimeException("Parent activity doesn't implement OnFragmentCallListener interface.")

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CoinPriceListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(LOG_DEBUG_TAG, "Created CoinPriceListFragment")
        _binding = FragmentCoinPriceListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PriceListAdapter(requireContext())
        adapter.onItemClickListener = setupItemClickListener()
        binding.rvCoinPriceList.adapter = adapter

        lifecycleScope.launch {
            viewModel.priceList.collectLatest {
                Log.d(LOG_DEBUG_TAG, "Adapter update")
                adapter.submitData(it)
            }
//            delay(3000)
//            adapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupItemClickListener(): PriceListAdapter.OnItemClickListener {
        return object : PriceListAdapter.OnItemClickListener {
            override fun onItemClick(coinInfo: CoinInfoEntity) {
                CoinDetailFragment().apply {
                    arguments = CoinDetailFragment.createArguments(coinInfo.fromSymbol)
                }.also { onFragmentCallListener.showFragment(it) }
            }
        }
    }

    interface OnFragmentCallListener { fun showFragment(fragmentToShow: Fragment) }
}
