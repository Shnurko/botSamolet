package com.example.botSamolet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class BotSamoletApplication

fun main(args: Array<String>) {
	runApplication<BotSamoletApplication>(*args)
}
