package com.example.cryptoapp.domain

import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke() = repository.loadData()
}