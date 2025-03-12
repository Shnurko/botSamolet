package com.example.botSamolet.repositories

import com.example.botSamolet.models.House
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface HouseRepository : ReactiveCrudRepository<House, Long> {
    //fun save(house: House): Mono<House>
    //fun saveAll(house: List<House>): Flux<House>
    //fun findById(id: Int): Mono<House>
    //override fun findAll(): Flux<House>

    @Modifying
    @Query("UPDATE house SET image = :image WHERE id = :id")
    fun updateImage(id: Int?, image: String?): Mono<Int>
    @Modifying
    @Query("DELETE FROM house WHERE id = :id")
    fun delete(id: Int): Mono<Int>

    @Query(
        value = "select id, date, land_area, area, price, url, " +
                "technology, status, article, type, completion_date, land_price, image " +
                "from house " +
                "where id = :id " +
                "order by date"
    )
    fun getHouse(id: Int): Flux<House>

    @Query(
        value = "select distinct on(id) id, date, land_area, area, price, url, " +
                "technology, status, article, type, completion_date, land_price, image " +
                "from house " +
                "where type like :type " +
                "and article like :article " +
                "and ( technology like :technology " +
                "    or technology is null )" +
                //"and price between :priceLow and :priceHigh " +
                "order by id, date desc"
    )
    fun getHouses(type: String, article: String,
                  technology: String): Flux<House>

    @Query(
        value = "select distinct type " +
                "from house " +
                "where article like :article " +
                "order by type"
    )
    fun getTypes(article: String): Flux<String>
}