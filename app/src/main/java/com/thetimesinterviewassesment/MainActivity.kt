package com.thetimesinterviewassesment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.thetimesinterviewassesment.model.Coin
import com.thetimesinterviewassesment.model.CoinItem
import com.thetimesinterviewassesment.ui.theme.TheTimesInterviewAssesmentTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: MainActivityRepository
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = MainActivityRepository()
        factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        setContent {
            TheTimesInterviewAssesmentTheme {
                val coinsUIState by viewModel.uiState.collectAsState()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    HomeContent(coinsUIState)
                }
            }
        }
    }

    @Composable
    fun HomeContent(coinsUIState: MainActivityViewModel.CoinsUIState) {
        when (coinsUIState) {
            is MainActivityViewModel.CoinsUIState.Loading -> ProgressIndicator()
            is MainActivityViewModel.CoinsUIState.Success -> CoinsUI(list = coinsUIState.coinsList)
            is MainActivityViewModel.CoinsUIState.Error -> ErrorAlertDialog(error = coinsUIState.error)
        }
    }

    @Composable
    fun CoinsUI(list: List<Coin>) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                // Refresh Button
                Button(
                    onClick = { viewModel.getCoins() },
                    shape = RoundedCornerShape(5.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 15.dp,
                        disabledElevation = 0.dp
                    ),
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Image(
                        painterResource(id = R.drawable.icn_refresh),
                        contentDescription = "Refresh button icon",
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Refresh", color = Color.White)
                }
            }

            //Recycler
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            ) {
                items(
                    items = list,
                    itemContent = {
                        CoinItem(coin = it)
                    })
            }
        }
    }

    @Composable
    fun ErrorAlertDialog(error: String) {
        MaterialTheme {
            Column {
                val openDialog = remember { mutableStateOf(false) }

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onCloseRequest.
                            openDialog.value = false
                        },
                        title = {
                            Text(text = error)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }

        }
    }

    @Preview
    @Composable
    fun ProgressIndicator() {
        Column(Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier
                    .height(10.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    }

    @Preview
    @Composable
    fun CoinsUIPreview() {
        val coins: MutableList<Coin> = mutableListOf()
        coins.add(Coin(0, "btc-bitcoin", "Bitcoin", "BTC", 1, "coin", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"))
        coins.add(Coin(1, "eth-ethereum", "Ethereum", "ETH", 2, "coin"))
        coins.add(Coin(2, "usdt-tether", "Tether", "USDT", 3, "token", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"))
        coins.add(Coin(3, "bnb-binance-coin", "Binance Coin", "BNB", 4, "coin", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"))

        CoinsUI(list = coins)
    }
}
