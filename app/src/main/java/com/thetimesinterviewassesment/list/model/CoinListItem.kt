package com.thetimesinterviewassesment.list.model

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thetimesinterviewassesment.R
import com.thetimesinterviewassesment.detail.CoinDetailActivity
import com.thetimesinterviewassesment.model.Coin
import com.thetimesinterviewassesment.ui.theme.GreenCoinItemBackground

@Composable
fun CoinListItem(
    modifier: Modifier = Modifier,
    coin: Coin,
    context: Context = LocalContext.current,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, CoinDetailActivity::class.java)
                intent.putExtra("COIN_ID", coin.id)
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = GreenCoinItemBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            CoinSymbolImage(coin.symbol)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
            ) {
                Text(text = coin.name, fontWeight = FontWeight.Bold)
                Text(text = coin.symbol)
                Text(
                    text = "#" + coin.rank,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Composable
fun CoinSymbolImage(symbol: String = "BTC") {
    Box(modifier = Modifier.padding(horizontal = 5.dp)) {
        val image = painterResource(id = R.drawable.icn_no_coin)
        Image(
            painter = image, contentDescription = "Coin list image", modifier = Modifier
                .size(100.dp)
                .padding(10.dp)
        )
        Text(
            text = symbol,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.ExtraBold,

            )
    }
}

@Preview
@Composable
fun CoinItemPreview() {
    val coin = Coin("btc-bitcoin", "Bitcoin", "BTC", 1, "coin", "https://static.coinpaprika.com/coin/btc-bitcoin/logo.png")
    CoinListItem(coin = coin)
}