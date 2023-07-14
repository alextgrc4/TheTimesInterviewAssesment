package com.thetimesinterviewassesment.list

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.thetimesinterviewassesment.BaseActivity
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.R
import com.thetimesinterviewassesment.list.model.CoinListItem
import com.thetimesinterviewassesment.model.Coin
import com.thetimesinterviewassesment.ui.theme.GreenAppBar
import com.thetimesinterviewassesment.ui.theme.TheTimesInterviewAssesmentTheme

class CoinsListActivity : BaseActivity() {

    private lateinit var repository: CoinsRepository
    private lateinit var factory: CoinsListViewModelFactory
    private lateinit var viewModel: CoinsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = CoinsRepository()
        factory = CoinsListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CoinsListViewModel::class.java]

        setContent {
            TheTimesInterviewAssesmentTheme {
                val coinsUIState by viewModel.coinsListUiState.collectAsState()

                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(15.dp),
                ) {
                    HomeContent(coinsUIState)
                }
            }
        }
    }

    @Composable
    fun HomeContent(coinsUIState: CoinsListUIState) {
        when (coinsUIState) {
            is CoinsListUIState.Loading -> ProgressIndicator(enabled = true)
            is CoinsListUIState.Success -> CoinsUI(list = coinsUIState.coinsList)
            is CoinsListUIState.Error -> ErrorAlertDialog(error = coinsUIState.error)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CoinsUI(list: List<Coin>) {

        val listState = rememberLazyListState()

        var refreshCount by remember { mutableStateOf(1) }

        // API call
        LaunchedEffect(key1 = refreshCount) {
            viewModel.getCoins()
            listState.scrollToItem(0)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Coins List",
                            color = GreenAppBar,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    },
                    modifier = Modifier.background(White)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        refreshCount++
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.icn_refresh),
                            contentDescription = "Refresh",
                            modifier = Modifier.size(30.dp),
                            tint = White
                        )
                    }
                )
            },
            content = { padding ->
                //Recycler
                LazyColumn(
                    state = listState,
                    modifier = Modifier.padding(10.dp),
                    contentPadding = padding,
                ) {
                    items(
                        items = list,
                        itemContent = {
                            CoinListItem(coin = it)
                        })
                }
            }
        )
    }

    @Preview
    @Composable
    fun CoinsUIPreview() {
        val coins: MutableList<Coin> = mutableListOf()
        coins.add(Coin("btc-bitcoin", "Bitcoin", "BTC", 1, "coin", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"))
        coins.add(Coin("eth-ethereum", "Ethereum", "ETH", 2, "coin"))
        coins.add(Coin("usdt-tether", "Tether", "USDT", 3, "token", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"))
        coins.add(Coin("bnb-binance-coin", "Binance Coin", "BNB", 4, "coin", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"))

        CoinsUI(list = coins)
    }
}
