package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.Repository
import javax.inject.Inject

class GetCoinInfoListUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke() = repository.getCoinInfoList()
}