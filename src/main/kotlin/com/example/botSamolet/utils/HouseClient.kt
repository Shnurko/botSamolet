package com.example.botSamolet.utils

import com.example.botSamolet.models.Favorites
import com.example.botSamolet.models.House
import com.example.botSamolet.models.Types
import com.example.botSamolet.models.Users
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/types")
    fun getTypes(
        @RequestParam("article") article: String = "%"
    ): List<Types>

    @GetMapping("/{id}/history")
    fun getHouseHistory(@PathVariable("id") id: Int): List<House>

    @PostMapping("/user")
    fun saveUser(@RequestBody user: Users): Users

    @GetMapping("/favorite")
    fun getFavorite(
        @RequestParam("userid") userid: Long?,
        @RequestParam("id") id: Long?
    ): Favorites

    @DeleteMapping("/favorite")
    fun deleteFavorite(
        @RequestParam("userid") userid: Long?,
        @RequestParam("id") id: Long?
    ): Int

    @PostMapping("/favorite")
    fun saveFavorite(@RequestBody request: Favorites): Int

    @GetMapping("/favorites")
    fun getFavorites(
        @RequestParam("userid") userid: Long?
    ): List<Favorites>
}