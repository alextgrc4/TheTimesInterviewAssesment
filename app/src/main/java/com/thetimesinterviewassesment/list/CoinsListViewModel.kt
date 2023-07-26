package com.thetimesinterviewassesment.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.ConnectivityHelper
import com.thetimesinterviewassesment.model.Coin
import kotlinx.coroutines.Dispatchers
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

    private val _sortTypeState = MutableStateFlow<SortByState>(SortByState.Unselected)
    val sortTypeStateState = _sortTypeState.asStateFlow()


    init {
        getCoins()
    }

    private fun getCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            if (connectivityHelper.isOnline()) {
                val response = repository.getCoins()
                if (response.isNullOrEmpty()) {
                    _coinsListUiState.emit(CoinsListUIState.Error("Something went wrong. Please try again."))
                } else {
                    _coinsListUiState.emit(CoinsListUIState.Success(response))
                }
            } else {
                _coinsListUiState.emit(CoinsListUIState.Error("No internet connection. Please try again later."))
            }
        }
    }

    fun sortBy(sortType: SortByState) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = when (val data = coinsListUiState.value) {
                is CoinsListUIState.Success -> data.list
                else -> emptyList()
            }

            if (sortType == SortByState.AlphabeticalSelected) {
                val startsWithLetter = "^[a-zA-Z]".toRegex()
                val alphabeticalSortedList = list.map { it.copy() }.filter { it.name.contains(startsWithLetter) && it.rank != 0 }.sortedBy { it.name }
                _sortTypeState.emit(SortByState.Alphabetical(alphabeticalSortedList))

            } else if (sortType == SortByState.RankSelected) {
                val rankSortedList = list.map { it.copy() }.filterNot { it.rank == 0 }.sortedBy { it.rank }
                _sortTypeState.emit(SortByState.Rank(rankSortedList))
            }
        }
    }

    fun filterBy(checked: Boolean, filterType: FilterByState, list: List<Coin>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checked) {
                val filteredList = when (filterType) {
                    FilterByState.New -> list.map { it.copy() }.filter { it.isNew == true }
                    FilterByState.Active -> list.map { it.copy() }.filter { it.isActive == true }
                }
                _coinsListUiState.emit(CoinsListUIState.Success(filteredList))
            } else {
                sortBy(sortTypeStateState.value)
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

    object Unselected : SortByState()

    object RankSelected : SortByState()

    object AlphabeticalSelected : SortByState()
    data class Rank(val list: List<Coin> = listOf()) : SortByState()
    data class Alphabetical(val list: List<Coin> = listOf()) : SortByState()

//    companion object{
//        fun sortBy(sortType: SortByState): List<Coin> {
//
//            val sortedList =if (sortType == Alphabetical){
//                val startsWithLetter = "^[a-zA-Z]".toRegex()
//                list.map { it.copy() }.filter { it.name.contains(startsWithLetter) && it.rank != 0 }.sortedBy { it.name }
//            } else  {
//                list.map { it.copy() }.filterNot { it.rank == 0 }.sortedBy { it.rank }
//            }
//            return sortedList
//        }
//    }
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
