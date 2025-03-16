package com.example.botSamolet.commands

import com.example.botSamolet.models.CommandName
import com.example.botSamolet.models.HandlerName
import com.example.botSamolet.models.House
import com.example.botSamolet.repositories.HouseRepository
import com.example.botSamolet.services.HouseService
import com.example.botSamolet.utils.createMessageWithInlineButtons
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class GetCommand(private val houseRep: HouseRepository,
                 private val houseService: HouseService) : BotCommand(CommandName.GET.text, "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        var all = "Все"
        var avail = "В продаже"
        var sold = "Проданные"
        var text = ""

        val callback = HandlerName.BOT_ANSWER.text

        val test = houseService.fetchHouses()

        var inlineButtons: List<List<Pair<String, String>>> = listOf(listOf())

        if (arguments[3] != "*"){
            arguments.drop(3).forEach {
                val house = houseRep.getHouse(it.toInt()).collectList().block()?.last()
                inlineButtons += listOf(listOf("$callback|${house?.id}" to "\uD83C\uDFE0 ${house?.id} дом ${house?.article} ${house?.price} рублей"))
            }
        } else {

            var houses =
                houseRep.getHouses(type = arguments[2], article = arguments[0], technology = "%")
                    .collectList()
                    .block()

            if (arguments[1].toInt() != 0) {
                houses = houses?.filter { it.status == arguments[1].toInt() }
            }

            when (arguments[1].toInt()) {
                0 -> all = "✅Все"
                1 -> avail = "✅В продаже"
                3 -> sold = "✅Проданные"
            }

            //var inlineButtons: List<List<Pair<String, String>>> = listOf(
            inlineButtons += listOf(
                listOf(
                    "$callback|${arguments[0]}/0/${arguments[2]}/*" to all,
                    "$callback|${arguments[0]}/1/${arguments[2]}/*" to avail,
                    "$callback|${arguments[0]}/3/${arguments[2]}/*" to sold
                )
            )

            houses?.forEach {
                inlineButtons += listOf(listOf("$callback|${it.id}" to "\uD83C\uDFE0 ${it.id} дом ${it.article} ${it.price} рублей"))
            }

        }

        inlineButtons += listOf(listOf("$callback|${arguments[0]}/0/%/*" to "<< назад"))

        when(arguments[0]){
            "ДМИ%" -> text = "Дмитров Дом"
            "ИСТ%" -> text = "Истра Дом"
            "ПУШ%" -> text = "Пушкино Дом"
            "ЭММ%" -> text = "PORT EMM ZAVIDOVO"
            "FAV" -> text = "Избранное"
        }

        absSender.execute(
            createMessageWithInlineButtons(
                chat.id.toString(),
                text,
                inlineButtons
            )
        )
    }
}