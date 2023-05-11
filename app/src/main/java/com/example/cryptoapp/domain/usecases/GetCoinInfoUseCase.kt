package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.Repository
import javax.inject.Inject

class GetCoinInfoUseCase @Inject constructor(
    private var repository: Repository
) {

    operator fun invoke(coinName: String) = repository.getCoinInfo(coinName)
}