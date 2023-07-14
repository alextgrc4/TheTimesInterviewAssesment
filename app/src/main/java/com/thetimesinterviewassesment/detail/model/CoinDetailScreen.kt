package com.thetimesinterviewassesment.detail.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.thetimesinterviewassesment.R
import com.thetimesinterviewassesment.model.Coin
import com.thetimesinterviewassesment.ui.theme.GreenAppBar
import com.thetimesinterviewassesment.ui.theme.LightBlue
import com.thetimesinterviewassesment.ui.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(coin: Coin) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Description",
                        color = GreenAppBar,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .fillMaxSize(),
            ) {
                CoinDetailContent(coin)
            }
        }
    )
}

@Composable
fun CoinDetailContent(coin: Coin) {
    CoinDetailCard(coin)
    CoinDescriptionCard(coin.description.orEmpty())
}

@Composable
fun CoinDetailCard(coin: Coin) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            LightBlue,
                            LightGreen
                        )
                    )
                )
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp),
        ) {
            CoinImage(coin.image)
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
            ) {
                Text(
                    text = coin.name + " (${coin.symbol})",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 10.dp),
                )
                CoinRank(coin.rank)
            }
        }
    }
}

@Preview
@Composable
fun CoinImage(image: String? = "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png") {
    if (image.isNullOrEmpty()) {
        Image(
            painterResource(id = R.drawable.icn_no_coin),
            alignment = Alignment.Center,
            contentDescription = "No Coin Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
        )
    } else {
        Image(
            rememberAsyncImagePainter(image),
            alignment = Alignment.Center,
            contentDescription = "URL Coin Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
        )
    }
}

@Composable
fun CoinRank(rank: Int = 7) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Ranked",
            color = Color.White,
        )
        Box(modifier = Modifier.padding(horizontal = 5.dp)) {
            val image = painterResource(id = R.drawable.icn_rank)
            Image(painter = image, contentDescription = null, modifier = Modifier.size(25.dp))
            Text(
                text = rank.toString(),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CoinDescriptionCard(description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .background(LightGreen)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "About",
                color = Color.White,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 10.dp),
            )
            Text(
                text = description,
                color = Color.White,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Preview
@Composable
fun CoinDetailScreenPreview() {
    val coin = Coin(
        "btc-bitcoin",
        "Bitcoin",
        "BTC",
        1,
        "coin",
        "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png"
    )
    CoinDetailContent(coin = coin)
}