package com.example.cryptoapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.databinding.FragmentCoinPriceListBinding
import com.example.cryptoapp.domain.CoinInfoEntity
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
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CoinPriceListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Created fragment: ", "price list")
        _binding = FragmentCoinPriceListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CoinInfoAdapter(requireContext())
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinInfoEntity) {
                if (requireActivity() is OnFragmentCallListener) {
                    val fragmentToShow = CoinDetailFragment()
                    fragmentToShow.arguments = CoinDetailFragment.newBundle(coinPriceInfo.fromSymbol)
                    onFragmentCallListener = requireActivity() as OnFragmentCallListener
                    onFragmentCallListener.showFragment(fragmentToShow)
                } else throw RuntimeException("Parent activity doesn't implement OnFragmentCallListener interface.")
            }
        }
        binding.rvCoinPriceList.adapter = adapter

        viewModel.priceList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnFragmentCallListener { fun showFragment(fragmentToShow: Fragment) }
}
