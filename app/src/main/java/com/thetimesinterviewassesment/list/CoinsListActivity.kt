package com.thetimesinterviewassesment.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.thetimesinterviewassesment.CoinsRepository
import com.thetimesinterviewassesment.ConnectivityHelper
import com.thetimesinterviewassesment.R
import com.thetimesinterviewassesment.list.model.CoinListItem
import com.thetimesinterviewassesment.model.Coin
import com.thetimesinterviewassesment.model.ErrorAlertDialog
import com.thetimesinterviewassesment.model.FilterByContent
import com.thetimesinterviewassesment.model.ProgressIndicator
import com.thetimesinterviewassesment.model.SortByContent
import com.thetimesinterviewassesment.ui.theme.Green
import com.thetimesinterviewassesment.ui.theme.GreenAppBar
import com.thetimesinterviewassesment.ui.theme.LightGreen
import com.thetimesinterviewassesment.ui.theme.MediumGreen
import com.thetimesinterviewassesment.ui.theme.TheTimesInterviewAssesmentTheme

class CoinsListActivity : ComponentActivity() {

    private lateinit var repository: CoinsRepository
    private lateinit var factory: CoinsListViewModelFactory
    private lateinit var viewModel: CoinsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = CoinsRepository()
        factory = CoinsListViewModelFactory(repository, ConnectivityHelper(applicationContext))
        viewModel = ViewModelProvider(this, factory)[CoinsListViewModel::class.java]

        setContent {
            TheTimesInterviewAssesmentTheme {
                val coinsListUIState by viewModel.coinsListUiState.collectAsState()
                val coinsListSortType by viewModel.sortTypeStateState.collectAsState()

                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(15.dp),
                ) {
                    HomeContent(coinsListUIState)
                    ListSortType(coinsListSortType)
                }
            }
        }
    }

    @Composable
    fun HomeContent(coinsUIState: CoinsListUIState) {
        when (coinsUIState) {
            is CoinsListUIState.Loading -> ProgressIndicator(enabled = true)
            is CoinsListUIState.Success -> CoinsUI(list = coinsUIState.list)
            is CoinsListUIState.Error -> ErrorAlertDialog(error = coinsUIState.error)
        }
    }

    @Composable
    fun ListSortType(sortTypeState: SortByState) {
        when (sortTypeState) {
            is SortByState.Rank -> CoinsUI(list = sortTypeState.list)
            is SortByState.Alphabetical -> CoinsUI(list = sortTypeState.list)
            else -> {}
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    fun CoinsUI(list: List<Coin>) {

        val listState = rememberLazyListState()

        var refreshCount by remember { mutableStateOf(1) }

        // API call
        LaunchedEffect(key1 = refreshCount) {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    //Filters
                    Filters(list)
                    //Recycler
                    LazyColumn(
                        state = listState,
                    ) {
                        itemsIndexed(
                            items = list,
                            key = { _, item -> item.id },
                        ) { _, item ->
                            CoinListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(),
                                coin = item,
                            )
                        }
                    }

                }

            }
        )

    }

    @Composable
    fun Filters(list: List<Coin>) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.weight(1f)
            ) {
                SortCoinsBy()
            }
            Column(
                Modifier.weight(1f)
            ) {
                FilterByTag(list)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FilterByTag(list: List<Coin>) {
        val checkBoxList = listOf(
            FilterByContent("New", FilterByState.New),
            FilterByContent("Active", FilterByState.Active)
        )
        var newCheckbox by remember { mutableStateOf(false) }
        var activeCheckbox by remember { mutableStateOf(false) }

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.padding(10.dp)

        ) {
            TextField(
                modifier = Modifier.menuAnchor(), // The `menuAnchor` modifier must be passed to the text field for correctness.
                readOnly = true,
                value = "Filter By",
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = MediumGreen,
                    unfocusedContainerColor = MediumGreen,
                    unfocusedTrailingIconColor = White,
                    focusedTrailingIconColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.background(LightGreen),
                onDismissRequest = { expanded = false },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = newCheckbox,
                        onCheckedChange = { checked_ ->
                            newCheckbox = checked_
                            expanded = false
                            viewModel.filterBy(newCheckbox, checkBoxList[0].type, list)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Green,
                            checkmarkColor = White,
                            uncheckedColor = White,
                        )
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = checkBoxList[0].name
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = activeCheckbox,
                        onCheckedChange = { checked_ ->
                            activeCheckbox = checked_
                            expanded = false
                            viewModel.filterBy(activeCheckbox, checkBoxList[0].type, list)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Green,
                            checkmarkColor = White,
                            uncheckedColor = White,
                        )
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = checkBoxList[1].name
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SortCoinsBy() {
        val options = listOf(
            SortByContent("Rank", R.drawable.icn_sort_rank, SortByState.RankSelected),
            SortByContent("Alphabetical Order", R.drawable.icn_sort_alphabetical, SortByState.AlphabeticalSelected),
        )
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0].name) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)

        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .wrapContentSize(), // The `menuAnchor` modifier must be passed to the text field for correctness.
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text("Sort By") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = Green,
                    unfocusedContainerColor = Green,
                    unfocusedTrailingIconColor = White,
                    focusedTrailingIconColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.background(LightGreen),
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.name) },
                        onClick = {
                            expanded = false

                            viewModel.sortBy(selectionOption.type)
                            selectedOptionText = selectionOption.name
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = selectionOption.icon),
                                contentDescription = "Sort By $selectionOption",
                                tint = GreenAppBar,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    )
                }
            }
        }
    }

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
