package com.example.cryptoapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.databinding.FragmentCoinDetailBinding
import com.example.cryptoapp.presentation.CoinApplication.Companion.LOG_DEBUG_TAG
import com.example.cryptoapp.presentation.MainActivity
import com.example.cryptoapp.presentation.viewmodels.AppViewModelFactory
import com.example.cryptoapp.presentation.viewmodels.CoinDetailViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CoinDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private lateinit var viewModel: CoinDetailViewModel
    private var _binding: FragmentCoinDetailBinding? = null
    private val binding: FragmentCoinDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentCoinDetailBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainActivitySubcomponent.inject(this)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CoinDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(LOG_DEBUG_TAG, "Created CoinDetailFragment")
        _binding = FragmentCoinDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments == null || !requireArguments().containsKey(COIN_TO_SHOW)) {
            parentFragmentManager.popBackStack()
            return
        }
        arguments?.getString(COIN_TO_SHOW)?.let { coin ->
            viewModel.getDetailInfo(coin).observe(viewLifecycleOwner) {
                binding.tvPrice.text = it.price
                binding.tvMinPrice.text = it.lowDay
                binding.tvMaxPrice.text = it.highDay
                binding.tvLastMarket.text = it.lastMarket
                binding.tvLastUpdate.text = it.lastUpdate
                binding.tvFromSymbol.text = it.fromSymbol
                binding.tvToSymbol.text = it.toSymbol
                Picasso.get().load(it.imageUrl).into(binding.ivLogoCoin)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val COIN_TO_SHOW = "fSym"

        fun createArguments(coinToShow: String): Bundle {
            val bundle = Bundle()
            bundle.putString(COIN_TO_SHOW, coinToShow)
            return bundle
        }
    }
}
