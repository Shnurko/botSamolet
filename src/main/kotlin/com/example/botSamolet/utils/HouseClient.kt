package com.example.botSamolet.utils

import com.example.botSamolet.models.House
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Flux

@FeignClient(name = "houseClient", url = "\${gateway.api}")
interface HouseClient {
    @GetMapping
    fun getHouses(
        @RequestParam("type") type: String = "%",
        @RequestParam("article") article: String = "%",
        @RequestParam("technology") technology: String = "%"
    ): List<House>

    @GetMapping("/{id}")
    fun getHouse(@PathVariable("id") id: Int): House

    //@GetMapping("/types")//, consumes = [MediaType.APPLICATION_JSON_VALUE])
    @GetMapping("/types")//, consumes = [MediaType.APPLICATION_JSON_VALUE], headers = ["Accept-Charset=UTF-8"])
    fun getTypes(
        @RequestParam("article") article: String = "%"
    ): List<House>
}