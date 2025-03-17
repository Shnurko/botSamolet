package com.example.botSamolet.commands

import com.example.botSamolet.models.CommandName
import com.example.botSamolet.models.HandlerName
import com.example.botSamolet.utils.createMessageWithInlineButtons
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class MenuCommand() : BotCommand(CommandName.MENU.text, "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        val callback = HandlerName.BOT_ANSWER.text

        var inlineButtons: List<List<Pair<String, String>>> = listOf(listOf())

        inlineButtons += listOf(listOf("$callback|ДМИ%/0/%/*" to "\uD83C\uDFE1 Дмитров Дом"))
        inlineButtons += listOf(listOf("$callback|ИСТ%/0/%/*" to "\uD83C\uDFD8 Истра Дом"))
        inlineButtons += listOf(listOf("$callback|ПУШ%/0/%/*" to "\uD83C\uDFE0 Пушкино Дом"))
        inlineButtons += listOf(listOf("$callback|ЭММ%/0/%/*" to "\uD83C\uDFE3 PORT EMM ZAVIDOVO"))
        inlineButtons += listOf(listOf("$callback|FAV" to "⭐Избранное"))

        absSender.execute(
            createMessageWithInlineButtons(
                chat.id.toString(),
                "Выберите проект",
                inlineButtons
            )
        )
    }
}