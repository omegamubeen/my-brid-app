package model

import kotlinx.serialization.Serializable

@Serializable
data class StockImageResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<StockImage>,
    val total_results: Int
)