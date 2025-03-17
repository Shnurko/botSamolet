package com.example.botSamolet.services

import com.example.botSamolet.models.Favorites
import com.example.botSamolet.models.House
import com.example.botSamolet.models.Types
import com.example.botSamolet.models.Users
import com.example.botSamolet.utils.HouseClient
import org.springframework.stereotype.Service

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

    fun fetchTypes(article: String): List<Types> {
        return houseClient.getTypes(article)
    }

    fun fetchHouseHistory(id: Int): List<House> = houseClient.getHouseHistory(id)

    fun saveUser(user: Users): Users {
        return houseClient.saveUser(user)
    }

    fun getFavorite(userid: Long?, id: Long?): Favorites {
        return houseClient.getFavorite(userid, id)
    }

    fun deleteFavorite(userid: Long?, id: Long?): Int {
        return houseClient.deleteFavorite(userid, id)
    }

    fun saveFavorite(userid: Long?, id: Long?): Int {
        return houseClient.saveFavorite(request = Favorites(userid = userid, id = id!!.toInt()))
    }

    fun getFavorites(userid: Long?): List<Favorites> {
        return houseClient.getFavorites(userid)
    }
}