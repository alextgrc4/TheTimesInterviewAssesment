package com.thetimesinterviewassesment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.model.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val repository: CoinsRepository,
    coinId: String?,
) : ViewModel() {

    private val _coinUiState = MutableStateFlow<CoinUIState>(CoinUIState.Loading)
    val coinUiState = _coinUiState.asStateFlow()

    init {
        getCoinById(coinId)
    }

    private fun getCoinById(id: String?) {
        viewModelScope.launch {
            val response = if (!id.isNullOrEmpty()) repository.getCoinById(id) else null
            if (response == null) {
                _coinUiState.emit(CoinUIState.Error("Something went wrong. Please try again."))
            } else {
                _coinUiState.emit(CoinUIState.Success(response))
            }
        }
    }
}

sealed class CoinUIState {
    data class Success(val coin: Coin) : CoinUIState()
    data class Error(val error: String) : CoinUIState()
    object Loading : CoinUIState()
}

class CoinDetailViewModelFactory(
    private val repository: CoinsRepository,
    private val coinId: String?
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CoinDetailViewModel(repository, coinId) as T

    }
}