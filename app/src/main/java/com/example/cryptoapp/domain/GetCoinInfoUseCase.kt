package com.example.cryptoapp.domain

import javax.inject.Inject

class GetCoinInfoUseCase @Inject constructor(
    private var repository: Repository
) {

    operator fun invoke(fSym: String) = repository.getCoinInfo(fSym)
}