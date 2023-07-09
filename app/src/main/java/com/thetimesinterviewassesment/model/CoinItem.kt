package com.thetimesinterviewassesment.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thetimesinterviewassesment.R

@Composable
fun CoinItem(coin: Coin) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { TODO("Add Detail View") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painterResource(id = R.drawable.icn_coin), // TODO: Substitute with response image
                contentScale = ContentScale.Crop,
                contentDescription = "Artist image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(84.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
            ) {
                Text(text = coin.name)
                Text(text = coin.symbol)
                Text(text = coin.type)
            }
        }
    }
}

@Composable
fun CoinImage() {
    Image(
        painterResource(id = R.drawable.icn_coin),
        contentScale = ContentScale.Crop,
        contentDescription = "Artist image",
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
    )
}

@Preview
@Composable
fun CoinItemPreview() {
    val coin = Coin(0, "btc-bitcoin", "Bitcoin", "BTC", 1, "coin", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png")
    CoinItem(coin = coin)
}