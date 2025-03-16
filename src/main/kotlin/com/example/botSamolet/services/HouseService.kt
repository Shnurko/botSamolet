package com.example.botSamolet.services

import com.example.botSamolet.models.House
import com.example.botSamolet.utils.HouseClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class HouseService(private val houseClient: HouseClient) {
    fun fetchHouses(
        type: String,
        article: String,
        technology: String
    ): List<House> {
        return houseClient.getHouses(type, article, technology)
    }

    fun fetchHouse(id: Int): House = houseClient.getHouse(id)

    fun fetchTypes(article: String): List<House> {
        return houseClient.getTypes(article)
    }
}