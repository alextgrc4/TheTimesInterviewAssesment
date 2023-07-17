package com.thetimesinterviewassesment.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.ConnectivityHelper
import com.thetimesinterviewassesment.model.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val repository: CoinsRepository,
    private val connectivityHelper: ConnectivityHelper
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _coinsListUiState = MutableStateFlow<CoinsListUIState>(CoinsListUIState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val coinsListUiState = _coinsListUiState.asStateFlow()
    var coinList = listOf<Coin>()
    var sortType: SortByState? = null

    init {
        getCoins()
    }

    private fun getCoins() {
        viewModelScope.launch {
            if (connectivityHelper.isOnline()) {
                val response = repository.getCoins()
                if (response.isNullOrEmpty()) {
                    _coinsListUiState.emit(CoinsListUIState.Error("Something went wrong. Please try again."))
                } else {
                    coinList = response
                    _coinsListUiState.emit(CoinsListUIState.Success(response))
                }
            } else {
                _coinsListUiState.emit(CoinsListUIState.Error("No internet connection. Please try again later."))
            }
        }
    }

    fun sortBy(type: SortByState, list: List<Coin>) {
        viewModelScope.launch {
            sortType = type
            val sortedList = if (SortByState.Alphabetical == type) {
                val startsWithLetter = "^[a-zA-Z]".toRegex()
                list.map { it.copy() }.filter { it.name.contains(startsWithLetter) && it.rank != 0 }.sortedBy { it.name }

            } else {
                list.map { it.copy() }.filterNot { it.rank == 0 }.sortedBy { it.rank }
            }
            _coinsListUiState.emit(CoinsListUIState.Success(sortedList))
        }
    }

    fun filterBy(checked: Boolean, filterType: FilterByState, list: List<Coin>) {
        viewModelScope.launch {
            if (checked) {
                val filteredList = when (filterType) {
                    FilterByState.New -> list.map { it.copy() }.filter { it.isNew == true }
                    FilterByState.Active -> list.map { it.copy() }.filter { it.isActive == true }
                }
                _coinsListUiState.emit(CoinsListUIState.Success(filteredList))
            } else {
                sortType?.let { sortBy(it, coinList) }
            }
        }
    }
}

sealed class CoinsListUIState {
    data class Success(val list: List<Coin> = listOf()) : CoinsListUIState()
    data class Error(val error: String) : CoinsListUIState()
    object Loading : CoinsListUIState()
}

sealed class SortByState {
    object Rank : SortByState()
    object Alphabetical : SortByState()

}

sealed class FilterByState {
    object New : FilterByState()
    object Active : FilterByState()

}

class CoinsListViewModelFactory(
    private val repository: CoinsRepository,
    private val connectivityHelper: ConnectivityHelper
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CoinsListViewModel(repository, connectivityHelper) as T

    }
}
