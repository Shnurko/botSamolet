package com.example.botSamolet.utils

import com.example.botSamolet.models.House
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "houseClient", url = "\${gateway.api}")
interface HouseClient {
    @GetMapping
    fun getHouses(): List<House>
}