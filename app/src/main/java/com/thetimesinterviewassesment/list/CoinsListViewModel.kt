package com.thetimesinterviewassesment.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.model.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinsListViewModel(private val repository: CoinsRepository) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _coinsListUiState = MutableStateFlow<CoinsListUIState>(CoinsListUIState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val coinsListUiState = _coinsListUiState.asStateFlow()


    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            val response = repository.getCoins()
            if (response.isNullOrEmpty()) {
                _coinsListUiState.emit(CoinsListUIState.Error("Something went wrong. Please try again."))
            } else {
                _coinsListUiState.emit(CoinsListUIState.Success(response, emptyList()))
            }
        }
    }
}

sealed class CoinsListUIState {
    data class Success(
        val coinsList: List<Coin> = listOf(),
        val filteredList: List<Coin> = listOf(),
    ) : CoinsListUIState() {
        val coins get() = coinsList
    }

    data class Error(val error: String) : CoinsListUIState()
    object Loading : CoinsListUIState()
}

class CoinsListViewModelFactory(
    private val repository: CoinsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CoinsListViewModel(repository) as T

    }
}
