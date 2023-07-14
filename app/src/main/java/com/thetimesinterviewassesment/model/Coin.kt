package com.thetimesinterviewassesment.model

import android.nfc.Tag
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Coin(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("symbol") val symbol: String,
    @JsonProperty("rank") val rank: Int,
    @JsonProperty("type") val type: String,
    @JsonProperty("logo") val image: String? = "",
    @JsonProperty("description") val description: String? = "",

//    @JsonProperty("tags")val tags: List<Tag>,
)

data class Tag(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
)
