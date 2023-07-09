package com.thetimesinterviewassesment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thetimesinterviewassesment.model.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: MainActivityRepository) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<CoinsUIState>(CoinsUIState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val uiState = _uiState.asStateFlow()


    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            val response = repository.getCoins()
            if (response.isEmpty()) {
                _uiState.emit(CoinsUIState.Error("Something went wrong. Please try again."))
            } else {
                _uiState.emit(CoinsUIState.Success(response, emptyList()))
            }
        }
    }

    fun getCoinById(id: String) {
        viewModelScope.launch {
            val response = repository.getCoinById(id)
        }
    }

    sealed class CoinsUIState {
        data class Success(
            val coinsList: List<Coin> = listOf(),
            val filteredList: List<Coin> = listOf(),
        ) : CoinsUIState() {
            val coins get() = coinsList
        }

        data class Error(val error: String) : CoinsUIState()

        object Loading : CoinsUIState()
    }
}

class ViewModelFactory(
    private val repository: MainActivityRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainActivityViewModel(repository as MainActivityRepository) as T

    }
}
