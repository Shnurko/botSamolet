package com.example.botSamolet.repositories

import com.example.botSamolet.models.Favorites
import com.example.botSamolet.models.House
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface FavoritesRepository : ReactiveCrudRepository<Favorites, Long>{
    @Modifying
    @Query(
        value = "INSERT INTO favorites " +
                "VALUES( :userid, :id )"
    )
    fun insert(userid: Long?, id: Int?): Mono<Int>

    @Modifying
    @Query("DELETE FROM favorites WHERE id = :id AND userid =:userid")
    fun delete(userid: Long?, id: Int?): Mono<Int>

    @Query(
        value = "select userid, id " +
                "from favorites " +
                "where id = :id " +
                "and userid = :userid"
    )
    fun select(userid: Long?, id: Int?): Flux<House>

    @Query(
        value = "select userid, id " +
                "from favorites " +
                "where userid = :userid"
    )
    fun selectall(userid: Long?): Flux<House>
}