package com.example.botSamolet.services

import com.example.botSamolet.models.House
import com.example.botSamolet.utils.HouseClient
import org.springframework.stereotype.Service

@Service
class HouseService(private val houseClient: HouseClient) {
    fun fetchHouses(): List<House> {
        return houseClient.getHouses()
    }
}