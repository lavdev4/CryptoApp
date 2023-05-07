package com.example.cryptoapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.databinding.FragmentCoinDetailBinding
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
        Log.d("Created fragment: ", "detail")
        _binding = FragmentCoinDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments == null || !requireArguments().containsKey(EXTRA_FROM_SYMBOL)) {
            parentFragmentManager.popBackStack()
            return
        }
        val fromSymbol = arguments?.getString(EXTRA_FROM_SYMBOL)
        fromSymbol?.let { _fromSymbol ->
            viewModel.getDetailInfo(_fromSymbol).observe(viewLifecycleOwner) {
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
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newBundle(fromSymbol: String): Bundle {
            val bundle = Bundle()
            bundle.putString(EXTRA_FROM_SYMBOL, fromSymbol)
            return bundle
        }
    }
}
