package com.example.botSamolet.repositories

import com.example.botSamolet.models.Users
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : ReactiveCrudRepository<Users, Long>