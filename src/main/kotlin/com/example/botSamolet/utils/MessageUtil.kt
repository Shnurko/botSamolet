package com.example.botSamolet.utils

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

fun createMessage(chatId: String, text: String) =
    SendMessage(chatId, text)
        .apply { enableMarkdown(true) }
        .apply { enableWebPagePreview() }
        //.apply { parseMode = "HTML" }

fun createMessageWithSimpleButtons(chatId: String, text: String, simpleButtons: List<List<String>>) =
    createMessage(chatId, text)
        .apply {
            replyMarkup = getSimpleKeyboard(simpleButtons)
        }

fun createMessageWithInlineButtons(chatId: String, text: String, inlineButtons: List<List<Pair<String, String>>>) =
    createMessage(chatId, text)
        .apply {
            replyMarkup = getInlineKeyboard(inlineButtons)
        }
        .apply { parseMode = "HTML" }

fun editMessageWithInlineButtons(chatId: String, text: String, inlineButtons: List<List<Pair<String, String>>>) {
    EditMessageReplyMarkup()
        .apply {
            replyMarkup = getInlineKeyboard(inlineButtons)
        }
        .chatId = chatId
}

fun getSimpleKeyboard(allButtons: List<List<String>>): ReplyKeyboard =
    ReplyKeyboardMarkup().apply {
        keyboard = allButtons.map { rowButtons ->
            val row = KeyboardRow()
            rowButtons.forEach { rowButton -> row.add(rowButton) }
            row
        }
        oneTimeKeyboard = true
    }

fun getInlineKeyboard(allButtons: List<List<Pair<String, String>>>): InlineKeyboardMarkup =
    InlineKeyboardMarkup().apply {
        keyboard = allButtons.map { rowButtons ->
            rowButtons.map { (data, buttonText) ->
                InlineKeyboardButton().apply {
                    text = buttonText
                    callbackData = data
                }
            }
        }
    }