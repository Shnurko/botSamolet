package com.example.botSamolet.commands

import com.example.botSamolet.models.CommandName
import com.example.botSamolet.models.HandlerName
import com.example.botSamolet.repositories.HouseRepository
import com.example.botSamolet.services.HouseService
import com.example.botSamolet.utils.createMessageWithInlineButtons
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class TypeCommand(private val houseRep: HouseRepository,
                  private val houseService: HouseService
) : BotCommand(CommandName.TYPE.text, "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {

        var text = ""

        val callback = HandlerName.BOT_ANSWER.text

        var inlineButtons: List<List<Pair<String, String>>> = listOf(listOf())

        //val types = houseRep.getTypes(article = arguments[0]).collectList().block()
        val types = houseService.fetchTypes(article = arguments[0])

        types.forEach {
            var new = it.type
            if (it.type == "Вилла Без комплектации"){
                return@forEach
            }
            if (it.type == "Вилла ㅤ"){
                new = "Вилла%"
            }
            inlineButtons += listOf(listOf("$callback|${arguments[0]}/0/${new}/*" to it.type.toString()))
        }

        inlineButtons += listOf(listOf("$callback|menu" to "<< назад"))

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