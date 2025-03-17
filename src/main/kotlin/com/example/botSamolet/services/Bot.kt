package com.example.botSamolet.services

import com.example.botSamolet.handlers.CallbackHandler
import com.example.botSamolet.utils.createMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class Bot(
    commands: Set<BotCommand>,
    callbackHandlers: Set<CallbackHandler>,
    @Value("\${telegram.token}")
    botToken: String
) : TelegramLongPollingCommandBot(botToken) {

    private lateinit var handlerMapping: Map<String, CallbackHandler>

    init {
        registerAll(*commands.toTypedArray())
        handlerMapping = callbackHandlers.associateBy { it.name.text }
    }

    override fun getBotUsername(): String = "\${telegram.bot_name}"

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasMessage()) {
            val chatId = update.message.chatId.toString()
            if (update.message.hasText()) {

            } else {
                execute(createMessage(chatId, "Я понимаю только текст!"))
            }
        } else if (update.hasCallbackQuery()) {
            val callbackQuery = update.callbackQuery
            val callbackData = callbackQuery.data

            val callbackQueryId = callbackQuery.id
            execute(AnswerCallbackQuery(callbackQueryId))

            val callbackArguments = callbackData.split("|")
            val callbackHandlerName = callbackArguments.first()

            handlerMapping.getValue(callbackHandlerName)
                .processCallbackData(
                    this,
                    callbackQuery,
                    callbackArguments.subList(1, callbackArguments.size)
                )
        }
    }
}