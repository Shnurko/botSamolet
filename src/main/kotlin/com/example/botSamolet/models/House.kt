package com.example.botSamolet.models

import java.time.LocalDateTime

data class House(
    var internalid: Int? = null,
    val id: Int? = null,
    var date: LocalDateTime? = LocalDateTime.now(),
    val land_area: Double? = null,
    val area: Double? = null,
    var price: Int = 0,
    val url: String? = null,
    val technology: String? = null,
    var status: Int = 0,
    val article: String? = null,
    val type: String? = null,
    val completion_date: String? = null,
    val land_price: Int? = null,
    var image: String? = null
)