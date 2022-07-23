package com.example.payment.model

data class trendingCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val block_time_in_minutes: Double,
    val image: Image,
    val market_data: MarketData,
    val last_updated: String,
    val localization: Localization,
    )
