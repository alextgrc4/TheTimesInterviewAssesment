package com.thetimesinterviewassesment.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.detail.model.CoinDetailScreen
import com.thetimesinterviewassesment.model.ErrorAlertDialog
import com.thetimesinterviewassesment.model.ProgressIndicator
import com.thetimesinterviewassesment.ui.theme.TheTimesInterviewAssesmentTheme

class CoinDetailActivity : ComponentActivity() {

    private lateinit var repository: CoinsRepository
    private lateinit var factory: CoinDetailViewModelFactory
    private lateinit var viewModel: CoinDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val coinId = intent.getStringExtra("COIN_ID")
        super.onCreate(savedInstanceState)

        repository = CoinsRepository()
        factory = CoinDetailViewModelFactory(repository, coinId)
        viewModel = ViewModelProvider(this, factory)[CoinDetailViewModel::class.java]


        setContent {
            TheTimesInterviewAssesmentTheme {
                val coinUIState by viewModel.coinUiState.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CoinDetailsContent(coinUIState = coinUIState)
                }
            }
        }
    }

    @Composable
    fun CoinDetailsContent(coinUIState: CoinUIState) {
        when (coinUIState) {
            is CoinUIState.Loading -> ProgressIndicator(enabled = true)
            is CoinUIState.Success -> CoinDetailScreen(coin = coinUIState.coin)
            is CoinUIState.Error -> ErrorAlertDialog(error = coinUIState.error)
        }
    }

}