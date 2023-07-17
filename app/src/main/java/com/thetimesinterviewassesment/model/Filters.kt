package com.thetimesinterviewassesment.model

import com.thetimesinterviewassesment.list.FilterByState
import com.thetimesinterviewassesment.list.SortByState

data class FilterByContent(
    val name: String,
    val type: FilterByState,
)

data class SortByContent(
    val name: String,
    val icon: Int,
    val type: SortByState,
)
